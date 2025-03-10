package risinget.commander.gui;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import risinget.commander.commands.GeminiAICommand;
import risinget.commander.config.ConfigCommander;
import risinget.commander.enums.GeminiModel;
import net.minecraft.client.gui.screen.Screen;
public class ScreenGUI {

    public static Screen yacl(MinecraftClient client){
        return YetAnotherConfigLib.createBuilder()
                .title(Text.of("Configuración de Commander"))
                .category(AutoDisconectConfig.category())
                .category(CopyCoordsConfig.category())
                .category(GeminiAIConfig.category())
                .category(CloudinaryConfig.category())

                .build()
                .generateScreen(client.currentScreen);
    }
}
