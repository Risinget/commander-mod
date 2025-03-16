package risinget.commander.gui;

import dev.isxander.yacl3.api.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.Screen;
import risinget.commander.config.ConfigCommander;

public class ScreenGUI{

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static Screen yacl(MinecraftClient client){
        return YetAnotherConfigLib.createBuilder()
            .title(Text.of("Configuración de Commander"))
            .category(AutoDisconectConfig.category())
            .category(CopyCoordsConfig.category())
            .category(GeminiAIConfig.category())
            .category(CloudinaryConfig.category())
            .category(ServerConfig.category())
            .build()
            .generateScreen(client.currentScreen);
    }
    public static void openGui(){
        ConfigCommander.HANDLER.load();
        client.setScreen(ScreenGUI.yacl(client));
    }
}
