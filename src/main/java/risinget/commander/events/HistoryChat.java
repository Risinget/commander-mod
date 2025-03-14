package risinget.commander.events;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import com.google.gson.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import net.minecraft.util.Util;
import org.slf4j.Logger;
import risinget.commander.Commander;
import risinget.commander.utils.TextColor;

public class HistoryChat {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static ServerInfo srv = null;
    private static File logFile = null;
    private static File logFileColored = null;
    private static final Logger LOGGER = Commander.getLogger(HistoryChat.class);
    private static final String CHAT_HISTORY_DIR = "config/commander/servers/";
    private static final String SINGLEPLAYER_DIR = "config/commander/servers/singleplayer/";
    private static final File FILE_LOG_ERROR = new File("config/commander/history_chat_debug.txt");
    public HistoryChat() {

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.isInSingleplayer()) {
                logFile = new File(SINGLEPLAYER_DIR + "commander_chat_history.txt");
                logFileColored = new File(SINGLEPLAYER_DIR + "commander_chat_colored.txt");
            } else {
                srv = client.getCurrentServerEntry();
                assert srv != null;

                logFile = new File(CHAT_HISTORY_DIR + srv.address.replace(":", ".") + "/commander_chat_history.txt");
                logFileColored = new File(CHAT_HISTORY_DIR + srv.address.replace(":", ".") + "/commander_chat_colored.txt");
            }
            createDirectories(logFile);
            createDirectories(logFileColored);
            createDirectories(FILE_LOG_ERROR);
        });
        ClientReceiveMessageEvents.ALLOW_CHAT.register((message, overlay, overlay2, overlay3, overlay4) -> message != null && onMessage(message));
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> message != null && onMessage(message));
    }

    private static void createDirectories(File file) {
        try {
            Files.createDirectories(file.toPath().getParent());
        } catch (IOException e) {
            LOGGER.error("Error al crear directorios: ", e);
        }
    }

    private boolean onMessage(Text message) {
        Util.getIoWorkerExecutor().execute(() -> processMessage(message));
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
//                    LOGGER.info(json);
                    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                    String formattedMessage = formatJsonMessage(jsonObject);
//                    LOGGER.info(formattedMessage);
                    saveMessage(handleColorCodes(formattedMessage, false), logFileColored);
                } catch (JsonSyntaxException | IllegalStateException e) {
//                    LOGGER.error(e.getMessage());
                    saveMessage(e.getMessage(),FILE_LOG_ERROR);
                }
            } else {
//                LOGGER.warn("El JSON está vacío o es nulo.");
                saveMessage("El JSON está vacío o es nulo.",FILE_LOG_ERROR);
            }
        } else {
//            LOGGER.warn("NetworkHandler is not initialized yet.");
            saveMessage("NetworkHandler is not initialized yet.",FILE_LOG_ERROR);
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

}
