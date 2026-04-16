package risinget.commander.commands;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.chat.MutableComponent;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import risinget.commander.utils.FormatterUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DaysToTimeCommand {

    private long days;

    public DaysToTimeCommand() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommands.literal("daystotime")
                .executes(context -> {
                    long timeOfDay = context.getSource().getClient().player.level().getGameTime() % 24000;
                    this.days = timeOfDay / 24000;
                    long totalSeconds = this.days * 1200;
                    totalSeconds %= (3600 * 24);
                    long hours = totalSeconds / 3600;
                    LocalDateTime fechaActual = LocalDateTime.now();
                    LocalDateTime fechaModificada = fechaActual.minusHours(hours);
                    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                    String fechaEstimada = fechaModificada.format(formato);
                    String message =
                        "&7Días del mundo:&b " + this.days + "\n" +
                        "&7Tiempo transcurrido en la vida real:&b " + hours + " horas\n" +
                        "&7El mundo se ha iniciado el:&b " + fechaEstimada;
                    MutableComponent textColored = FormatterUtils.parseAndFormatText(message);
                    context.getSource().sendFeedback(textColored);
                    return 1;
                })
            );
        });
    }
}
