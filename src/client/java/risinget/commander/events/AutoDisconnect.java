package risinget.commander.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import risinget.commander.config.ConfigCommander;

public class AutoDisconnect {

    public AutoDisconnect(){
		// Registrar un evento que se dispare al entrar a un mundo
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> ConfigCommander.setOn(false));
		
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null) {
				float health = client.player.getHealth();
				if (health < ConfigCommander.getHealthMin() && ConfigCommander.isOn()) { // 5 corazones equivalen a 10 puntos de salud
					this.disconnect(client);
				}
			}
		});
    }

	public void disconnect(MinecraftClient client){
        client.world.disconnect(); // tell the server you are disconnecting
		client.disconnect();
		client.setScreen(new MultiplayerScreen(new TitleScreen()));
		ConfigCommander.setOn(false);
		ConfigCommander.HANDLER.save();
	}
}
