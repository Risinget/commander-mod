package risinget.commander;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class PlayerSecure{

    public PlayerSecure(){

       ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                float health = client.player.getHealth();
                if (health < 10) { // 5 corazones equivalen a 10 puntos de salud
                    disconnectPlayer(client);
                }
            }
        });
    }

    private void disconnectPlayer(MinecraftClient client) {
        client.world.disconnect(); // tell the server you are disconnecting
        client.disconnect();
    }
}