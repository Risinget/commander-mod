package risinget.commander;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import net.minecraft.client.util.ScreenshotRecorder;
import java.io.File;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.io.IOException;
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

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import java.util.Map;

public class CloudSS {

    private void registerScreenshotCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("screenshot")
                .executes(this::executeScreenshotCommand));
    }
    private int executeScreenshotCommand(CommandContext<FabricClientCommandSource> context) {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            File gameDirectory = client.runDirectory;
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dgggg",
                    "api_key", "gaaaaa",
                    "api_secret", "Qgrx6CK9cSgHQ5t3Dw5qJZ8pcYM")
            );

            // Upload the image (for testing purposes)
            Map<String, Object> params1 = ObjectUtils.asMap(
                    "use_filename", true,
                    "unique_filename", false,
                    "overwrite", true
            );

            System.out.println(cloudinary.uploader().upload(
                    "https://cloudinary-devs.github.io/cld-docs-assets/assets/images/coffee_cup.jpg",
                    params1
            ));

            // Take and save the screenshot
            ScreenshotRecorder.saveScreenshot(gameDirectory, null, client.getFramebuffer(), (text) -> {
                client.execute(() -> {
                    client.player.sendMessage(Text.of("Captura de pantalla guardada: " + text.getString()), false);
                });
            });

        } catch (IOException e) {
            e.printStackTrace(); // Log the error
            return 0; // Return 0 to indicate failure
        }

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
