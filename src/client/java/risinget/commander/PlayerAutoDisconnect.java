package risinget.commander;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;

public class PlayerAutoDisconnect {
	public boolean isOn;

    public PlayerAutoDisconnect(){

        
		// Registrar un evento que se dispare al entrar a un mundo
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			this.isOn = false; // Establecer this.isOn en false al entrar a un mundo
		});
		

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("on").executes(context -> {
				this.isOn = true;
				return 1;
			}));
		});

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("off").executes(context -> {
				this.isOn = false;
				return 1;
			}));
		});



		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null) {
				float health = client.player.getHealth();
				if (health < 10 && this.isOn) { // 5 corazones equivalen a 10 puntos de salud
					client.world.disconnect(); // tell the server you are disconnecting
					client.disconnect();
            		client.setScreen(new MultiplayerScreen(new TitleScreen()));

				}
			}
		});
    }
}
