package risinget.commander.commands;

import risinget.commander.config.ConfigCommander;
import risinget.commander.utils.Formatter;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.text.MutableText;

public class CopyCoords {

    public CopyCoords() {
        // Inicialización del KeyBinding
        KeyBinding copyPosKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Copiar posición", // La traducción del nombre de la tecla en el archivo de idiomas
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F9, // La tecla F9
                "Commander" // La categoría de la tecla en la configuración de controles
        ));

        // Inicialización del KeyBinding
        KeyBinding copyPosBlockViewing = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Copiar posición del bloque donde miras", // La traducción del nombre de la tecla en el archivo de idiomas
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F10, // La tecla F9
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
                    String pos = replaceCoordsVariables(posX, posY, posZ);

                    client.keyboard.setClipboard(pos); // Use Minecraft's method to set clipboard content
                    MutableText text = Formatter.parseAndFormatText("&b&oCoords de posición copiadas");
                    // client.player.sendMessage(Text.of("Coords de posición copiadas"), true);
                    client.player.sendMessage(text, true);

                }
            }else if(copyPosBlockViewing.wasPressed()){
                if (client.player != null) {
                    HitResult hitResult = client.player.raycast(20.0, 0.0F, false);
                    if (hitResult.getType() == HitResult.Type.BLOCK) {
                        BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
                        int x = blockPos.getX();
                        int y = blockPos.getY();
                        int z = blockPos.getZ();
                        String pos = replaceCoordsVariables(x, y, z);
                        client.keyboard.setClipboard(pos); // Use Minecraft's method to set clipboard content
                        client.player.sendMessage(Text.of("Coords del bloque copiadas"), true);

                    }
                }
            }
        });
    }

    private String replaceCoordsVariables(int x, int y, int z) {
        return ConfigCommander.getCoordsFormat().replace("{X}", String.valueOf(x))
                .replace("{Y}", String.valueOf(y))
                .replace("{Z}", String.valueOf(z));
    }

}
