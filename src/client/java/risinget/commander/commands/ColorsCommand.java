package risinget.commander.commands;

import risinget.commander.utils.FormatterUtils;
import risinget.commander.utils.Prefix;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;

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
                MutableComponent combinedText = Component.literal(Prefix.COMMANDER+"&7 Minecraft colors:\n");
                for (int i = 0; i < colors.length; i++) {
                    final int index = i;
                    MutableComponent colorText = FormatterUtils.parseAndFormatColor(colors[index])
                            .withStyle(style -> style
                                    .withClickEvent(new ClickEvent.CopyToClipboard(colors[index]))
                                    .withInsertion(colors[index])
                                    .withHoverEvent(new HoverEvent.ShowText(Component.literal("Shift + Click para Insertar!")))
                            );

                    combinedText.append(colorText);
                    if ((index + 1) % 4 == 0) {
                        combinedText.append(Component.literal("\n"));
                    } else {
                        combinedText.append(Component.literal(" "));
                    }
                }
                combinedText.append(Component.literal("\n"));
                for (int i = 0; i < styles.length; i++) {
                    final int index = i;
                    MutableComponent styleText = FormatterUtils.parseAndFormatColor(styles[index])
                        .withStyle(style -> style
                        .withClickEvent(new ClickEvent.CopyToClipboard(styles[index]))
                        .withInsertion(styles[index])
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("Click para copiar!"))));
                    combinedText.append(styleText);
                    if ((index + 1) % 2 == 0) {
                        combinedText.append(Component.literal("\n"));
                    } else {
                        combinedText.append(Component.literal(" "));
                    }
                }
                context.getSource().sendFeedback(combinedText);
                return 1;
            }));
        });
    }
}