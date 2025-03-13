package risinget.commander.events;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import com.google.gson.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;

import net.minecraft.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import risinget.commander.Commander;
import risinget.commander.utils.TextColor;


public class HistoryChat {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static ServerInfo srv = null;
    private static File logFile = null;
    private static File logFileColored = null;

    private static final Logger LOGGER = Commander.getLogger(HistoryChat.class);

    public HistoryChat() {

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {

            if (client.isInSingleplayer()) {
                System.out.println("This is a single-player world. No MOTD to capture.");

                logFile = new File("config/commander/servers/singleplayer/commander_chat_history.txt");
                logFileColored = new File("config/commander/servers/singleplayer/commander_chat_colored.txt");
                try {
                    Files.createDirectories(logFile.toPath().getParent());
                    Files.createDirectories(logFileColored.toPath().getParent());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }

            srv = client.getCurrentServerEntry();
            assert srv != null;

            logFile = new File("config/commander/servers/"+srv.address.replace(":",".")+"/commander_chat_history.txt");
            logFileColored = new File("config/commander/servers/"+srv.address.replace(":",".")+"/commander_chat_colored.txt");

            try {
                Files.createDirectories(logFile.toPath().getParent());
                Files.createDirectories(logFileColored.toPath().getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        ClientReceiveMessageEvents.ALLOW_CHAT.register((message, overlay, overlay2, overlay3, overlay4) ->
                message != null ? onChatMessage(message, overlay, overlay2, overlay3, overlay4) : false);

        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) ->
                message != null ? onGameMessage(message, overlay) : false);
    }

    private boolean onGameMessage(Text message, boolean overlay) {
        Util.getIoWorkerExecutor().execute(() -> {
            processMessage(message);
        });
        return true;
    }

    private boolean onChatMessage(Text message, SignedMessage overlay, GameProfile overlay2, MessageType.Parameters overlay3, Instant overlay4) {
        Util.getIoWorkerExecutor().execute(() -> {
            processMessage(message);
        });
        return true;
    }
    private static void processMessage(Text message) {
        if (client.getNetworkHandler() != null) {

            String json = TextColor.toJson(message);
            // Guardar el mensaje en texto plano
            String msgPlain = message.getString();
            saveMessage(handleColorCodes(msgPlain, true), logFile);
            // Verificar si el JSON está vacío
            if (json != null && !json.isEmpty()) {
                try {
                    // Parsear el JSON solo si no está vacío
                    System.out.println(json);
                    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                    String formattedMessage = formatJsonMessage(jsonObject);
                    System.out.println(formattedMessage);
                    saveMessage(handleColorCodes(formattedMessage, false), logFileColored);
                } catch (JsonSyntaxException | IllegalStateException e) {
                    // Manejar errores de parsing
                    System.err.println("Error al parsear el JSON: " + e.getMessage());
                }
            } else {
                System.out.println("El JSON está vacío o es nulo.");
            }


        } else {
            System.out.println("NetworkHandler is not initialized yet.");
        }
    }

    private static synchronized void saveMessage(String msg, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + timestamp + "] " + msg);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String formatJsonMessage(JsonObject jsonObject) {
        StringBuilder builder = new StringBuilder();
        // Procesar el campo "translate" si existe
        if (jsonObject.has("translate")) {
            String translate = jsonObject.get("translate").getAsString();
            if (jsonObject.has("with")) {
                JsonArray withArray = jsonObject.getAsJsonArray("with");
                for (JsonElement element : withArray) {
                    if (element.isJsonObject()) {
                        JsonObject withObject = element.getAsJsonObject();
                        builder.append(formatJsonMessage(withObject)); // Procesar recursivamente
                    } else if (element.isJsonPrimitive()) {
                        builder.append(element.getAsString()); // Añadir texto plano
                    }
                }
            }
        }
        // Procesar el campo "color" si existe
        if (jsonObject.has("color")) {
            String color = jsonObject.get("color").getAsString();
            builder.append(getColorCode(color)); // Aplicar el código de color
        }

        // Procesar el campo "text" si existe
        if (jsonObject.has("text")) {
            String text = jsonObject.get("text").getAsString();
            builder.append(text);
        }

        // Procesar el campo "extra" si existe
        if (jsonObject.has("extra")) {
            JsonArray extraArray = jsonObject.getAsJsonArray("extra");
            for (JsonElement element : extraArray) {
                if (element.isJsonObject()) {
                    JsonObject component = element.getAsJsonObject();
                    builder.append(formatChatComponent(component));
                } else if (element.isJsonPrimitive()) {
                    builder.append(element.getAsString()); // Añadir texto plano
                }
            }
        }

        return builder.toString();
    }

    private static String formatChatComponent(JsonObject component) {
        StringBuilder builder = new StringBuilder();

        // Procesar el campo "color" si existe
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
        // Procesar el campo "bold" si existe
        if (component.has("bold") && component.get("bold").getAsBoolean()) {
            builder.append("&l");
        }
        // Procesar el campo "italic" si existe
        if (component.has("italic") && component.get("italic").getAsBoolean()) {
            builder.append("&o");
        }
        if (component.has("underline") && component.get("underline").getAsBoolean()) {
            builder.append("&u");
        }

        // Procesar el campo "text" si existe
        if (component.has("text")) {
            String text = component.get("text").getAsString();
            builder.append(text);
        }

        // Procesar el campo "extra" si existe (recursivamente)
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

        // Procesar el campo "clickEvent" si existe
        if (component.has("clickEvent")) {
            JsonObject clickEvent = component.getAsJsonObject("clickEvent");
            String action = clickEvent.get("action").getAsString();
            String value = clickEvent.get("value").getAsString();
            // Aquí puedes manejar el evento de clic si es necesario
        }

        // Procesar el campo "hoverEvent" si existe
        if (component.has("hoverEvent")) {
            JsonObject hoverEvent = component.getAsJsonObject("hoverEvent");
            String action = hoverEvent.get("action").getAsString();
            JsonObject contents = hoverEvent.getAsJsonObject("contents");
            // Aquí puedes manejar el evento de hover si es necesario
        }

        return builder.toString();
    }
    // Función que maneja el reemplazo o eliminación de los códigos de color (& o §)
    public static String handleColorCodes(String message, boolean remove) {
        // Expresión regular para encontrar los códigos de color (& o § seguidos de un carácter)
        String regex = "[&§][0-9a-fA-Fk-oK-r]";

        // Si el booleano es true, elimina los códigos de color
        if (remove) {
            return message.replaceAll(regex, "");
        } else {
            // Si el booleano es false, reemplaza los códigos de color con el prefijo adecuado (&)
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

}
