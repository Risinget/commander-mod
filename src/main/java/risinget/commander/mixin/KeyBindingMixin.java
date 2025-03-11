package risinget.commander.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import risinget.commander.keybinds.KeyHandler;
@Mixin(Keyboard.class)
public abstract class KeyBindingMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onKey", at = @At("HEAD") )
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
        System.out.println(KeyHandler.keyG.wasPressed());
    }
}