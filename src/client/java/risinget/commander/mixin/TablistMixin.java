package risinget.commander.mixin;

import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import risinget.commander.core.TabListStorage;


@Mixin(ClientGamePacketListener.class)
public interface TablistMixin {

//    @Unique
//    private static final Logger LOGGER = Commander.getLogger(TablistMixin.class);
    @Unique
    public default void onPlayerListHeader(ClientboundTabListPacket clientboundTabListPacket, CallbackInfo ci) {
        TabListStorage.setHeader(clientboundTabListPacket.header());
        TabListStorage.setFooter(clientboundTabListPacket.footer());
    }
}
