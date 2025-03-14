package risinget.commander.commands;

import risinget.commander.utils.Formatter;
import risinget.commander.utils.Prefix;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
public class ColorsCommand {

    public ColorsCommand() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("colors").executes(context -> {
                String[] colors = {
                        "&0", "&1", "&2", "&3",
                        "&4", "&5", "&6", "&7",
                        "&8", "&9", "&a", "&b",
                        "&c", "&d", "&e", "&f"
                };
                String[] styles = {
                        "&k", "&l",
                        "&m", "&n",
                        "&o", "&r"
                };
                MutableText combinedText = Text.literal(Prefix.COMMANDER+"&7 Minecraft colors:\n");
                for (int i = 0; i < colors.length; i++) {
                    final int index = i;
                    MutableText colorText = Formatter.parseAndFormatColor(colors[index])
                        .styled(style -> style
                        .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,colors[index]))
                        .withInsertion(colors[index])
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Shift + Click para Insertar!"))));
                    combinedText.append(colorText);
                    if ((index + 1) % 4 == 0) {
                        combinedText.append(Text.literal("\n"));
                    } else {
                        combinedText.append(Text.literal(" "));
                    }
                }
                combinedText.append(Text.literal("\n"));
                for (int i = 0; i < styles.length; i++) {
                    final int index = i;
                    MutableText styleText = Formatter.parseAndFormatColor(styles[index])
                        .styled(style -> style
                        .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,styles[index]))
                        .withInsertion(styles[index])
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click para copiar!"))));
                    combinedText.append(styleText);
                    if ((index + 1) % 2 == 0) {
                        combinedText.append(Text.literal("\n"));
                    } else {
                        combinedText.append(Text.literal(" "));
                    }
                }
                context.getSource().sendFeedback(combinedText);
                return 1;
            }));
        });
    }
}