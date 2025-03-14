package risinget.commander.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.ServerList;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtList;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import risinget.commander.Commander;
import risinget.commander.events.HistoryChat;
import risinget.commander.utils.ImageUtils;
import risinget.commander.utils.TextColor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import org.slf4j.Logger;
@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {

    @Shadow protected MultiplayerServerListWidget serverListWidget;
    @Shadow private ServerList serverList;
    @Unique
    private Map<String, String> serverInfoMap = new HashMap<>();
    @Unique
    private boolean isServerListLoaded = false;
    @Unique
    public void addServer(String ip, String value) { serverInfoMap.put(ip, value); }
    @Unique
    public String getServerInfo(String ip) { return serverInfoMap.get(ip); }
    @Unique
    protected final MinecraftClient client = MinecraftClient.getInstance();
    protected MultiplayerScreenMixin(Text title) { super(title); }

    @Unique
    private static final Logger LOGGER = (Logger) Commander.getLogger(MultiplayerScreenMixin.class);

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        int addServerButtonX = this.width / 2 - 154;
        int addServerButtonY = this.height - 54;
        ButtonWidget btnCopyMotd = ButtonWidget.builder(Text.of("Copy MOTD"), button -> {
                    MultiplayerServerListWidget.Entry entry = this.serverListWidget.getSelectedOrNull();
                    if (entry instanceof MultiplayerServerListWidget.ServerEntry) {
                        ServerInfo srv = ((MultiplayerServerListWidget.ServerEntry)entry).getServer();
                        if (srv.name != null) {
                            Text serverMOTD = srv.label;
                            MinecraftClient client = MinecraftClient.getInstance();
                            JsonObject jsonObject;
                            try {
                                jsonObject = JsonParser.parseString(TextColor.toJson(serverMOTD)).getAsJsonObject();
                                String formattedMessage = HistoryChat.formatJsonMessage(jsonObject);
                                System.out.println(formattedMessage);
                                client.keyboard.setClipboard(HistoryChat.handleColorCodes(formattedMessage, false));
                            }catch(IllegalStateException e){
                                client.keyboard.setClipboard(TextColor.toJson(serverMOTD));
                            }
                        }
                    }
                })
                .dimensions(addServerButtonX + 325, addServerButtonY, 70, 15)
                .build();

        ButtonWidget btnSaveSrvsInfo = ButtonWidget.builder(Text.of("SaveAllInfo"), button -> {
                    try {
                        loadServerList();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (this.serverList != null) {
                        for (int i = 0; i < (this.serverList != null ? this.serverList.size() : 0); i++) {
                            ServerInfo srv = this.serverList.get(i);
                            Util.getIoWorkerExecutor().execute(()->{
                                this.saveIcon(srv);
                                this.saveInfo(srv);
                            });
                            LOGGER.info("ADDRESS: "+srv.address+"MOTD: "+srv.label);
                        }
                    }
                }).tooltip(Tooltip.of(Text.of("First Scroll up to down"))).dimensions(addServerButtonX + 325 + 70, addServerButtonY, 70, 15) // Ajustar la posición del botón
                .build();

        ButtonWidget CopyAsIP = ButtonWidget.builder(Text.of("Copy as IP"), button -> {

                    MultiplayerServerListWidget.Entry entry = this.serverListWidget.getSelectedOrNull();
                    if (entry instanceof MultiplayerServerListWidget.ServerEntry) {
                        ServerInfo srv = ((MultiplayerServerListWidget.ServerEntry) entry).getServer();
                        if (srv != null) {
                            try {
                                InetAddress inetAddress = InetAddress.getByName(srv.address);
                                String numericIP = inetAddress.getHostAddress(); // IP numérica
                                System.out.println("ADDRESS: "+srv.address+"NUMERIC IP: "+numericIP);
                                client.keyboard.setClipboard(numericIP);
                            } catch (UnknownHostException e) {
                                System.out.println("HOST INVALID: "+srv.address);
                            }
                        }
                    }
                }).dimensions(addServerButtonX + 325, addServerButtonY + 15, 70, 15) // Coloca el botón a la derecha del botón "Añadir servidor"
                .build();

        ButtonWidget btnSaveFavicon = ButtonWidget.builder(Text.of("Save Icon"), button -> {
                    try {
                        loadServerList();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    MultiplayerServerListWidget.Entry entry = this.serverListWidget.getSelectedOrNull();
                    if (entry instanceof MultiplayerServerListWidget.ServerEntry) {
                        ServerInfo srv = ((MultiplayerServerListWidget.ServerEntry) entry).getServer();
                        this.saveIcon(srv);
                    }
                }).dimensions(addServerButtonX + 325+70, addServerButtonY + 15, 70, 15) // Coloca el botón a la derecha del botón "Añadir servidor"
                .build();

        ButtonWidget btnOpenFolder = ButtonWidget.builder(Text.of("Open Folder Servers"), button -> {
                    Path folderServers = MinecraftClient.getInstance().runDirectory.toPath().resolve("config/commander/servers");
                    try {
                        if (!Files.exists(folderServers)) {
                            Files.createDirectory(folderServers);
                        }
                        Util.getOperatingSystem().open(folderServers);
                    } catch (IOException e) {
                        LOGGER.error("Failed to create or open the servers folder: ", e);
                    }
                }).tooltip(Tooltip.of(Text.of("First Scroll up to down"))).dimensions(addServerButtonX + 325, addServerButtonY + 30, 100, 15) // Ajustar la posición del botón
                .build();
        this.addDrawableChild(btnCopyMotd);
        this.addDrawableChild(btnSaveSrvsInfo);
        this.addDrawableChild(CopyAsIP);
        this.addDrawableChild(btnSaveFavicon);
        this.addDrawableChild(btnOpenFolder);
    }

    @Unique
    public void saveIcon(ServerInfo srv){
        if (srv != null) {
            String base64icon = getServerInfo(srv.address);
            String srvName = srv.address.replace(":",".");
            Path filePath = client.runDirectory.toPath().resolve("config/commander/servers/"+srvName+"/"+srvName+".png");
            try {
                Files.createDirectories(filePath.getParent());
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
            ImageUtils.saveBase64AsImage(base64icon, String.valueOf(filePath));
        }
    }

    @Unique
    public synchronized void saveInfo(ServerInfo srv){
        if (srv.label != null) {
            String srvName = srv.address.replace(":",".");
            Path filePath = client.runDirectory.toPath().resolve("config/commander/servers/"+srvName+"/"+srvName+".txt");
            try {
                Files.createDirectories(filePath.getParent());
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath.toUri()), true))) {
                try {
                    writer.write(HistoryChat.handleColorCodes(HistoryChat.formatJsonMessage(JsonParser.parseString(TextColor.toJson(srv.label)).getAsJsonObject()),false));
                    writer.newLine();
                }catch(JsonSyntaxException | IllegalStateException e) {
                    writer.write(HistoryChat.handleColorCodes(TextColor.toJson(srv.label), false));
                    writer.newLine();
                }
                Text serverMOTD = srv.label;
                JsonObject jsonObject;
                try {
                    jsonObject = JsonParser.parseString(TextColor.toJson(serverMOTD)).getAsJsonObject();
                    String formattedMessage = HistoryChat.formatJsonMessage(jsonObject);
                    writer.write(HistoryChat.handleColorCodes(formattedMessage, false));
                    writer.newLine();
                }catch(IllegalStateException e){
                    writer.write(TextColor.toJson(srv.label));
                    writer.newLine();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    @Unique
    private void loadServerList() throws IOException {
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
                addServer(address,icon);
            }
            isServerListLoaded = true;
        }
    }
}