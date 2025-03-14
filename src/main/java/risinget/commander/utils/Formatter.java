package risinget.commander.utils;

import com.google.gson.*;
import com.mojang.brigadier.Command;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import risinget.commander.Commander;
import org.slf4j.Logger;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatter {

    public static final Logger LOGGER = Commander.getLogger(Formatter.class);


    public static String toJson(Text message) {
        // Obtener el RegistryManager local (BuiltinRegistries)
        RegistryWrapper.WrapperLookup registry = BuiltinRegistries.createWrapperLookup();

        if (registry == null) {
            throw new IllegalStateException("Local RegistryManager is not available");
        }

        // Serializar el Text a JSON
        String json = Text.Serialization.toJsonString(message, registry);
        return json;
    }


    private boolean onMessage(Text message) {
        Util.getIoWorkerExecutor().execute(() -> processMessage(message));
        return true;
    }
    private static void processMessage(Text message) {

        String json = TextColor.toJson(message);
        // Guardar el mensaje en texto plano
        String msgPlain = message.getString();
//        saveMessage(handleColorCodes(msgPlain, true), logFile);
        // Verificar si el JSON está vacío
        if (json != null && !json.isEmpty()) {
            try {
                // Parsear el JSON solo si no está vacío
                System.out.println(json);
                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                String formattedMessage = formatJsonMessage(jsonObject);
                System.out.println(formattedMessage);
//                saveMessage(handleColorCodes(formattedMessage, false), logFileColored);
            } catch (JsonSyntaxException | IllegalStateException e) {
                // Manejar errores de parsing
                System.err.println("Error al parsear el JSON: " + e.getMessage());
            }
        } else {
            System.out.println("El JSON está vacío o es nulo.");
        }

    }

    private static synchronized void saveMessage(String msg, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + timestamp + "] " + msg);
            writer.newLine();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static String formatJsonMessage(JsonObject jsonObject) {
        StringBuilder builder = new StringBuilder();
        if (jsonObject.has("translate")) {
            String translate = jsonObject.get("translate").getAsString();
            if (jsonObject.has("with")) {
                JsonArray withArray = jsonObject.getAsJsonArray("with");
                for (JsonElement element : withArray) {
                    if (element.isJsonObject()) {
                        JsonObject withObject = element.getAsJsonObject();
                        builder.append(formatJsonMessage(withObject));
                    } else if (element.isJsonPrimitive()) {
                        builder.append(element.getAsString());
                    }
                }
            }
        }
        if (jsonObject.has("color")) {
            String color = jsonObject.get("color").getAsString();
            builder.append(getColorCode(color));
        }
        if (jsonObject.has("text")) {
            String text = jsonObject.get("text").getAsString();
            builder.append(text);
        }
        if (jsonObject.has("extra")) {
            JsonArray extraArray = jsonObject.getAsJsonArray("extra");
            for (JsonElement element : extraArray) {
                if (element.isJsonObject()) {
                    JsonObject component = element.getAsJsonObject();
                    builder.append(formatChatComponent(component));
                } else if (element.isJsonPrimitive()) {
                    builder.append(element.getAsString());
                }
            }
        }
        return builder.toString();
    }

    private static String formatChatComponent(JsonObject component) {
        StringBuilder builder = new StringBuilder();
        if (component.has("color")) {
            String color = component.get("color").getAsString();
            builder.append(getColorCode(color));
        }
        if (component.has("strikethrough") && component.get("strikethrough").getAsBoolean()) {
            builder.append("&m");
        }
        if (component.has("obfuscated") && component.get("obfuscated").getAsBoolean()) {
            builder.append("&k");
        }
        if (component.has("bold") && component.get("bold").getAsBoolean()) {
            builder.append("&l");
        }
        if (component.has("italic") && component.get("italic").getAsBoolean()) {
            builder.append("&o");
        }
        if (component.has("underline") && component.get("underline").getAsBoolean()) {
            builder.append("&u");
        }
        if (component.has("text")) {
            String text = component.get("text").getAsString();
            builder.append(text);
        }
        if (component.has("extra")) {
            JsonArray extraArray = component.getAsJsonArray("extra");
            for (JsonElement element : extraArray) {
                if (element.isJsonObject()) {
                    JsonObject nestedComponent = element.getAsJsonObject();
                    builder.append(formatChatComponent(nestedComponent));
                } else if (element.isJsonPrimitive()) {
                    builder.append(element.getAsString());
                }
            }
        }

        if (component.has("clickEvent")) {
            JsonObject clickEvent = component.getAsJsonObject("clickEvent");
            String action = clickEvent.get("action").getAsString();
            String value = clickEvent.get("value").getAsString();
        }
        if (component.has("hoverEvent")) {
            JsonObject hoverEvent = component.getAsJsonObject("hoverEvent");
            String action = hoverEvent.get("action").getAsString();
            JsonObject contents = hoverEvent.getAsJsonObject("contents");
        }
        return builder.toString();
    }
    public static String handleColorCodes(String message, boolean remove) {
        String regex = "[&§][0-9a-fA-Fk-oK-r]";
        if (remove) {
            return message.replaceAll(regex, "");
        } else {
            return message.replaceAll("§", "&");
        }
    }
    private static String getColorCode(String colorName) {
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
            default -> "";
        };
    }


    public static MutableText parseAndFormatText(String text) {
        MutableText result = Text.literal("");
        MutableText currentSegment = Text.literal("");
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '&' && i + 1 < chars.length) {
                if (!currentSegment.getSiblings().isEmpty()) {
                    result.append(currentSegment);
                    currentSegment = Text.literal("");
                }
                char colorCode = chars[i + 1];
                Formatting format = getFormattingByCode(colorCode);
                if (format != null) {
                    currentSegment = currentSegment.formatted(format);
                    i++; // Skip the color code
                }
            } else {
                currentSegment.append(Text.literal(String.valueOf(chars[i])));
            }
        }
        if (!currentSegment.getSiblings().isEmpty()) {
            result.append(currentSegment);
        }
        return result;
    }

    public static MutableText parseAndFormatColor(String text) {
        MutableText result = Text.literal("");
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '&' && i + 1 < chars.length) {
                char colorCode = chars[i + 1];
                Formatting format = Formatter.getFormattingByCode(colorCode);
                if (format != null) {
                    result.append(Text.literal("&" + colorCode).formatted(format));
                    i++;
                }
            } else {
                result.append(Text.literal(String.valueOf(chars[i])));
            }
        }
        return result;
    }

     public static Formatting getFormattingByCode(char code) {
         return switch (code) {
             case '0' -> Formatting.BLACK;
             case '1' -> Formatting.DARK_BLUE;
             case '2' -> Formatting.DARK_GREEN;
             case '3' -> Formatting.DARK_AQUA;
             case '4' -> Formatting.DARK_RED;
             case '5' -> Formatting.DARK_PURPLE;
             case '6' -> Formatting.GOLD;
             case '7' -> Formatting.GRAY;
             case '8' -> Formatting.DARK_GRAY;
             case '9' -> Formatting.BLUE;
             case 'a' -> Formatting.GREEN;
             case 'b' -> Formatting.AQUA;
             case 'c' -> Formatting.RED;
             case 'd' -> Formatting.LIGHT_PURPLE;
             case 'e' -> Formatting.YELLOW;
             case 'f' -> Formatting.WHITE;
             case 'k' -> Formatting.OBFUSCATED;
             case 'l' -> Formatting.BOLD;
             case 'm' -> Formatting.STRIKETHROUGH;
             case 'n' -> Formatting.UNDERLINE;
             case 'o' -> Formatting.ITALIC;
             case 'r' -> Formatting.RESET;
             default -> null;
         };
    }
}
