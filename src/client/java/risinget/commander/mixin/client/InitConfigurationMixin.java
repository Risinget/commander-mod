package risinget.commander.mixin.client;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import risinget.commander.config.ConfigCommander;

@Mixin(MinecraftClient.class)
public class InitConfigurationMixin {

    @Inject(at = @At("HEAD"), method = "run")
    private void init(CallbackInfo info) {
        ConfigCommander.HANDLER.load();
    }
}
