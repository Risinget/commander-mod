package risinget.commander.mixin;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import risinget.commander.Commander;

@Mixin(ChatHud.class)
public class ChatHudMixin {

    @Inject(method = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;)V",
            at = @At("HEAD"))
    private void onAddMessage(Text message, CallbackInfo ci) {
        Commander.LOGGER.info("Mensaje capturado: " + message.getString());
    }
}
