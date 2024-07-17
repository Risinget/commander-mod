package risinget.commander;

import java.util.HashMap;
import java.util.Map;

import com.mojang.brigadier.arguments.StringArgumentType;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

public class SmallCapsConverter {
    private final Map<Character, Character> diccionario = new HashMap<>();

    public void converter() {
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

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("smallcaps")
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

                        String convertedText = textoConvertido.toString();

                        MutableText feedbackText = Text.literal("Tu texto convertido es: " + convertedText)
                            .styled(style -> style
                                .withColor(TextColor.fromRgb(0x00FF00)) // Cambia el color si deseas
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click para copiar")))
                                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, convertedText))
                            );

                        context.getSource().sendFeedback(feedbackText);
                        return 1;
                    })
                )
            );
        });

    }

}
