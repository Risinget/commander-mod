package risinget.commander.gui;

import dev.isxander.yacl3.api.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import risinget.commander.config.ConfigCommander;

public class ScreenGUI{

    private static final Minecraft client = Minecraft.getInstance();

    public static Screen yacl(Minecraft client){
        return YetAnotherConfigLib.createBuilder()
            .title(Component.nullToEmpty("Configuración de Commander"))
//            .screenInit(yacl -> ConfigCommander.HANDLER.load())
            .category(AutoDisconectConfig.category())
            .category(CopyCoordsConfig.category())
            .category(GeminiAIConfig.category())
            .category(CloudinaryConfig.category())
            .category(ServerConfig.category())
            .category(OthersConfig.category())
            .save(ConfigCommander.HANDLER::save)
            .build()
            .generateScreen(client.screen);
    }
    public static void openGui(){
        client.setScreen(ScreenGUI.yacl(client));
    }
}
