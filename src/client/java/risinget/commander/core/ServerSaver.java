package risinget.commander.core;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.Util;
import org.slf4j.Logger;
import risinget.commander.Commander;
import risinget.commander.utils.FormatterUtils;
import risinget.commander.utils.ImageUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ServerSaver {

    private static final Minecraft client = Minecraft.getInstance();
    private static final Logger LOGGER = Commander.getLogger(ServerSaver.class);
    private static final Map<String, String> serverInfoMap = new HashMap<>();
    private static boolean isServerListLoaded = false;
    public static void saveIcon() {
        try {
            loadServerList();
        } catch (IOException e) {
            LOGGER.warn("Error loading server list");
        }
        if(client.getConnection() != null && client.getConnection().getServerData() != null ){
            saveIcon(serverInfoMap.get(client.getConnection().getServerData().ip), client.getConnection().getServerData().ip);
        }
    }

    public static void saveMotd() {
        if(client.getConnection() != null){
            if(client.getConnection().getServerData() != null){
               saveMotd(client.getConnection().getServerData());
            }
        }
    }

    public static void saveTabList(){
        if(client.getConnection() != null && client.getConnection().getServerData() != null){
            saveTabList(client.getConnection().getServerData());
        }
    }

    public static void saveTabList(ServerData srv) {
        if (srv.motd != null) {
            String srvName = srv.ip.replace(":", ".");
            Path filePath = client.gameDirectory.toPath().resolve("config/commander/servers/" + srvName + "/tablist.txt");

            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                String formattedHeader = FormatterUtils.getColoredText(TabListStorage.getHeader());
                String formattedFooter = FormatterUtils.getColoredText(TabListStorage.getFooter());

                writer.append(formattedHeader.replace("\"", "").replace("\\n", "\n"));
                writer.append(formattedFooter.replace("\"", "").replace("\\n", "\n"));
                writer.newLine();
            } catch (IOException e) {
                LOGGER.error("Error writing tab list for {}: {}", srvName, e.getMessage(), e);
            }
        }
    }


    public static void openFolderCurrentServer(){
        if(client.getConnection() !=null && client.getConnection().getServerData() != null)
        {
            ServerData srv = client.getConnection().getServerData();
            String srvName = srv.ip.replace(":",".");
            Path folderPath = client.gameDirectory.toPath().resolve("config/commander/servers/"+srvName+"/");
            try {
                Files.createDirectories(folderPath);
                Util.getPlatform().openPath(folderPath);
            } catch (IOException e) {
                LOGGER.error("Failed to open servers folder: ", e);
            }
        }
    }

    public static void copyMOTD(ServerSelectionList serverListWidget) {
        getSelectedServer(serverListWidget).ifPresent(srv -> {
            String motd = FormatterUtils.getColoredText(srv.motd);
            client.keyboardHandler.setClipboard(motd);
        });
    }


    public static void saveAllServerInfo(ServerList serverList) {
        try {
            loadServerList();
            if (serverList != null) {
                Util.ioPool().execute(() -> {
                    for(int i = 0; i < serverList.size(); i++){
                        ServerData srv = serverList.get(i);
                        saveIcon(serverInfoMap.get(srv.ip), srv.ip);
                        saveMotd(serverList.get(i));
                    }
                });
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load server list: ", e);
        }
    }


    public static void copyNumericIP(ServerSelectionList serverListWidget) {
        getSelectedServer(serverListWidget).ifPresent(srv -> {
            try {
                String numericIP = InetAddress.getByName(srv.ip).getHostAddress();
                client.keyboardHandler.setClipboard(numericIP);
            } catch (UnknownHostException e) {
                LOGGER.warn("Invalid host: {}", srv.ip);
            }
        });
    }


    public static void saveSelectedIcon(ServerSelectionList serverListWidget) {
        getSelectedServer(serverListWidget).ifPresent((srv)-> {
            try {
                loadServerList();
//                LOGGER.info("BUSCANDO: {} para address: {}", serverInfoMap.get(srv.address), srv.address);
//                LOGGER.info("=== CONTENIDO DEL MAP ===");
//                serverInfoMap.forEach((key, value) -> {
//                    LOGGER.info("KEY='{}' VALUE='{}'", key, value);
//                });
                saveIcon(serverInfoMap.get(srv.ip), srv.ip);
            } catch (IOException e) {
                LOGGER.warn("Invalid: {}", e.getMessage());
            }
        });
    }


    public static void openServerFolder() {
        Path folderPath = client.gameDirectory.toPath().resolve("config/commander/servers");
        try {
            Files.createDirectories(folderPath);
            Util.getPlatform().openPath(folderPath);
        } catch (IOException e) {
            LOGGER.error("Failed to open servers folder: ", e);
        }
    }


    public static Optional<ServerData> getSelectedServer(ServerSelectionList serverListWidget) {
        ServerSelectionList.Entry entry = serverListWidget.getSelected();
        return (entry instanceof ServerSelectionList.OnlineServerEntry)
                ? Optional.of(((ServerSelectionList.OnlineServerEntry) entry).getServerData())
                : Optional.empty();
    }

    public static void saveIcon(String base64icon, String address) {
        String srvName = address.replace(":", ".") ;
        Path filePath = client.gameDirectory.toPath().resolve("config/commander/servers/" + srvName + "/logo.png");
        try {
            Files.createDirectories(filePath.getParent());
            ImageUtils.saveBase64AsImage(base64icon, filePath.toString());
        } catch (IOException e) {
            LOGGER.error("Failed to save server icon: ", e);
        }
    }


    public static void saveMotd(ServerData srv) {
        if (srv.motd != null) {
            String srvName = srv.ip.replace(":", ".");
            Path filePath = client.gameDirectory.toPath().resolve("config/commander/servers/" + srvName + "/motd.txt");

            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                String formattedText = FormatterUtils.getColoredText(srv.motd);
                writer.write(formattedText);
                writer.newLine();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    public static void loadServerList() throws IOException {
        if (isServerListLoaded) {
            return;
        }
        CompoundTag nbtCompound = NbtIo.read(client.gameDirectory.toPath().resolve("servers.dat"));
        if(nbtCompound != null){
            Optional<ListTag> nbtList = nbtCompound.getList("servers");
            for (int i = 0; i < nbtList.get().size(); i++) {
                Optional<CompoundTag> serverData = nbtList.get().getCompound(i);
                String address = serverData.get().get("ip").toString().replace("\"", "");
                String icon = serverData.get().get("icon").toString().replace("\"", "");
                serverInfoMap.put(address,icon);
            }
            isServerListLoaded = true;
        }
    }
}
