package risinget.commander.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import risinget.commander.config.ConfigCommander;

public class CoordsConverter {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void copyAndConvertToNether(){
        if (client.player != null) {
            HitResult hitResult = client.player.raycast(20.0, 0.0F, false);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
                int x = Math.round((float) blockPos.getX() /8);
                int y = blockPos.getY();
                int z = Math.round((float) blockPos.getZ() /8);
                String pos = replaceCoordsVariables(x, y, z, ConfigCommander.getFormatCoordsNether());
                client.keyboard.setClipboard(pos);
                client.player.sendMessage(Text.of("Coords convertidas al nether y copiadas"), true);
            } else {
                client.player.sendMessage(Text.of("No estas mirando ningún bloque"), true);
            }
        }
    }

    public static void copyAndConvertToOverworld(){
        if (client.player != null) {
            HitResult hitResult = client.player.raycast(20.0, 0.0F, false);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
                int x = blockPos.getX()*8;
                int y = blockPos.getY();
                int z = blockPos.getZ()*8;
                String pos = replaceCoordsVariables(x, y, z, ConfigCommander.getFormatCoordsOverworld());
                client.keyboard.setClipboard(pos);
                client.player.sendMessage(Text.of("Coords convertidas al overworld y copiadas"), true);

            } else {
                client.player.sendMessage(Text.of("No estas mirando ningún bloque"), true);
            }
        }
    }

    public static String replaceCoordsVariables(int x, int y, int z, String formatCoords) {
        return formatCoords.replace("{X}", String.valueOf(x))
            .replace("{Y}", String.valueOf(y))
            .replace("{Z}", String.valueOf(z));
    }
}

