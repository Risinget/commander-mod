package risinget.commander;

import org.lwjgl.glfw.GLFW;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
public class CommanderClient implements ClientModInitializer {


	@Override

	public void onInitializeClient() {

		SmallCapsConverter smallCapsConverter = new SmallCapsConverter();
		smallCapsConverter.converter();

		new EmojisCommand();

		new CoordsCopyShortcut();

		new Factorial();

		new PlayerAutoDisconnect();


		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("print").executes(context -> {
				context.getSource().sendFeedback(Text.literal("tu mensaje es :"));
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



		// Inicialización del KeyBinding
		KeyBinding openGUI = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"OpenGUI", // La traducción del nombre de la tecla en el archivo de idiomas
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_F10, // La tecla F9
				"Commander" // La categoría de la tecla en la configuración de controles
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (openGUI.wasPressed()) {
				
				MinecraftClient.getInstance().setScreen(
						new CustomScreen(Text.empty()));
			}
		});





	}



}