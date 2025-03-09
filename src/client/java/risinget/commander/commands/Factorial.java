package risinget.commander;

import com.mojang.brigadier.arguments.StringArgumentType;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

public class Factorial {
    
    // simple factorial
    public Factorial(){
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("factorial")
				.then(ClientCommandManager.argument("integer", StringArgumentType.string())
					.executes(context -> {
						// Obtener el argumento
						String integer = StringArgumentType.getString(context, "integer");
						// Usar el argumento en el mensaje
						String finalFactorial = String.valueOf(factorial(Integer.parseInt(integer)));
						context.getSource()
								.sendFeedback(Text
										.literal("Factorial de " + integer + " es: " + finalFactorial));
						return 1;
					})));
		});
    }

    public static int factorial(int n) {
        if (n == 0) {
            return 1;
        }
        return n * factorial(n - 1);
    }
}
