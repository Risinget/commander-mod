package risinget.commander.mixin;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import risinget.commander.core.ServerSaver;

@Mixin(JoinMultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {

    @Shadow protected ServerSelectionList serverSelectionList;
    @Shadow private ServerList servers;

    protected MultiplayerScreenMixin(Component title) { super(title); }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        int baseX = this.width / 2 - 154;
        int baseY = this.height - 54;

        addButton("Copy MOTD", baseX + 325, baseY, button -> ServerSaver.copyMOTD(this.serverSelectionList));
        addButton("Save All Info", baseX + 395, baseY, button -> ServerSaver.saveAllServerInfo(servers));
        addButton("Copy as IP", baseX + 325, baseY + 15, button -> ServerSaver.copyNumericIP(this.serverSelectionList));
        addButton("Save Icon", baseX + 395, baseY + 15, button -> ServerSaver.saveSelectedIcon(this.serverSelectionList));
        addButton("Open Folder Servers", baseX + 325, baseY + 30, button -> ServerSaver.openServerFolder());
    }

    @Unique
    private void addButton(String label, int x, int y, Button.OnPress action) {
        this.addRenderableWidget(Button.builder(Component.nullToEmpty(label), action).bounds(x, y, 70, 15).build());
    }

}
