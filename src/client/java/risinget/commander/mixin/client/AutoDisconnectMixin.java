package risinget.commander.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import risinget.commander.events.AutoDisconnect;

import java.io.File;


@Mixin(AutoDisconnect.class)
public class AutoDisconnectMixin {

	@Inject(at = @At("HEAD"), method = "disconnect(Lnet/minecraft/client/MinecraftClient;)V")
	private void beforeDisconnect(MinecraftClient client, CallbackInfo info) {
		// Take and save the screenshot
		File gameDirectory = client.runDirectory;
		ScreenshotRecorder.saveScreenshot(gameDirectory, null, client.getFramebuffer(), (text) -> {
			System.out.println("Locura hermano BV");
		});
	}

}
