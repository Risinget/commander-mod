package risinget.commander;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.MutableText;
import risinget.commander.utils.Formatter;

public class Commands {

    public Commands() {

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("commands").executes(context -> {
                Formatter formatter = new Formatter();
                String commands = """
                        &c&lComandos disponibles:&r
                        &b/testTextColors <texto> &6-&7 Prueba la salida de texto con colores&r
                        &b/smallcaps &6-&7 Convierte texto a SMALLCAPS&r
                        &b/factorial <número> &6-&7 Calcula el factorial de un número&r
                        &b/emojis &6-&7 Muestra una lista de emojis para usar&r 
                        &b/colors &6-&7 Muestra una lista de colores&r
                        &b/daysToTime &6-&7 Te dice días del mundo y más&r
                        &b/wordsList &6-&7 Muestra una lista de palabras&r

                        F9 para copiar posición actual
                        F10 copiar posición del bloque donde miras
                        F6 copiar y convertir bloque donde miras a Nether
                        F7 copiar y convertir bloque donde miras a Overworld
                        F8 abrir commander GUI
                        """;
                
                MutableText textColored = formatter.parseAndFormatText(commands);

                context.getSource().sendFeedback(textColored);
                return 1;
            }));
        });
    }

}