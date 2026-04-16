package risinget.commander.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import risinget.commander.utils.FormatterUtils;

public class TestTextColorsCommand {

    public TestTextColorsCommand() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommands.literal("testColors")
                .then(ClientCommands.argument("text", StringArgumentType.greedyString())
                    .executes(context -> {
                        String textToTest = StringArgumentType.getString(context, "text");
                        MutableComponent textColored = FormatterUtils.parseAndFormatText(textToTest)
                            .withStyle(style -> style
                            .withClickEvent(new ClickEvent.CopyToClipboard(textToTest))
                            .withHoverEvent(new HoverEvent.ShowText(Component.literal("Click para copiar!"))));
                        context.getSource().sendFeedback(textColored);
//                        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(textColored);
                        return 1;
                })));
        });
    }
}
