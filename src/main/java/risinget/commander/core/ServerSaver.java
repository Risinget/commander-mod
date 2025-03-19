package risinget.commander.core;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtList;
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

    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Logger LOGGER = Commander.getLogger(ServerSaver.class);
    private static final Map<String, String> serverInfoMap = new HashMap<>();
    private static boolean isServerListLoaded = false;
    public static void saveIcon() {
        try {
            loadServerList();
        } catch (IOException e) {
            LOGGER.warn("Error loading server list");
        }
        if(client.getNetworkHandler() != null && client.getNetworkHandler().getServerInfo() != null ){
            saveIcon(serverInfoMap.get(client.getNetworkHandler().getServerInfo().address), client.getNetworkHandler().getServerInfo().address);
        }
    }

    public static void saveMotd() {
        if(client.getNetworkHandler() != null){
            if(client.getNetworkHandler().getServerInfo() != null){
               saveMotd(client.getNetworkHandler().getServerInfo());
            }
        }
    }

    public static void saveTabList(){
        if(client.getNetworkHandler() != null && client.getNetworkHandler().getServerInfo() != null){
            saveTabList(client.getNetworkHandler().getServerInfo());
        }
    }

    public static void saveTabList(ServerInfo srv) {
        if (srv.label != null) {
            String srvName = srv.address.replace(":", ".");
            Path filePath = client.runDirectory.toPath().resolve("config/commander/servers/" + srvName + "/tablist.txt");

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
        if(client.getNetworkHandler() !=null && client.getNetworkHandler().getServerInfo() != null)
        {
            ServerInfo srv = client.getNetworkHandler().getServerInfo();
            String srvName = srv.address.replace(":",".");
            Path folderPath = client.runDirectory.toPath().resolve("config/commander/servers/"+srvName+"/");
            try {
                Files.createDirectories(folderPath);
                Util.getOperatingSystem().open(folderPath);
            } catch (IOException e) {
                LOGGER.error("Failed to open servers folder: ", e);
            }
        }
    }

    public static void copyMOTD(MultiplayerServerListWidget serverListWidget) {
        getSelectedServer(serverListWidget).ifPresent(srv -> {
            String motd = FormatterUtils.getColoredText(srv.label);
            client.keyboard.setClipboard(motd);
        });
    }


    public static void saveAllServerInfo(ServerList serverList) {
        try {
            loadServerList();
            if (serverList != null) {
                Util.getIoWorkerExecutor().execute(() -> {
                    for(int i = 0; i < serverList.size(); i++){
                        ServerInfo srv = serverList.get(i);
                        LOGGER.info(serverInfoMap.get(srv.address));
                        saveIcon(serverInfoMap.get(srv.address), srv.address);
                        saveMotd(serverList.get(i));
                    }
                });
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load server list: ", e);
        }
    }


    public static void copyNumericIP(MultiplayerServerListWidget serverListWidget) {
        getSelectedServer(serverListWidget).ifPresent(srv -> {
            try {
                String numericIP = InetAddress.getByName(srv.address).getHostAddress();
                client.keyboard.setClipboard(numericIP);
            } catch (UnknownHostException e) {
                LOGGER.warn("Invalid host: {}", srv.address);
            }
        });
    }


    public static void saveSelectedIcon(MultiplayerServerListWidget serverListWidget) {
        getSelectedServer(serverListWidget).ifPresent((srv)-> {
            try {
                loadServerList();
            } catch (IOException e) {
                LOGGER.warn("Invalid: {}", e.getMessage());
            }
            saveIcon(serverInfoMap.get(srv.address), srv.address);
        });
    }


    public static void openServerFolder() {
        Path folderPath = client.runDirectory.toPath().resolve("config/commander/servers");
        try {
            Files.createDirectories(folderPath);
            Util.getOperatingSystem().open(folderPath);
        } catch (IOException e) {
            LOGGER.error("Failed to open servers folder: ", e);
        }
    }


    public static Optional<ServerInfo> getSelectedServer(MultiplayerServerListWidget serverListWidget) {
        MultiplayerServerListWidget.Entry entry = serverListWidget.getSelectedOrNull();
        return (entry instanceof MultiplayerServerListWidget.ServerEntry)
                ? Optional.of(((MultiplayerServerListWidget.ServerEntry) entry).getServer())
                : Optional.empty();
    }

    public static void saveIcon(String base64icon, String address) {
        String srvName = address.replace(":", ".") ;
        Path filePath = client.runDirectory.toPath().resolve("config/commander/servers/" + srvName + "/logo.png");
        try {
            Files.createDirectories(filePath.getParent());
            ImageUtils.saveBase64AsImage(base64icon, filePath.toString());
        } catch (IOException e) {
            LOGGER.error("Failed to save server icon: ", e);
        }
    }


    public static void saveMotd(ServerInfo srv) {
        if (srv.label != null) {
            String srvName = srv.address.replace(":", ".");
            Path filePath = client.runDirectory.toPath().resolve("config/commander/servers/" + srvName + "/motd.txt");

            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                String formattedText = FormatterUtils.getColoredText(srv.label);
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
        NbtCompound nbtCompound = NbtIo.read(client.runDirectory.toPath().resolve("servers.dat"));
        if(nbtCompound != null){
            NbtList nbtList = nbtCompound.getList("servers", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < nbtList.size(); i++) {
                NbtCompound serverData = nbtList.getCompound(i);
                String address = serverData.getString("ip");
                String icon = serverData.getString("icon");
                serverInfoMap.put(address,icon);
            }
            isServerListLoaded = true;
        }
    }
}
