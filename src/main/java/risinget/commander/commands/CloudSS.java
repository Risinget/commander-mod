package risinget.commander.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import java.io.File;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.nio.file.Path;
import net.minecraft.client.MinecraftClient;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import com.cloudinary.*;

public class CloudSS {
    private static final MinecraftClient client = MinecraftClient.getInstance();


    private void registerScreenshotCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("screenshot")
                .executes(this::executeScreenshotCommand));
    }
    private int executeScreenshotCommand(CommandContext<FabricClientCommandSource> context) {
        return 1;
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
