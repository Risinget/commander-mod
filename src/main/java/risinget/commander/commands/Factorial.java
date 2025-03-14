package risinget.commander.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import risinget.commander.utils.Formatter;
import risinget.commander.utils.Prefix;

public class Factorial {
    
    public Factorial(){
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("factorial")
            .then(ClientCommandManager.argument("integer", IntegerArgumentType.integer())
                .executes(context -> {
                    int n = IntegerArgumentType.getInteger(context,"integer");
                    int finalFactorial = factorial(n);
                    context.getSource().sendFeedback(Formatter.parseAndFormatText(Prefix.COMMANDER+" &7Factorial de " + n + " es: &6" + finalFactorial));
                    return 1;
                })
            )
        ));
    }
    public static int factorial(int n) {
        if (n == 0) { return 1; }
        return n * factorial(n - 1);
    }
}
