package risinget.commander.mixin;

import dev.isxander.yacl3.gui.YACLScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import risinget.commander.config.ConfigCommander;

@Mixin(YACLScreen.class)
public abstract class CloseYACLScreen {
    @Inject(method = "close", at = @At("HEAD"))
    public void onClose(CallbackInfo info) {
        System.out.println("La pantalla se está cerrando.");
        ConfigCommander.HANDLER.save();
    }
}
