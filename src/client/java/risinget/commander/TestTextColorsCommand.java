package risinget.commander;

import risinget.commander.utils.Formatter;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class TestTextColorsCommand {

    public TestTextColorsCommand() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("testTextColors")
                    .then(ClientCommandManager.argument("textToTest", StringArgumentType.greedyString())
                            .executes(context -> {
                                // Obtener el argumento
                                String textToTest = StringArgumentType.getString(context, "textToTest");
                                // Create an instance of Formatter
                                Formatter formatter = new Formatter();

                                MutableText textColored = formatter.parseAndFormatText(textToTest)
                                        .styled(style -> style
                                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                                        "/copytext " + textToTest))
                                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                        Text.literal("Click para copiar!"))));

                                context.getSource().sendFeedback(textColored);
                                return 1;
                            })));
        });
    }

    // private MutableText parseAndFormatText(String text) {
    //     MutableText result = Text.literal("");
    //     MutableText currentSegment = Text.literal("");
    //     char[] chars = text.toCharArray();
    //     for (int i = 0; i < chars.length; i++) {
    //         if (chars[i] == '&' && i + 1 < chars.length) {
    //             if (currentSegment.getSiblings().size() > 0) {
    //                 result.append(currentSegment);
    //                 currentSegment = Text.literal("");
    //             }
    //             char colorCode = chars[i + 1];
    //             Formatting format = getFormattingByCode(colorCode);
    //             if (format != null) {
    //                 currentSegment = currentSegment.formatted(format);
    //                 i++; // Skip the color code
    //             }
    //         } else {
    //             currentSegment.append(Text.literal(String.valueOf(chars[i])));
    //         }
    //     }
    //     if (currentSegment.getSiblings().size() > 0) {
    //         result.append(currentSegment);
    //     }
    //     return result;
    // }

    // private Formatting getFormattingByCode(char code) {
    //     switch (code) {
    //         case '0':
    //             return Formatting.BLACK;
    //         case '1':
    //             return Formatting.DARK_BLUE;
    //         case '2':
    //             return Formatting.DARK_GREEN;
    //         case '3':
    //             return Formatting.DARK_AQUA;
    //         case '4':
    //             return Formatting.DARK_RED;
    //         case '5':
    //             return Formatting.DARK_PURPLE;
    //         case '6':
    //             return Formatting.GOLD;
    //         case '7':
    //             return Formatting.GRAY;
    //         case '8':
    //             return Formatting.DARK_GRAY;
    //         case '9':
    //             return Formatting.BLUE;
    //         case 'a':
    //             return Formatting.GREEN;
    //         case 'b':
    //             return Formatting.AQUA;
    //         case 'c':
    //             return Formatting.RED;
    //         case 'd':
    //             return Formatting.LIGHT_PURPLE;
    //         case 'e':
    //             return Formatting.YELLOW;
    //         case 'f':
    //             return Formatting.WHITE;
    //         case 'k':
    //             return Formatting.OBFUSCATED;
    //         case 'l':
    //             return Formatting.BOLD;
    //         case 'm':
    //             return Formatting.STRIKETHROUGH;
    //         case 'n':
    //             return Formatting.UNDERLINE;
    //         case 'o':
    //             return Formatting.ITALIC;
    //         case 'r':
    //             return Formatting.RESET;
    //         default:
    //             return null;
    //     }
    // }
}
