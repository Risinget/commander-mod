package risinget.commander;

import com.mojang.brigadier.arguments.StringArgumentType;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class CommanderClient implements ClientModInitializer {
	@Override

	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("print").executes(context -> {
				context.getSource().sendFeedback(Text.literal("tu mensaje es :"));
				return 1;
			}));
		});


		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("prueba_mundo").executes(context -> {
				context.getSource().sendFeedback(Text.literal("hola mundo!!!").formatted(Formatting.RED));
				return 1;
			}));
		});


		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("print")
					.then(ClientCommandManager.argument("mensaje", StringArgumentType.string())
							.executes(context -> {
								// Obtener el argumento
								String mensaje = StringArgumentType.getString(context, "mensaje");
								// Usar el argumento en el mensaje
								String playerPos = context.getSource().getPlayer().getBlockPos().toString();
								context.getSource()
										.sendFeedback(Text.literal("tu mensaje es: " + mensaje + " y tu posicion: " + playerPos));
								return 1;
							})));
		});
	}


}