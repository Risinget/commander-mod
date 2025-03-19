package risinget.commander.utils;

import java.awt.*;

public class HexUtil {

    public static final String[] minecraftColorsHex = {
            "#000000", "#0000AA", "#00AA00", "#00AAAA",
            "#AA0000", "#AA00AA", "#FFAA00", "#AAAAAA",
            "#555555", "#5555FF", "#55FF55", "#55FFFF",
            "#FF5555", "#FF55FF", "#FFFF55", "#FFFFFF"
    };

    public static final String[] minecraftColorCodes = {
            "&0", "&1", "&2", "&3",
            "&4", "&5", "&6", "&7",
            "&8", "&9", "&a", "&b",
            "&c", "&d", "&e", "&f"
    };

    public static String hexToMinecraftColor(String hex) {
        Color inputColor = Color.decode(hex);
        double minDistance = Double.MAX_VALUE;
        String closestColorCode = "";
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

    public static double colorDistance(Color c1, Color c2) {
        int redDiff = c1.getRed() - c2.getRed();
        int greenDiff = c1.getGreen() - c2.getGreen();
        int blueDiff = c1.getBlue() - c2.getBlue();
        return Math.sqrt(redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
    }
}
