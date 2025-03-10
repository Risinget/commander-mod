package risinget.commander.commands;
import org.jetbrains.annotations.NotNull;
import risinget.commander.utils.Formatter;
import risinget.commander.utils.Prefix;

import java.util.HashMap;
import java.util.Map;

import com.mojang.brigadier.arguments.StringArgumentType;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class SmallCapsConverter {

    public void converter() {
        Map<Character, Character> diccionario = getDiccionario();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("smallcaps")
            .then(ClientCommandManager.argument("texto", StringArgumentType.greedyString())
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
                    String outputWithText = Prefix.COMMANDER + "&7Tu texto convertido es:&r "+ textoConvertido;
                    MutableText feedbackText = Formatter.parseAndFormatText(outputWithText)
                                    .styled(style -> style
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Click para copiar")))
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, textoConvertido.toString())));
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
