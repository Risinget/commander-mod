package risinget.commander;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;


public class EmojisCommand {
    
    public EmojisCommand() {
        
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("emojis").executes(context -> {
                String[] emojis = { "☄", "⭐" };

                for (String emoji : emojis) {
                    MutableText emojiText = Text.literal(emoji + " ")
                            .styled(style -> style
                                    .withColor(Formatting.RED)
                                    .withClickEvent(
                                            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/copyemoji " + emoji))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                            Text.literal("Click para copiar!"))));

                    context.getSource().sendFeedback(emojiText);
                }

                return 1;
            }));

            dispatcher.register(ClientCommandManager.literal("copyemoji")
                    .then(ClientCommandManager.argument("emoji", StringArgumentType.greedyString())
                            .executes(context -> {
                                String emoji = StringArgumentType.getString(context, "emoji");
                                MinecraftClient.getInstance().keyboard.setClipboard(emoji); // Usa el método de
                                                                                            // Minecraft para copiar al
                                                                                            // portapapeles
                                context.getSource().sendFeedback(Text.literal("Emoji copiado al portapapeles: " + emoji)
                                        .formatted(Formatting.GREEN));
                                return 1;
                            })));


        });
    }

}
