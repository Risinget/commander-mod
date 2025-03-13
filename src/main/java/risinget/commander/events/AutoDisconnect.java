package risinget.commander.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.text.Text;
import risinget.commander.config.ConfigCommander;
import java.util.Objects;

public class AutoDisconnect {

	public AutoDisconnect(){
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> ConfigCommander.setOn(false));
		ClientTickEvents.END_CLIENT_TICK.register(this::check);
	}

	public void check(MinecraftClient client) {
		if (client == null || client.player == null || client.world == null || !client.isRunning()) return;
		float health = client.player.getHealth();
		if (health < ConfigCommander.getHealthMin() && ConfigCommander.isOn()) {
			disconnect(client, health);
		}
	}

	public void disconnect(MinecraftClient client, float health){
		if (client.world == null) return;
		if (client.isInSingleplayer()) {
			client.world.disconnect();
			client.disconnect();
			client.setScreen(new TitleScreen());
		} else{
			Objects.requireNonNull(client.getNetworkHandler()).getConnection().disconnect(Text.literal("BAJA VIDA A " + health));
			client.setScreen(new MultiplayerScreen(client.currentScreen));
		}
		ConfigCommander.setOn(false);
		ConfigCommander.HANDLER.save();
	}
}
