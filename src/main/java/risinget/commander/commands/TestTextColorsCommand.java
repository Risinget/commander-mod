package risinget.commander.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import risinget.commander.utils.Formatter;

public class TestTextColorsCommand {

    public TestTextColorsCommand() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("testTextColors")
                .then(ClientCommandManager.argument("textToTest", StringArgumentType.greedyString())
                    .executes(context -> {
                        String textToTest = StringArgumentType.getString(context, "textToTest");
                        MutableText textColored = Formatter.parseAndFormatText(textToTest)
                                .styled(style -> style
                                        .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, textToTest))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click para copiar!"))));
                        context.getSource().sendFeedback(textColored);
//                        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(textColored);
                        return 1;
                })));
        });
    }
}
