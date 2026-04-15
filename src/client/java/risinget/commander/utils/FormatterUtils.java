package risinget.commander.utils;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import risinget.commander.Commander;
import org.slf4j.Logger;


public class FormatterUtils {

    private static final Logger LOGGER = Commander.getLogger(FormatterUtils.class);


    public static String getColoredText(Component text) {
        try {
            JsonObject json = JsonParser.parseString(FormatterUtils.toJson(text)).getAsJsonObject();
            return FormatterUtils.handleColorCodes(FormatterUtils.formatJsonMessage(json), false);
        } catch (JsonSyntaxException | IllegalStateException e) {
            return FormatterUtils.handleColorCodes(FormatterUtils.toJson(text), false);
        }
    }


    public static String toJson(Component message) {
        Gson gson = new Gson();
        // Serializar a JsonElement, luego a String
        JsonElement jsonElement = ComponentSerialization.CODEC
                .encodeStart(JsonOps.INSTANCE, message)
                .getOrThrow();

        return gson.toJson(jsonElement);
//        Text.object(TextObjectContents).toJson(message, registry);
//        return Text.Serialization.toJsonString(message, registry);
    }

    public static String formatJsonMessage(JsonObject jsonObject) {
        StringBuilder builder = new StringBuilder();
        if (jsonObject.has("color")) {
            builder.append(getColorCode(jsonObject.get("color").getAsString()));
        }

        if (jsonObject.has("translate")) {
            builder.append(handleTranslation(jsonObject));
        }

        return formatChatComponent(jsonObject, builder);
    }

    private static String handleTranslation(JsonObject jsonObject) {
        StringBuilder builder = new StringBuilder();
        String translate = jsonObject.get("translate").getAsString();
        Component translated = Component.translatable(translate);  // Obtener el texto traducido

        // Aquí obtenemos la cadena de texto traducido
        String translatedString = translated.getString();

        // Si existe el campo "with", procesamos los valores a insertar en los placeholders
        if (jsonObject.has("with")) {
            JsonArray withArray = jsonObject.getAsJsonArray("with");

            // Primero reemplazamos los placeholders numerados como '%1$s', '%2$s', etc.
            for (int i = 0; i < withArray.size(); i++) {
                JsonElement element = withArray.get(i);
                String value = "";

                // Si el valor es un objeto, lo formateamos recursivamente
                if (element.isJsonObject()) {
                    value = formatJsonMessage(element.getAsJsonObject());
                } else if (element.isJsonPrimitive()) {
                    value = element.getAsString();
                }

                // Reemplazamos el placeholder específico (por ejemplo, '%1$s')
                translatedString = translatedString.replaceFirst("%" + (i + 1) + "\\$s", value);
            }

            // Luego, reemplazamos los placeholders genéricos '%s'
            for (int i = 0; i < withArray.size(); i++) {
                JsonElement element = withArray.get(i);
                String value = "";

                // Si el valor es un objeto, lo formateamos recursivamente
                if (element.isJsonObject()) {
                    value = formatJsonMessage(element.getAsJsonObject());
                } else if (element.isJsonPrimitive()) {
                    value = element.getAsString();
                }

                // Reemplazamos el primer '%s' que encontramos
                translatedString = translatedString.replaceFirst("%s", value);
            }
        }
        // Finalmente, devolvemos el texto con los placeholders reemplazados
        builder.append(translatedString);

        return builder.toString();
    }


    public static String formatChatComponent(JsonObject component) {
        return formatChatComponent(component, new StringBuilder());
    }

    private static String formatChatComponent(JsonObject component, StringBuilder builder) {
        if (component.has("color")) {
            builder.append(getColorCode(component.get("color").getAsString()));
        }

        // Agrega estilos si están presentes
        if (component.has("strikethrough") && component.get("strikethrough").getAsBoolean()) builder.append("&m");
        if (component.has("obfuscated") && component.get("obfuscated").getAsBoolean()) builder.append("&k");
        if (component.has("bold") && component.get("bold").getAsBoolean()) builder.append("&l");
        if (component.has("italic") && component.get("italic").getAsBoolean()) builder.append("&o");
        if (component.has("underline") && component.get("underline").getAsBoolean()) builder.append("&u");

        return processExtraAndText(component, builder).toString();
    }

