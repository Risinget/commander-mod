package risinget.commander.commands;

import risinget.commander.utils.Formatter;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
public class WordsList {
    public String[] wordList = {
        "&b&k!!&r &cDDLS ON TOP &b&k!!",
    };
    public WordsList() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("wordlist").executes(context -> {
                for (String word : wordList) {
                    MutableText textWord = Formatter.parseAndFormatText(word)
                        .styled(style -> style
                            .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, word))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click para copiar!"))));
                    context.getSource().sendFeedback(textWord);
                }
                return 1;
            }));
        });
    }
}
