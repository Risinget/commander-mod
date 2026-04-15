package risinget.commander.core;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import risinget.commander.config.ConfigCommander;
import risinget.commander.utils.FormatterUtils;

public class CopyCoords {

    private static final Minecraft client = Minecraft.getInstance();
    public static void copyCurrentPos(){
        if (client.player != null) {
            // X: 0, Y: 0, Z: 0
            int posX = client.player.blockPosition().getX();
            int posY = client.player.blockPosition().getY();
            int posZ = client.player.blockPosition().getZ();
            String pos = replaceCoordsVariables(posX, posY, posZ);

            client.keyboardHandler.setClipboard(pos); // Use Minecraft's method to set clipboard content
            MutableComponent text = FormatterUtils.parseAndFormatText("&b&oCoords de posición copiadas");
            // client.player.sendMessage(Text.of("Coords de posición copiadas"), true);
            client.player.displayClientMessage(text, true);

        }
    }

    public static void copyPosViewing() {
        if (client.player != null) {
            HitResult hitResult = client.player.pick(20.0, 0.0F, false);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
                int x = blockPos.getX();
                int y = blockPos.getY();
                int z = blockPos.getZ();
                String pos = replaceCoordsVariables(x, y, z);
                client.keyboardHandler.setClipboard(pos); // Use Minecraft's method to set clipboard content
                client.player.displayClientMessage(Component.nullToEmpty("Coords del bloque copiadas"), true);

            }
        }
    }

    private static String replaceCoordsVariables(int x, int y, int z) {
        return ConfigCommander.getPosCoordsFormat().replace("{X}", String.valueOf(x))
                .replace("{Y}", String.valueOf(y))
                .replace("{Z}", String.valueOf(z));
    }

}
