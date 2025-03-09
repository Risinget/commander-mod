package risinget.commander.mixin.client;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import risinget.commander.events.AutoDisconnect;

@Mixin(AutoDisconnect.class)
public class ExampleClientMixin {
	@Inject(at = @At("HEAD"), method = "disconnect()V")
	private void init(CallbackInfo info) {
		// This code is injected into the start of MinecraftClient.run()V
	}
}