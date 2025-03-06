package risinget.commander;

import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.eliux.mega.error.MegaUnexpectedFailureException;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import net.minecraft.client.util.ScreenshotRecorder;
import java.io.File;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import java.nio.file.Path;
import net.minecraft.client.MinecraftClient;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import io.github.eliux.mega.MegaSession;
import io.github.eliux.mega.Mega;
import io.github.eliux.mega.auth.MegaAuthCredentials;


public class CloudSS {

    private void registerScreenshotCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("screenshot")
                .executes(this::executeScreenshotCommand));
    }

    private int executeScreenshotCommand(CommandContext<FabricClientCommandSource> context) {
        MinecraftClient client = MinecraftClient.getInstance();
        File gameDirectory = client.runDirectory;

        MegaAuthCredentials authMega = new MegaAuthCredentials("x","x");
        try {
            MegaSession sessionMega = Mega.login(authMega);
            sessionMega.uploadFile("screenshots/2025-03-05_18.25.29.png", "megacmd4j/")
                    .createRemotePathIfNotPresent()
                    .run();
        } catch (MegaUnexpectedFailureException e) {
            e.printStackTrace(); // Esto ayudará a entender mejor el error
        }


        // Tomar la captura de pantalla
        ScreenshotRecorder.saveScreenshot(gameDirectory, null, client.getFramebuffer(), (text) -> {
            client.execute(() -> {
                client.player.sendMessage(Text.of("Captura de pantalla guardada: " + text.getString()), false);
            });
        });

        return 1; // Retorna 1 para indicar que el comando se ejecutó correctamente
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
