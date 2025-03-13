package risinget.commander.mixin;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.LanServerQueryManager;
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
import risinget.commander.events.HistoryChat;
import risinget.commander.utils.ImageUtils;
import risinget.commander.utils.TextColor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget.*;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {

    @Shadow protected MultiplayerServerListWidget serverListWidget;
    @Shadow protected abstract void removeEntry(boolean confirmedAction);

    @Shadow private ServerList serverList;
    @Shadow private LanServerQueryManager.LanServerEntryList lanServers;
    // Usamos un HashMap donde la clave es la IP (String) y el valor es otro String (por ejemplo, el nombre del servidor)
    private Map<String, String> serverInfoMap = new HashMap<>();
    private boolean isServerListLoaded = false;
    // Método para agregar datos al mapa
    public void addServer(String ip, String value) {
        serverInfoMap.put(ip, value); // Asocia la IP con un valor (como el nombre o algún dato relacionado)
    }

    // Método para obtener el valor usando la IP como clave
    public String getServerInfo(String ip) {
        return serverInfoMap.get(ip); // Devuelve el valor asociado a la IP
    }

    protected final MinecraftClient client = MinecraftClient.getInstance();
    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        // Coordenadas del botón "Añadir servidor"
        int addServerButtonX = this.width / 2 - 154; // Posición X del botón "Añadir servidor"
        int addServerButtonY = this.height - 54;     // Posición Y del botón "Añadir servidor"

        // Crear el botón personalizado y colocarlo a la derecha del botón "Añadir servidor"
        ButtonWidget btnCopyMotd = ButtonWidget.builder(Text.of("Copy MOTD"), button -> {
                    // Acción al hacer clic en el botón
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
                .dimensions(addServerButtonX + 325, addServerButtonY, 70, 15) // Coloca el botón a la derecha del botón "Añadir servidor"
                .build();

        ButtonWidget btnSaveSrvsInfo = ButtonWidget.builder(Text.of("SaveAllInfo"), button -> {
                    // Ahora la animación ha terminado, imprimir la lista de servidores
                    if (this.serverList != null) {
                        System.out.println(this.serverList.get(1).label);
                    }
                    for (int i = 0; i < this.serverList.size(); i++) {
                        ServerInfo srv = this.serverList.get(i);
                        Text srvMotd = srv.label;
                        System.out.println("ADDRESS: "+srv.address+"MOTD: "+srvMotd);
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
                        if (srv != null) {
                            // Buscar el servidor correspondiente en la lista
                            String base64icon = getServerInfo(srv.address);
                            String srvName = srv.address.replace(":",".");
                            Path filePath = client.runDirectory.toPath().resolve("config/commander/servers/"+srvName+"/"+srvName+".png");
                            // Crear directorios si no existen
                            try {
                                Files.createDirectories(filePath.getParent());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            ImageUtils.saveBase64AsImage(base64icon, String.valueOf(filePath));
                        }
                    }


                }).dimensions(addServerButtonX + 325+70, addServerButtonY + 15, 70, 15) // Coloca el botón a la derecha del botón "Añadir servidor"
                .build();




        ButtonWidget btnOpenFolder = ButtonWidget.builder(Text.of("Open Folder Servers"), button -> {
                    Path folderServers = MinecraftClient.getInstance().runDirectory.toPath().resolve("config/commander/servers");
                    try {
                        Files.createDirectory(folderServers);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Util.getOperatingSystem().open(folderServers);
                }).tooltip(Tooltip.of(Text.of("First Scroll up to down"))).dimensions(addServerButtonX + 325, addServerButtonY+30, 100, 15) // Ajustar la posición del botón
                .build();

        this.addDrawableChild(btnCopyMotd); // Añade el botón a la pantalla
        this.addDrawableChild(btnSaveFavicon);
        this.addDrawableChild(CopyAsIP);
        this.addDrawableChild(btnSaveSrvsInfo);
        this.addDrawableChild(btnOpenFolder);
    }


    // Método para cargar los servidores
    @Unique
    private void loadServerList() throws IOException {
        if (isServerListLoaded) {
            // Si ya hemos cargado los servidores, no lo hacemos de nuevo
            return;
        }
        // Cargar el archivo servers.dat
        NbtCompound nbtCompound = NbtIo.read(client.runDirectory.toPath().resolve("servers.dat"));
        NbtList nbtList = nbtCompound.getList("servers", NbtElement.COMPOUND_TYPE);
        // Convertir los datos NBT a ServerInfo y almacenarlos en la lista
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound serverData = nbtList.getCompound(i);
            String address = serverData.getString("ip");
            String icon = serverData.getString("icon");
            addServer(address,icon);
        }

        // Marcar como cargada la lista de servidores
        isServerListLoaded = true;
    }

}