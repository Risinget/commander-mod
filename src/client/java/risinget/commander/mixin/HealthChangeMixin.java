package risinget.commander.mixin;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LocalPlayer.class)
public abstract class HealthChangeMixin {

//    @Inject(method = "updateHealth", at = @At("RETURN"))
//    public void onHealthChange(float health, CallbackInfo info) {
//
//    }
}
