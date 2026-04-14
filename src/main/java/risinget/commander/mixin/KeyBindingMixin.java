package risinget.commander.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.input.KeyInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import risinget.commander.keybinds.KeyHandler;

@Mixin(Keyboard.class)
public abstract class KeyBindingMixin {

    @Inject(method = "onKey", at = @At("HEAD"))
    private void commander$onKey(long window, int action, KeyInput input, CallbackInfo ci) {
        KeyHandler.onKey(input.key());
    }
}