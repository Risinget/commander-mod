package risinget.commander.utils;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Formatter {
    
    public static MutableText parseAndFormatText(String text) {
        MutableText result = Text.literal("");
        MutableText currentSegment = Text.literal("");
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '&' && i + 1 < chars.length) {
                if (!currentSegment.getSiblings().isEmpty()) {
                    result.append(currentSegment);
                    currentSegment = Text.literal("");
                }
                char colorCode = chars[i + 1];
                Formatting format = getFormattingByCode(colorCode);
                if (format != null) {
                    currentSegment = currentSegment.formatted(format);
                    i++; // Skip the color code
                }
            } else {
                currentSegment.append(Text.literal(String.valueOf(chars[i])));
            }
        }
        if (!currentSegment.getSiblings().isEmpty()) {
            result.append(currentSegment);
        }
        return result;
    }

     public static Formatting getFormattingByCode(char code) {
         return switch (code) {
             case '0' -> Formatting.BLACK;
             case '1' -> Formatting.DARK_BLUE;
             case '2' -> Formatting.DARK_GREEN;
             case '3' -> Formatting.DARK_AQUA;
             case '4' -> Formatting.DARK_RED;
             case '5' -> Formatting.DARK_PURPLE;
             case '6' -> Formatting.GOLD;
             case '7' -> Formatting.GRAY;
             case '8' -> Formatting.DARK_GRAY;
             case '9' -> Formatting.BLUE;
             case 'a' -> Formatting.GREEN;
             case 'b' -> Formatting.AQUA;
             case 'c' -> Formatting.RED;
             case 'd' -> Formatting.LIGHT_PURPLE;
             case 'e' -> Formatting.YELLOW;
             case 'f' -> Formatting.WHITE;
             case 'k' -> Formatting.OBFUSCATED;
             case 'l' -> Formatting.BOLD;
             case 'm' -> Formatting.STRIKETHROUGH;
             case 'n' -> Formatting.UNDERLINE;
             case 'o' -> Formatting.ITALIC;
             case 'r' -> Formatting.RESET;
             default -> null;
         };
    }
}
