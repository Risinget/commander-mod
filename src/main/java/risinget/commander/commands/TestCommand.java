package risinget.commander.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import java.awt.*;

public class TestCommand {
    public TestCommand() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("prueba")
                .then(ClientCommandManager.argument("text", StringArgumentType.string())
                    .executes((context)->{
                        // Ejemplo de uso
                        String hexColor = "#3AA9FF"; // Color HEX de "CubeCraft"
                        String minecraftColor = hexToMinecraftColor(hexColor);

                        System.out.println("Color HEX: " + hexColor);
                        System.out.println("Código de Minecraft más cercano: " + minecraftColor);


                        return 1;
                    })
                ));
        });
    }

    // Colores sólidos de Minecraft en formato HEX
    private static final String[] minecraftColorsHex = {
            "#000000", "#0000AA", "#00AA00", "#00AAAA",
            "#AA0000", "#AA00AA", "#FFAA00", "#AAAAAA",
            "#555555", "#5555FF", "#55FF55", "#55FFFF",
            "#FF5555", "#FF55FF", "#FFFF55", "#FFFFFF"
    };

    // Códigos de color de Minecraft
    private static final String[] minecraftColorCodes = {
            "&0", "&1", "&2", "&3",
            "&4", "&5", "&6", "&7",
            "&8", "&9", "&a", "&b",
            "&c", "&d", "&e", "&f"
    };

    // Método para convertir HEX a código de Minecraft
    public static String hexToMinecraftColor(String hex) {
        Color inputColor = Color.decode(hex);

        double minDistance = Double.MAX_VALUE;
        String closestColorCode = "";

        // Comparar el color HEX con cada color sólido de Minecraft
        for (int i = 0; i < minecraftColorsHex.length; i++) {
            Color minecraftColor = Color.decode(minecraftColorsHex[i]);
            double distance = colorDistance(inputColor, minecraftColor);

            if (distance < minDistance) {
                minDistance = distance;
                closestColorCode = minecraftColorCodes[i];
            }
        }

        return closestColorCode;
    }

    // Método para calcular la distancia entre dos colores
    private static double colorDistance(Color c1, Color c2) {
        int redDiff = c1.getRed() - c2.getRed();
        int greenDiff = c1.getGreen() - c2.getGreen();
        int blueDiff = c1.getBlue() - c2.getBlue();

        return Math.sqrt(redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
    }
}