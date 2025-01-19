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
                                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/copytext " + textToTest))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click para copiar!"))));

                        context.getSource().sendFeedback(textColored);
                        return 1;
                    })));
        });
    }

}
