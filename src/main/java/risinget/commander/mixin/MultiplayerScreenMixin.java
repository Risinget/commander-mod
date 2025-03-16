package risinget.commander.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.ServerList;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import risinget.commander.core.ServerSaver;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {

    @Shadow protected MultiplayerServerListWidget serverListWidget;
    @Shadow private ServerList serverList;

    protected MultiplayerScreenMixin(Text title) { super(title); }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        int baseX = this.width / 2 - 154;
        int baseY = this.height - 54;

        addButton("Copy MOTD", baseX + 325, baseY, button -> ServerSaver.copyMOTD(this.serverListWidget));
        addButton("Save All Info", baseX + 395, baseY, button -> ServerSaver.saveAllServerInfo(serverList));
        addButton("Copy as IP", baseX + 325, baseY + 15, button -> ServerSaver.copyNumericIP(this.serverListWidget));
        addButton("Save Icon", baseX + 395, baseY + 15, button -> ServerSaver.saveSelectedIcon(this.serverListWidget));
        addButton("Open Folder Servers", baseX + 325, baseY + 30, button -> ServerSaver.openServerFolder());
    }

    @Unique
    private void addButton(String label, int x, int y, ButtonWidget.PressAction action) {
        this.addDrawableChild(ButtonWidget.builder(Text.of(label), action).dimensions(x, y, 70, 15).build());
    }

}
