package risinget.commander;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class CoordsCopyShortcut {

    public CoordsCopyShortcut() {
         // Inicialización del KeyBinding
        KeyBinding copyPosKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Copiar posición", // La traducción del nombre de la tecla en el archivo de idiomas
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F9, // La tecla F9
                "Commander" // La categoría de la tecla en la configuración de controles
        ));

        // Registro del evento para escuchar el tick del cliente
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (copyPosKeyBinding.wasPressed()) {
                if (client.player != null) {
					// X: 0, Y: 0, Z: 0
					int posX = client.player.getBlockPos().getX();
					int posY = client.player.getBlockPos().getY();
					int posZ = client.player.getBlockPos().getZ();
					String pos = "X: " + posX + ", Y: " + posY + ", Z: " + posZ;

					client.keyboard.setClipboard(pos); // Use Minecraft's method to set clipboard content
				}
            }
        });
    }

    
    
}
