package risinget.commander.commands;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.MutableText;
import risinget.commander.utils.Formatter;

public class ListCommandsCommand {

    public ListCommandsCommand() {

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("commands").executes(context -> {
                String commands = """
                        &c&lComandos disponibles:&r
                        &b/testTextColors <texto> &6-&7 Prueba la salida de texto con colores&r
                        &b/smallcaps &6-&7 Convierte texto a SMALLCAPS&r
                        &b/factorial <número> &6-&7 Calcula el factorial de un número&r
                        &b/emojis &6-&7 Muestra una lista de emojis para usar&r
                        &b/colors &6-&7 Muestra una lista de colores&r
                        &b/daysToTime &6-&7 Te dice días del mundo y más&r
                        &b/wordsList &6-&7 Muestra una lista de palabras&r
                        &b/gemini &7<prompt> &6-&7 Conversa com Gemini&r
                        &b/wordsList &6-&7 Muestra una lista de palabras&r

                        &7F9 para copiar posición actual
                        &7F10 copiar posición del bloque donde miras
                        &7F6 copiar y convertir bloque donde miras a Nether
                        &7F7 copiar y convertir bloque donde miras a Overworld
                        &7F8 abrir commander GUI
                        """;
                MutableText textColored = Formatter.parseAndFormatText(commands);
                context.getSource().sendFeedback(textColored);
                return 1;
            }));
        });
    }


}