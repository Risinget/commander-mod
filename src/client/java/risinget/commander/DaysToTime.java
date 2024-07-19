package risinget.commander;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.text.Text;

import java.time.Duration;
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
                        long days = totalSeconds / (3600 * 24);
                        totalSeconds %= (3600 * 24);
                        long hours = totalSeconds / 3600;
                        totalSeconds %= 3600;
                        long minutes = totalSeconds / 60;
                        long seconds = totalSeconds % 60;

                        // Calcular la fecha y hora de inicio del mundo
                        LocalDateTime now = LocalDateTime.now();
                        LocalDateTime worldStartDateTime = now.minus(Duration.ofSeconds(realSeconds));

                        // Formatear la fecha y hora para la salida
                        DateTimeFormatter formatter = DateTimeFormatter
                                .ofPattern("dd 'de' MMMM 'de' yyyy 'a las' HH:mm:ss");
                        String formattedDateTime = worldStartDateTime.format(formatter);

                        // Enviar el mensaje al jugador
                        context.getSource().sendFeedback(Text.of(
                                "Días del mundo: " + this.days + "\n" +
                                        "Tiempo transcurrido en la vida real: " + days + " días, " + hours
                                        + " horas, " +
                                        minutes + " minutos, " + seconds + " segundos\n" +
                                        "El mundo se ha iniciado estimadamente en la vida real el: "
                                        + formattedDateTime));
                        return 1;
                    }));
        });
    }
}
