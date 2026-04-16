package risinget.commander.commands;
import org.jetbrains.annotations.NotNull;
import risinget.commander.utils.FormatterUtils;
import risinget.commander.utils.Prefix;
import java.util.HashMap;
import java.util.Map;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;

public class SmallCapsCommand {

    public SmallCapsCommand() {
        Map<Character, Character> diccionario = getDiccionario();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommands.literal("smallcaps")
            .then(ClientCommands.argument("texto", StringArgumentType.greedyString())
                .executes(context -> {
                    String texto = StringArgumentType.getString(context, "texto");
                    StringBuilder textoConvertido = new StringBuilder();

                    for (char c : texto.toCharArray()) {
                        if (diccionario.containsKey(c)) {
                            textoConvertido.append(diccionario.get(c));
                        } else if (diccionario.containsKey(Character.toUpperCase(c))) {
                            textoConvertido.append(diccionario.get(Character.toUpperCase(c)));
                        } else {
                            textoConvertido.append(c);
                        }
                    }
                    String outputWithText = Prefix.COMMANDER + " &7Tu texto convertido es:&r "+ textoConvertido;
                    MutableComponent feedbackText = FormatterUtils.parseAndFormatText(outputWithText)
                                    .withStyle(style -> style
                                    .withHoverEvent(new HoverEvent.ShowText(Component.literal("Click para copiar")))
                                    .withClickEvent(new ClickEvent.CopyToClipboard(textoConvertido.toString())));
                    context.getSource().sendFeedback(feedbackText);
                    return 1;
                })
            )
        ));

    }

    private static @NotNull Map<Character, Character> getDiccionario() {
        Map<Character, Character> diccionario = new HashMap<>();

        diccionario.put('A', 'ᴀ');
        diccionario.put('B', 'ʙ');
        diccionario.put('C', 'ᴄ');
        diccionario.put('D', 'ᴅ');
        diccionario.put('E', 'ᴇ');
        diccionario.put('F', 'ғ');
        diccionario.put('G', 'ɢ');
        diccionario.put('H', 'ʜ');
        diccionario.put('I', 'ɪ');
        diccionario.put('J', 'ᴊ');
        diccionario.put('K', 'ᴋ');
        diccionario.put('L', 'ʟ');
        diccionario.put('M', 'ᴍ');
        diccionario.put('N', 'ɴ');
        diccionario.put('Ñ', 'Ñ');
        diccionario.put('O', 'ᴏ');
        diccionario.put('P', 'ᴘ');
        diccionario.put('Q', 'ǫ');
        diccionario.put('R', 'ʀ');
        diccionario.put('S', 's');
        diccionario.put('T', 'ᴛ');
        diccionario.put('U', 'ᴜ');
        diccionario.put('V', 'ᴠ');
        diccionario.put('W', 'ᴡ');
        diccionario.put('X', 'x');
        diccionario.put('Y', 'ʏ');
        diccionario.put('Z', 'ᴢ');
        return diccionario;
    }

}
