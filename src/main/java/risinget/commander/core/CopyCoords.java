package risinget.commander.core;

import net.minecraft.client.MinecraftClient;
import risinget.commander.config.ConfigCommander;
import risinget.commander.utils.Formatter;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.text.MutableText;

public class CopyCoords {

    private static final MinecraftClient client = MinecraftClient.getInstance();
    public static void copyCurrentPos(){
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
    }

    public static void copyPosViewing() {
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

    private static String replaceCoordsVariables(int x, int y, int z) {
        return ConfigCommander.getPosCoordsFormat().replace("{X}", String.valueOf(x))
                .replace("{Y}", String.valueOf(y))
                .replace("{Z}", String.valueOf(z));
    }

}