    private static StringBuilder processExtraAndText(JsonObject component, StringBuilder builder) {
        if (component.has("text")) {
            builder.append(component.get("text").getAsString());
        }

        if (component.has("extra")) {
            JsonArray extraArray = component.getAsJsonArray("extra");
            for (JsonElement element : extraArray) {
                if (element.isJsonObject()) {
                    builder.append(formatChatComponent(element.getAsJsonObject()));
                } else if (element.isJsonPrimitive()) {
                    builder.append(element.getAsString());
                }
            }
        }

        return builder;
    }


    public static String handleColorCodes(String message, boolean remove) {
        String regex = "[&§][0-9a-fA-Fk-oK-r]";
        if (remove) {
            return message.replaceAll(regex, "");
        } else {
            return message.replace("§", "&");
        }
    }
    public static String getColorCode(String colorName) {
        return switch (colorName) {
            case "black" -> "&0";
            case "dark_blue" -> "&1";
            case "dark_green" -> "&2";
            case "dark_aqua" -> "&3";
            case "dark_red" -> "&4";
            case "dark_purple" -> "&5";
            case "gold" -> "&6";
            case "gray" -> "&7";
            case "dark_gray" -> "&8";
            case "blue" -> "&9";
            case "green" -> "&a";
            case "aqua" -> "&b";
            case "red" -> "&c";
            case "light_purple" -> "&d";
            case "yellow" -> "&e";
            case "white" -> "&f";
            default -> HexUtil.hexToMinecraftColor(colorName);
        };
    }


    public static MutableComponent parseAndFormatText(String text) {
        MutableComponent result = Component.literal("");
        MutableComponent currentSegment = Component.literal("");
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '&' && i + 1 < chars.length) {
                if (!currentSegment.getSiblings().isEmpty()) {
                    result.append(currentSegment);
                    currentSegment = Component.literal("");
                }
                char colorCode = chars[i + 1];
                ChatFormatting format = getFormattingByCode(colorCode);
                if (format != null) {
                    currentSegment = currentSegment.withStyle(format);
                    i++; // Skip the color code
                }
            } else {
                currentSegment.append(Component.literal(String.valueOf(chars[i])));
            }
        }
        if (!currentSegment.getSiblings().isEmpty()) {
            result.append(currentSegment);
        }
        return result;
    }

    public static MutableComponent parseAndFormatColor(String text) {
        MutableComponent result = Component.literal("");
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '&' && i + 1 < chars.length) {
                char colorCode = chars[i + 1];
                ChatFormatting format = getFormattingByCode(colorCode);
                if (format != null) {
                    result.append(Component.literal("&" + colorCode).withStyle(format));
                    i++;
                }
            } else {
                result.append(Component.literal(String.valueOf(chars[i])));
            }
        }
        return result;
    }

     public static ChatFormatting getFormattingByCode(char code) {
         return switch (code) {
             case '0' -> ChatFormatting.BLACK;
             case '1' -> ChatFormatting.DARK_BLUE;
             case '2' -> ChatFormatting.DARK_GREEN;
             case '3' -> ChatFormatting.DARK_AQUA;
             case '4' -> ChatFormatting.DARK_RED;
             case '5' -> ChatFormatting.DARK_PURPLE;
             case '6' -> ChatFormatting.GOLD;
             case '7' -> ChatFormatting.GRAY;
             case '8' -> ChatFormatting.DARK_GRAY;
             case '9' -> ChatFormatting.BLUE;
             case 'a' -> ChatFormatting.GREEN;
             case 'b' -> ChatFormatting.AQUA;
             case 'c' -> ChatFormatting.RED;
             case 'd' -> ChatFormatting.LIGHT_PURPLE;
             case 'e' -> ChatFormatting.YELLOW;
             case 'f' -> ChatFormatting.WHITE;
             case 'k' -> ChatFormatting.OBFUSCATED;
             case 'l' -> ChatFormatting.BOLD;
             case 'm' -> ChatFormatting.STRIKETHROUGH;
             case 'n' -> ChatFormatting.UNDERLINE;
             case 'o' -> ChatFormatting.ITALIC;
             case 'r' -> ChatFormatting.RESET;
             default -> null;
         };
    }
}
