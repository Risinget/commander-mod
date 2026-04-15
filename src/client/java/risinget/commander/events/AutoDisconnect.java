package risinget.commander.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;
import risinget.commander.config.ConfigCommander;
import java.util.Objects;

public class AutoDisconnect {

	public AutoDisconnect(){
		ClientTickEvents.END_CLIENT_TICK.register(this::check);
	}

	public void check(Minecraft client) {
		if (client == null || client.player == null || client.level == null || !client.isRunning()) return;
		float health = client.player.getHealth();
		if (health < ConfigCommander.getHealthMin() && ConfigCommander.isOn()) {
			disconnect(client, health);
		}
	}

	public void disconnect(Minecraft client, float health){
		if (client.level == null) return;
		if (client.isLocalServer()) {
			client.level.disconnect(Component.literal("Razón: BAJA VIDA A " + health));
			client.disconnectFromWorld(Component.literal("Razón: BAJA VIDA A " + health));
			client.setScreen(new TitleScreen());
		} else{
			Objects.requireNonNull(client.getConnection()).getConnection().disconnect(Component.literal("BAJA VIDA A " + health));
			client.setScreen(new JoinMultiplayerScreen(client.screen));
		}
		ConfigCommander.setOn(false);
		ConfigCommander.HANDLER.save();
	}
}
