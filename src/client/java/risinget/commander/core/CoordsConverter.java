package risinget.commander.core;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import risinget.commander.config.ConfigCommander;

public class CoordsConverter {
    private static final Minecraft client = Minecraft.getInstance();

    public static void copyAndConvertToNether(){
        if (client.player != null) {
            HitResult hitResult = client.player.pick(20.0, 0.0F, false);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
                int x = Math.round((float) blockPos.getX() /8);
                int y = blockPos.getY();
                int z = Math.round((float) blockPos.getZ() /8);
                String pos = replaceCoordsVariables(x, y, z, ConfigCommander.getFormatCoordsNether());
                client.keyboardHandler.setClipboard(pos);

                Minecraft.getInstance().gui.setOverlayMessage(
                        Component.literal("Coords convertidas al nether y copiadas"), false
                );
            } else {
              Minecraft.getInstance().gui.setOverlayMessage(
                        Component.literal("No estas mirando ningun bloque"), false
                );
            }
        }
    }

    public static void copyAndConvertToOverworld(){
        if (client.player != null) {
            HitResult hitResult = client.player.pick(20.0, 0.0F, false);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
                int x = blockPos.getX()*8;
                int y = blockPos.getY();
                int z = blockPos.getZ()*8;
                String pos = replaceCoordsVariables(x, y, z, ConfigCommander.getFormatCoordsOverworld());
                client.keyboardHandler.setClipboard(pos);
                Minecraft.getInstance().gui.setOverlayMessage(
                    Component.literal("Coords convertidas al overworld y copiadas"), false
                );
            } else {
                Minecraft.getInstance().gui.setOverlayMessage(
                    Component.literal("No estas mirando ningún bloque"), false
                );
            }
        }
    }

    public static String replaceCoordsVariables(int x, int y, int z, String formatCoords) {
        return formatCoords.replace("{X}", String.valueOf(x))
            .replace("{Y}", String.valueOf(y))
            .replace("{Z}", String.valueOf(z));
    }
}

