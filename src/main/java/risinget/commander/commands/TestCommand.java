package risinget.commander.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.text.Text;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.Collection;

public class TestCommand {
    public TestCommand() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("prueba")
                .then(ClientCommandManager.argument("text", StringArgumentType.string())
                    .executes((context)->{
                        MinecraftClient client = context.getSource().getClient();
                        ClientPlayerEntity player = client.player;

                        if(player == null) return 0;

                        Scoreboard scoreboard = client.getNetworkHandler().getScoreboard();

                        // Iterar a través de todos los objetivos del scoreboard
                        for(ScoreboardObjective objective : scoreboard.getObjectives()) {
                            // Obtener el nombre del objetivo
                            String objectiveName = objective.getName();
                            // Crear texto para mostrar
                            Text objectiveText = Text.literal("Objective: " + objective.getDisplayName());
                            System.out.println(objectiveText.toString());

                            // Obtener todas las entradas del scoreboard para este objetivo
                            Collection<ScoreboardEntry> entries = scoreboard.getScoreboardEntries(objective);

                            // Iterar a través de cada entrada
                            for(ScoreboardEntry entry : entries) {
                                // entry.display() siempre será nulo no se pq aún así hay contenido no se a que metodo se refiere...
                                String playerName = entry.owner();
                                int score = entry.value();
                                Text scoreText = Text.literal("  " + playerName + ": " + score);
                                context.getSource().sendFeedback(scoreText);
                                System.out.println(entry.formatted(entry.numberFormatOverride()));

                            }
                        }

                        return 1;
                    })
                ));
        });
    }
}