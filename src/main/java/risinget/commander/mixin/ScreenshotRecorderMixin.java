package risinget.commander.mixin;

import com.mojang.logging.LogUtils;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import java.io.File;
import java.lang.annotation.Native;
import java.util.function.Consumer;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.util.Formatting;
import net.minecraft.text.ClickEvent;
import net.minecraft.client.texture.NativeImage;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.util.IConsumer;
import risinget.commander.config.ConfigCommander;
import risinget.commander.events.CloudinaryScreenshot;
import risinget.commander.utils.FormatterUtils;
import risinget.commander.utils.Prefix;

import static net.minecraft.client.util.ScreenshotRecorder.takeScreenshot;

@Mixin(ScreenshotRecorder.class)
public class ScreenshotRecorderMixin {

    /**
     * Sobrescribe el método saveScreenshotInner para personalizar el mensaje.
     *
     * @author Risinget
     * @reason Personalizar la lógica de funcionalidad de imagen.
     */

    private static final Logger LOGGER = LogUtils.getLogger();

    @Overwrite

    public static void saveScreenshot(File gameDirectory, @Nullable String fileName, Framebuffer framebuffer, int downscaleFactor, Consumer<Text> messageReceiver) {



        takeScreenshot(framebuffer, 1, (image) -> {
            File file2 = new File(gameDirectory, "screenshots");
            file2.mkdir();
            File file3;
            if (fileName == null) {
                file3 = getScreenshotFilename(file2);
            } else {
                file3 = new File(file2, fileName);
            }

            Util.getIoWorkerExecutor().execute(() -> {
                try {
                    NativeImage exception = image;

                    try {
                        image.writeTo(file3);
                        Text text = FormatterUtils.parseAndFormatText(Prefix.COMMANDER+" ").append(Text.literal(file3.getName()).formatted(Formatting.UNDERLINE).styled((style) -> style.withClickEvent(new ClickEvent.CopyToClipboard(file3.getAbsolutePath()))));
                        messageReceiver.accept(Text.translatable("screenshot.success", new Object[]{text}));
                        if(ConfigCommander.getEnableUploadToCloud()){
                            try {
                                CloudinaryScreenshot.uploadImage(String.valueOf(file3));
                                messageReceiver.accept(Text.literal("Enviado a la nube: "+file3.getName()));
                            }catch (Exception e){
                                messageReceiver.accept(Text.literal("Error al subir la imagen: "+e.getMessage()));
                            }
                        }
                    } catch (Throwable var7) {
                        if (image != null) {
                            try {
                                exception.close();
                            } catch (Throwable var6) {
                                var7.addSuppressed(var6);
                            }
                        }

                        throw var7;
                    }

                    if (image != null) {
                        image.close();
                    }
                } catch (Exception exception) {
                    LOGGER.warn("Couldn't save screenshot", exception);
                    messageReceiver.accept(Text.translatable("screenshot.failure", new Object[]{exception.getMessage()}));
                }

            });
        });
    }

    @Unique
    private static File getScreenshotFilename(File directory) {
        String string = Util.getFormattedCurrentTime();
        int i = 1;

        while (true) {
            File file = new File(directory, string + (i == 1 ? "" : "_" + i) + ".png");
            if (!file.exists()) {
                return file;
            }
            ++i;
        }
    }
}











//
//@Mixin(ScreenshotRecorder.class)
//public class ScreenshotRecorderMixin {
//
//
//    @Inject(
//            method = "saveScreenshotInner",
//            at = @At("RETURN"),
//            cancellable = true
//    )
//    private static void onScreenshotSaved(File gameDirectory, @Nullable String fileName, Framebuffer framebuffer, Consumer<Text> messageReceiver, CallbackInfo ci) {
//        File screenshotFolder = new File(gameDirectory, "screenshots");
//        System.out.println(screenshotFolder);
//        File screenshotFile = (fileName == null) ? getScreenshotFilename(screenshotFolder) : new File(screenshotFolder, fileName);
//
//        // Formatear el mensaje que se enviará al chat
//        Text mensaje = Text.literal("Recibido: " + screenshotFile.getName());
//
//        // Enviar el mensaje al chat
//
//        // Retrasar la impresión en consola al siguiente tick
//        Util.getMainWorkerExecutor().execute(() -> {
//            messageReceiver.accept(mensaje);
//            System.out.println("------------------------------------");
//            System.out.println("Captura de pantalla guardada en: " + screenshotFile.getAbsolutePath());
//            System.out.println("Directorio del juego: " + gameDirectory.getAbsolutePath());
//            System.out.println("Nombre de archivo: " + (fileName == null ? "[Generado automáticamente]" : fileName));
//            System.out.println("Dimensiones del Framebuffer: " + framebuffer.textureWidth + "x" + framebuffer.textureHeight);
//            System.out.println("CallbackInfo: " + ci);
//            System.out.println("Mensaje de recepción: " + mensaje.getString());
//            System.out.println("------------------------------------");
//            ci.cancel();
//        });
//    }
//
//    @Unique
//    private static File getScreenshotFilename(File directory) {
//        String string = Util.getFormattedCurrentTime();
//        int i = 1;
//
//        while (true) {
//            File file = new File(directory, string + (i == 1 ? "" : "_" + i) + ".png");
//            if (!file.exists()) {
//                return file;
//            }
//            ++i;
//        }
//    }
//}
