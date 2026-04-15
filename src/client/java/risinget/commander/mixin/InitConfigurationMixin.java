package risinget.commander.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import risinget.commander.config.ConfigCommander;

@Mixin(Minecraft.class)
public class InitConfigurationMixin {

    @Inject(at = @At("HEAD"), method = "run")
    private void init(CallbackInfo info) {
        ConfigCommander.HANDLER.load();
    }
}
