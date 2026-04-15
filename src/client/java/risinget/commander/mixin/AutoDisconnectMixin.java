package risinget.commander.mixin;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import risinget.commander.Commander;
import risinget.commander.config.ConfigCommander;
import risinget.commander.events.AutoDisconnect;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;

@Mixin(AutoDisconnect.class)
public class AutoDisconnectMixin {

	@Unique
	private static final Logger LOGGER = Commander.getLogger(AutoDisconnectMixin.class);

	@Inject(at = @At("HEAD"), method = "disconnect(Lnet/minecraft/client/Minecraft;F)V")
	private static void beforeDisconnect(Minecraft client, float health, CallbackInfo info) {
		File gameDirectory = client.gameDirectory;
		if(ConfigCommander.getEnableSsBeforeDisc()){
			Screenshot.grab(gameDirectory, client.getMainRenderTarget(), (text) -> LOGGER.info("SCREENSHOT TAKEN BEFORE DEATH"));
		}
	}

}
