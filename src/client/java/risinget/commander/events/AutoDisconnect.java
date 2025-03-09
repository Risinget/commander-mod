package risinget.commander.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import risinget.commander.config.ConfigCommander;

public static class PlayerAutoDisconnect {
	public static boolean isOn;
	public static int healthMin;

	public static void syncConfig(){
		this.isOn = ConfigCommander.getOn();
		this.healthMin = ConfigCommander.getHealthMin();
	}

    public static PlayerAutoDisconnect(){

		// Registrar un evento que se dispare al entrar a un mundo
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			this.isOn = false; // Establecer this.isOn en false al entrar a un mundo
		});
		
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null) {
				float health = client.player.getHealth();
				if (health < this.healthMin && this.isOn) { // 5 corazones equivalen a 10 puntos de salud
					client.world.disconnect(); // tell the server you are disconnecting
					client.disconnect();
            		client.setScreen(new MultiplayerScreen(new TitleScreen()));
					ConfigCommander.setOn(false);
					ConfigCommander.HANDLER.save();
				}
			}
		});
    }
	public static void setHealthMin(int health){
		healthMin = health;
	}

	public static int getHealthMin(){
		return healthMin;
	}

	public static boolean getOn(){
		return isOn;
	}

	public static boolean isOn() {
		return isOn;
	}

	public static void setOn(boolean value) {
		isOn = value;
	}
}
