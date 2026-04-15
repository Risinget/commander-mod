package risinget.commander.commands;

import risinget.commander.utils.FormatterUtils;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
public class WordsListCommand {
    public String[] wordList = {
        "&b&k!!&r &cDDLS ON TOP &b&k!!",
    };
    public WordsListCommand() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("wordlist").executes(context -> {
                for (String word : wordList) {
                    MutableComponent textWord = FormatterUtils.parseAndFormatText(word)
                        .withStyle(style -> style
                            .withClickEvent(new ClickEvent.CopyToClipboard(word))
                            .withHoverEvent(new HoverEvent.ShowText(Component.literal("Click para copiar!"))));
                    context.getSource().sendFeedback(textWord);
                }
                return 1;
            }));
        });
    }
}
