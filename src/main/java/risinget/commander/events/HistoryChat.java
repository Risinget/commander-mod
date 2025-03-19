package risinget.commander.events;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import com.google.gson.*;
import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import net.minecraft.util.Util;
import org.slf4j.Logger;
import risinget.commander.Commander;
import risinget.commander.config.ConfigCommander;
import risinget.commander.utils.FileUtils;
import risinget.commander.utils.FormatterUtils;


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
        Util.getIoWorkerExecutor().execute(() -> {
            if(ConfigCommander.getEnableHistoryChat()){
                processMessage(message);
            }
        });
        return true;
    }
    private static void processMessage(Text message) {
        if (client.getNetworkHandler() != null) {
            String json = FormatterUtils.toJson(message);
            // Guardar el mensaje en texto plano
            String msgPlain = message.getString();
            FileUtils.saveMessage(FormatterUtils.handleColorCodes(msgPlain, true), logFile);
            // Verificar si el JSON está vacío
            if (json != null && !json.isEmpty()) {
                try {
                    // this logger is for debugging text jsons
                    LOGGER.info(json);
                    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                    String formattedMessage = FormatterUtils.formatJsonMessage(jsonObject);
//                    LOGGER.info(formattedMessage);
                    FileUtils.saveMessage(FormatterUtils.handleColorCodes(formattedMessage, false), logFileColored);
                } catch (JsonSyntaxException | IllegalStateException e) {
//                    LOGGER.error(e.getMessage());
                    FileUtils.saveMessage(e.getMessage()+": "+json,FILE_LOG_ERROR);
                }
            } else {
//                LOGGER.warn("El JSON está vacío o es nulo.");
                FileUtils.saveMessage("El JSON está vacío o es nulo.",FILE_LOG_ERROR);
            }
        } else {
//            LOGGER.warn("NetworkHandler is not initialized yet.");
            FileUtils.saveMessage("NetworkHandler is not initialized yet.",FILE_LOG_ERROR);
        }
    }

}
