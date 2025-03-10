package risinget.commander.commands;

import risinget.commander.utils.Formatter;
import risinget.commander.utils.Prefix;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.Format;

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

                MutableText combinedText = Text.literal("Minecraft colors:\n");

                // Adding colors
                for (int i = 0; i < colors.length; i++) {
                    final int index = i; // Crear una variable final para la lambda
                    MutableText colorText = Formatter.parseAndFormatText(colors[index])
                            .styled(style -> style
                                    .withClickEvent(
                                            new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, colors[index]))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                            Text.literal("Click para copiar!"))));

                    combinedText.append(colorText);
                    if ((index + 1) % 4 == 0) {
                        combinedText.append(Text.literal("\n")); // Añadir un salto de línea cada 4 colores
                    } else {
                        combinedText.append(Text.literal(" ")); // Añadir un espacio entre los colores
                    }
                }

                combinedText.append(Text.literal("\n")); // Añadir saltos de línea entre colores y estilos

                // Adding styles
                for (int i = 0; i < styles.length; i++) {
                    final int index = i; // Crear una variable final para la lambda
                    MutableText styleText = Formatter.parseAndFormatText(styles[index])
                            .styled(sty -> sty
                                    .withClickEvent(
                                            new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, styles[index]))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                            Text.literal("Click para copiar!"))));

                    combinedText.append(styleText);
                    if ((index + 1) % 2 == 0) {
                        combinedText.append(Text.literal("\n")); // Añadir un salto de línea cada 2 estilos
                    } else {
                        combinedText.append(Text.literal(" ")); // Añadir un espacio entre los estilos
                    }
                }
                context.getSource().sendFeedback(combinedText);
                return 1;
            }));
        });
    }


}
