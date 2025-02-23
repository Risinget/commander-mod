package risinget.commander;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;

public class PlayerAutoDisconnect {
	public boolean isOn;
	public int healthMin;

	public void syncConfig(){
		this.isOn = ConfigCommander.getOn();
		this.healthMin = ConfigCommander.getHealthMin();
	}

    public PlayerAutoDisconnect(){

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
	public void setHealthMin(int healthMin){
		this.healthMin = healthMin;
	}

	public int getHealthMin(){
		return this.healthMin;
	}

	public boolean getOn(){
		return this.isOn;
	}

	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}
}
