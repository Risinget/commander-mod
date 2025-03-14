package risinget.commander.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.ScreenshotRecorder;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import risinget.commander.Commander;
import risinget.commander.events.AutoDisconnect;
import java.io.File;

@Mixin(AutoDisconnect.class)
public class AutoDisconnectMixin {

	@Unique
	private static final Logger LOGGER = Commander.getLogger(AutoDisconnectMixin.class);

	@Inject(at = @At("HEAD"), method = "disconnect(Lnet/minecraft/client/MinecraftClient;F)V")
	private static void beforeDisconnect(MinecraftClient client, float health, CallbackInfo info) {
		File gameDirectory = client.runDirectory;
		ScreenshotRecorder.saveScreenshot(gameDirectory, null, client.getFramebuffer(), (text) -> LOGGER.info("SCREENSHOT TAKEN BEFORE DEATH"));
	}

}
