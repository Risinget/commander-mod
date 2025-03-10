package risinget.commander.commands;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.text.MutableText;
import risinget.commander.utils.Formatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DaysToTime {

    public long days;

    public DaysToTime() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("daystotime")
                .executes(context -> {
                    long timeOfDay = context.getSource().getWorld().getTimeOfDay();
                    this.days = timeOfDay / 24000;

                    // Convertir los días del juego a tiempo real (segundos)
                    long realSeconds = this.days * 1200; // 20 minutos * 60 segundos
                    long totalSeconds = realSeconds;
                    totalSeconds %= (3600 * 24);
                    long hours = totalSeconds / 3600;

                    // Fecha y hora actual
                    LocalDateTime fechaActual = LocalDateTime.now();
                    // Restar hours a la fecha actual
                    LocalDateTime fechaModificada = fechaActual.minusHours(hours);
                    // Formatear la fecha para imprimirla
                    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                    String fechaEstimada = fechaModificada.format(formato);

                    String message = 
                            "&7Días del mundo:&b " + this.days + "\n" +
                            "&7Tiempo transcurrido en la vida real:&b " + hours + " horas\n" +
                            "&7El mundo se ha iniciado el:&b " + fechaEstimada;
                    
                    MutableText textColored = Formatter.parseAndFormatText(message);
                    
                    // Enviar el mensaje al jugador
                    context.getSource().sendFeedback(textColored);
                    return 1;
                }));
        });
    }
}
