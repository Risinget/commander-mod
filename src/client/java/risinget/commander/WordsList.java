package risinget.commander;

import risinget.commander.utils.Formatter;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class WordsList {

    public WordsList() {

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("wordlist").executes(context -> {
                String[] wordsList = { "&b&k!!&r &cDDLS ON TOP &b&k!!", "Texto2" };

                Formatter formatter = new Formatter();

                for (String word : wordsList) {

                    MutableText textWord = formatter.parseAndFormatText(word)
                            .styled(style -> style
                                    .withClickEvent(
                                            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/copytext " + word))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                            Text.literal("Click para copiar!"))));

                    context.getSource().sendFeedback(textWord);
                }

                return 1;
            }));

            dispatcher.register(ClientCommandManager.literal("copytext")
                    .then(ClientCommandManager.argument("text", StringArgumentType.greedyString())
                            .executes(context -> {
                                String text = StringArgumentType.getString(context, "text");
                                MinecraftClient.getInstance().keyboard.setClipboard(text); // Usa el método de
                                                                                           // Minecraft para copiar al
                                                                                           // portapapeles
                                context.getSource().sendFeedback(
                                        Text.literal("[Commander] Texto copiado al portapapeles: " + text)
                                                .formatted(Formatting.GREEN));
                                return 1;
                            })));

        });
    }

    
}
