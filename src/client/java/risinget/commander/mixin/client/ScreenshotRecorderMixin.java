package risinget.commander.mixin.client;

import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import java.io.File;
import java.util.function.Consumer;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.util.Formatting;
import net.minecraft.text.ClickEvent;
import net.minecraft.client.texture.NativeImage;
import org.spongepowered.asm.mixin.Overwrite;
import risinget.commander.events.CloudinaryScreenshot;
import risinget.commander.utils.Formatter;
import risinget.commander.utils.Prefix;

@Mixin(ScreenshotRecorder.class)
public class ScreenshotRecorderMixin {

    /**
     * Sobrescribe el método saveScreenshotInner para personalizar el mensaje.
     *
     * @author Risinget
     * @reason Personalizar la lógica de funcionalidad de imagen.
     */
    @Overwrite
    private static void saveScreenshotInner(File gameDirectory, String fileName, Framebuffer framebuffer, Consumer<Text> messageReceiver) {
        NativeImage nativeImage = ScreenshotRecorder.takeScreenshot(framebuffer);
        File screenshotsDir = new File(gameDirectory, "screenshots");
        screenshotsDir.mkdir();
        File screenshotFile = (fileName == null) ? getScreenshotFilename(screenshotsDir) : new File(screenshotsDir, fileName);

        Util.getIoWorkerExecutor().execute(() -> {
            try {
                nativeImage.writeTo(screenshotFile);
                Text customMessage = Formatter.parseAndFormatText(Prefix.COMMANDER+" ")
                        .append(Text.literal(screenshotFile.getName()).formatted(Formatting.UNDERLINE).styled((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, screenshotFile.getAbsolutePath()))));
                messageReceiver.accept(customMessage);
                try {
                    CloudinaryScreenshot.uploadImage(String.valueOf(screenshotFile));
                    messageReceiver.accept(Text.literal("Enviado a la nube: "+screenshotFile.getName()));
                }catch (Exception e){
                    messageReceiver.accept(Text.literal("Error al subir la imagen: "+e.getMessage()));
                }
            } catch (Exception e) {
                messageReceiver.accept(Text.translatable("screenshot.failure", e.getMessage()));
            } finally {
                nativeImage.close();
            }
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
