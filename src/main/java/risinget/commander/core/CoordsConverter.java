package risinget.commander.core;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;
public class CoordsConverter {
    private static boolean gPressed = false;

    public static void registerKeyCallback() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            long window = MinecraftClient.getInstance().getWindow().getHandle();

            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_G) == GLFW.GLFW_PRESS && !gPressed) {
                System.out.println("¡Tecla G presionada!");
                gPressed = true;
            }
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_G) == GLFW.GLFW_RELEASE && gPressed) {
                System.out.println("¡Tecla G soltada!");
                gPressed = false;
            }
        });
    }

    private final String formatCoordsNether = "X: {X}, Y: {Y}, Z: {Z}";
    private final String formatCoordsOverworld = "X: {X}, Y: {Y}, Z: {Z}";

    private final KeyBinding copyAndConvertToNether = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Copiar y convertir BlocksPosViewing a Nether",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F6,
            "Commander"));

    private final KeyBinding copyAndConvertToOverworld = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Copiar y convertir BlocksPosViewing a Overworld",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F7,
            "Commander"));

    public CoordsConverter() {

        // Registro del comando
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("getblockcoords")
                    .executes(context -> {
                        MinecraftClient client = MinecraftClient.getInstance();
                        if (client.player != null) {
                            HitResult hitResult = client.player.raycast(20.0, 0.0F, false);
                            if (hitResult.getType() == HitResult.Type.BLOCK) {
                                BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
                                int x = blockPos.getX();
                                int y = blockPos.getY();
                                int z = blockPos.getZ();
                                context.getSource().sendFeedback(Text.of(
                                        "Estás mirando el bloque en las coordenadas: " +
                                                "X: " + x + ", Y: " + y + ", Z: " + z));
                            } else {
                                context.getSource().sendFeedback(Text.of("No estás mirando a ningún bloque."));
                            }
                        }
                        return 1;
                    }));
        });

        // Registro de eventos de tick del cliente
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (copyAndConvertToNether.wasPressed()) {
                if (client.player != null) {
                    HitResult hitResult = client.player.raycast(20.0, 0.0F, false);
                    if (hitResult.getType() == HitResult.Type.BLOCK) {
                        BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
                        // redondear a la posición más cercana
                        int x = Math.round(blockPos.getX()/8);
                        int y = blockPos.getY();
                        int z = Math.round(blockPos.getZ()/8);
                        String pos = replaceCoordsVariables(x, y, z, this.formatCoordsNether);
                        client.keyboard.setClipboard(pos); 
                        client.player.sendMessage(Text.of("Coords convertidas al nether y copiadas"), true);

                    } else {
                        client.player.sendMessage(Text.of("No estas mirando ningún bloque"), true);
                    }
                }
            } else if (copyAndConvertToOverworld.wasPressed()) {
                if (client.player != null) {
                    HitResult hitResult = client.player.raycast(20.0, 0.0F, false);
                    if (hitResult.getType() == HitResult.Type.BLOCK) {
                        BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
                        int x = blockPos.getX()*8;
                        int y = blockPos.getY();
                        int z = blockPos.getZ()*8;
                        String pos = replaceCoordsVariables(x, y, z, this.formatCoordsOverworld);
                        client.keyboard.setClipboard(pos); 
                        client.player.sendMessage(Text.of("Coords convertidas al overworld y copiadas"), true);

                    } else {
                        client.player.sendMessage(Text.of("No estas mirando ningún bloque"), true);
                    }
                }
            }
        });
    }

    public String replaceCoordsVariables(int x, int y, int z, String formatCoords) {
        return formatCoords.replace("{X}", String.valueOf(x))
                .replace("{Y}", String.valueOf(y))
                .replace("{Z}", String.valueOf(z));
    }
}

