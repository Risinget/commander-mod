package risinget.commander.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import net.minecraft.client.util.ScreenshotRecorder;
import java.io.File;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.io.IOException;
import java.nio.file.Path;
import net.minecraft.client.MinecraftClient;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import java.util.Map;

public class CloudSS {

    private void registerScreenshotCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("screenshot")
                .executes(this::executeScreenshotCommand));
    }
    private int executeScreenshotCommand(CommandContext<FabricClientCommandSource> context) {
        // Registra ambos eventos
//        InGameHud ingame = new InGameHud();
//        System.out.println(ingame.getTextRenderer());

        return 1; // Return 1 to indicate success
    }

    public CloudSS(){


        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            registerScreenshotCommand(dispatcher);
        });
        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            if (mc.options.screenshotKey.wasPressed() || mc.options.screenshotKey.isPressed())
            {Path screenshotDir = mc.runDirectory.toPath().resolve("screenshots");
               System.out.println(screenshotDir);

               System.out.println("xd");
            }
        });
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("prueba")
                .then(ClientCommandManager.argument("text", StringArgumentType.string())
                    .executes(context -> {
                        // Obtener el argumento
                        String texto = StringArgumentType.getString(context, "text");
                        context.getSource()
                                .sendFeedback(Text
                                        .literal("Soy prueba"+texto));
                        return 1;
                    })
                ));
        });
    }
}
