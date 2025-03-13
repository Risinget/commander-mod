package risinget.commander.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class CoordsConverter {

    private static final String formatCoordsNether = "X: {X}, Y: {Y}, Z: {Z}";
    private static final String formatCoordsOverworld = "X: {X}, Y: {Y}, Z: {Z}";
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public CoordsConverter() {

        // Registro de eventos de tick del cliente
//        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            if (copyAndConvertToNether.wasPressed()) {
//                if (client.player != null) {
//                    HitResult hitResult = client.player.raycast(20.0, 0.0F, false);
//                    if (hitResult.getType() == HitResult.Type.BLOCK) {
//                        BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
//                        // redondear a la posición más cercana
//                        int x = Math.round(blockPos.getX()/8);
//                        int y = blockPos.getY();
//                        int z = Math.round(blockPos.getZ()/8);
//                        String pos = replaceCoordsVariables(x, y, z, this.formatCoordsNether);
//                        client.keyboard.setClipboard(pos);
//                        client.player.sendMessage(Text.of("Coords convertidas al nether y copiadas"), true);
//
//                    } else {
//                        client.player.sendMessage(Text.of("No estas mirando ningún bloque"), true);
//                    }
//                }
//            } else if (copyAndConvertToOverworld.wasPressed()) {
//                if (client.player != null) {
//                    HitResult hitResult = client.player.raycast(20.0, 0.0F, false);
//                    if (hitResult.getType() == HitResult.Type.BLOCK) {
//                        BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
//                        int x = blockPos.getX()*8;
//                        int y = blockPos.getY();
//                        int z = blockPos.getZ()*8;
//                        String pos = replaceCoordsVariables(x, y, z, this.formatCoordsOverworld);
//                        client.keyboard.setClipboard(pos);
//                        client.player.sendMessage(Text.of("Coords convertidas al overworld y copiadas"), true);
//
//                    } else {
//                        client.player.sendMessage(Text.of("No estas mirando ningún bloque"), true);
//                    }
//                }
//            }
//        });
    }

    public static void copyAndConvertToNether(){
        if (client.player != null) {
            HitResult hitResult = client.player.raycast(20.0, 0.0F, false);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
                // redondear a la posición más cercana
                int x = Math.round(blockPos.getX()/8);
                int y = blockPos.getY();
                int z = Math.round(blockPos.getZ()/8);
                String pos = replaceCoordsVariables(x, y, z, formatCoordsNether);
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
                String pos = replaceCoordsVariables(x, y, z, formatCoordsOverworld);
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

