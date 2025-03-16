package risinget.commander.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import risinget.commander.Commander;
import risinget.commander.core.TabListStorage;

@Mixin(ClientPlayNetworkHandler.class)
public class TablistMixin {

    @Unique
    private static final Logger LOGGER = Commander.getLogger(TablistMixin.class);
    @Inject(method = "onPlayerListHeader", at = @At("HEAD"))
    private void onPlayerListHeader(PlayerListHeaderS2CPacket packet, CallbackInfo ci) {
        TabListStorage.setHeader(packet.header());
        TabListStorage.setFooter(packet.footer());
    }
}