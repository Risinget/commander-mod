package risinget.commander;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class CommanderClient implements ClientModInitializer {
	@Override

	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("clienttater").executes(context -> {
				context.getSource().sendFeedback(Text.literal("Called /clienttater with no arguments."));
				return 1;
			}));
		});


		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("prueba_mundo").executes(context -> {
				context.getSource().sendFeedback(Text.literal("hola mundo!!!").formatted(Formatting.RED));
				return 1;
			}));
		});
	}
}