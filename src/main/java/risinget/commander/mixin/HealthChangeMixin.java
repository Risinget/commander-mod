package risinget.commander.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayerEntity.class)
public abstract class HealthChangeMixin {

//    @Inject(method = "updateHealth", at = @At("RETURN"))
//    public void onHealthChange(float health, CallbackInfo info) {
//
//    }
}
