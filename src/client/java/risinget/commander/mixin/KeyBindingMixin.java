package risinget.commander.mixin;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import risinget.commander.keybinds.KeyHandler;

@Mixin(KeyboardHandler.class)
public abstract class KeyBindingMixin {

    @Inject(method = "keyPress", at = @At("HEAD"))
    private void commander$onKey(long window, int action, KeyEvent input, CallbackInfo ci) {
        KeyHandler.onKey(input.key());
    }
}