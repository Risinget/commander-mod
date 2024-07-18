package risinget.commander;

import org.lwjgl.glfw.GLFW;

// import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
// import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
// import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
// import net.minecraft.client.MinecraftClient;


import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;

public class CommanderClient implements ClientModInitializer {



	@Override
	public void onInitializeClient() {

		SmallCapsConverter smallCapsConverter = new SmallCapsConverter();
		smallCapsConverter.converter();

		new EmojisCommand();

		CoordsCopyShortcut  coordsShortcut = new CoordsCopyShortcut();

		new Factorial();

		PlayerAutoDisconnect autoDisc = new PlayerAutoDisconnect();

		new WordsList();

		new ColorsCommand();
		new TestTextColorsCommand();


		// ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
		// 	dispatcher.register(ClientCommandManager.literal("print").executes(context -> {
		// 		context.getSource().sendFeedback(Text.literal("tu mensaje es :"));
		// 		return 1;
		// 	}));
		// });

		// ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
		// 	dispatcher.register(ClientCommandManager.literal("print")
		// 			.then(ClientCommandManager.argument("mensaje", StringArgumentType.string())
		// 					.executes(context -> {
		// 						// Obtener el argumento
		// 						String mensaje = StringArgumentType.getString(context, "mensaje");
		// 						// Usar el argumento en el mensaje
		// 						String playerPos = context.getSource().getPlayer().getBlockPos().toString();
		// 						context.getSource()
		// 								.sendFeedback(Text.literal("tu mensaje es: " + mensaje + " y tu posicion: " + playerPos));
		// 						return 1;
		// 					})));
		// });



		// // Inicialización del KeyBinding
		// KeyBinding openGUI = KeyBindingHelper.registerKeyBinding(new KeyBinding(
		// 		"OpenGUI", // La traducción del nombre de la tecla en el archivo de idiomas
		// 		InputUtil.Type.KEYSYM,
		// 		GLFW.GLFW_KEY_F10, // La tecla F9
		// 		"Commander" // La categoría de la tecla en la configuración de controles
		// ));

		// ClientTickEvents.END_CLIENT_TICK.register(client -> {
		// 	if (openGUI.wasPressed()) {
				
		// 		MinecraftClient.getInstance().setScreen(
		// 				new CustomScreen(Text.empty()));
		// 	}
		// });




		// Inicialización del KeyBinding
		KeyBinding openGUIYacl = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"Open Commander Config", // Asegúrate de que esto coincida con tu archivo de idiomas
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_F8, // La tecla F8
				"Commander" // Asegúrate de que esto coincida con tu archivo de idiomas
		));


		// Registrar el evento de tick del cliente
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (openGUIYacl.wasPressed()) {
				// Configurar y mostrar la pantalla de configuración
				client.setScreen(YetAnotherConfigLib.createBuilder()
					.title(Text.of("Used for narration. Could be used to render a title in the future."))
					.category(ConfigCategory.createBuilder()
							.name(Text.of("Commander Config"))
							.tooltip(Text.of("Configuraciones de Commander"))
							.group(OptionGroup.createBuilder()
									.name(Text.of("Player Auto Disconnect Config"))
									.description(OptionDescription.of(Text.of("Configuración de desconexión automática. Esta configuración se aplica a la desconexión automática.")))
									.option(Option.<Boolean>createBuilder()
											.name(Text.of("Activar desconexión automática"))
											.description(OptionDescription.of(Text.of("Habilitar o deshabilitar la desconexión automática.")))
											.binding(autoDisc.getOn(), () -> autoDisc.getOn(), newVal -> autoDisc.setOn(newVal))
											.controller(TickBoxControllerBuilder::create)
											.build())
									.option(Option.<String>createBuilder()
											.name(Text.of("Desconectarse al tener menor vida que"))
											.description(OptionDescription.of(Text.of(
													"Determina la cantidad de vida que el jugador debe tener para desconectarse automáticamente.")))
											.binding( String.valueOf(autoDisc.getHealthMin()),
																		() -> String.valueOf(autoDisc.getHealthMin()) ,
													 newVal -> { autoDisc.setHealthMin(Integer.parseInt(newVal));; })
											.controller(StringControllerBuilder::create)
											.build())

									.build())
							// .group(OptionGroup.createBuilder()
							// 		.name(Text.of("Name of the group"))
							// 		.description(OptionDescription.of(Text.of("This text will appear when you hover over the name or focus on the collapse button with Tab.")))
							// 		.option(Option.<String>createBuilder()
							// 				.name(Text.of("String Option"))
							// 				.description(OptionDescription.of(Text.of("This text will appear as a tooltip when you hover over the option.")))
							// 				.binding("Default value", () -> "Default value", newVal -> {})
							// 				.controller(StringControllerBuilder::create)
							// 				.build())
							// 		.build())
							.group(OptionGroup.createBuilder()
									.name(Text.of("Coords Copy "))
									.description(OptionDescription.of(Text.of(
											"Configuración de la copia de coordenadas. Esta configuración se aplica a la copia de coordenadas.")))
									.option(Option.<String>createBuilder()
											.name(Text.of("Coords Format"))
											.description(OptionDescription.of(Text.of(
													"Hay tres variables X, Y y Z que se pueden usar en el formato de coordenadas. Estas variables se reemplazarán por las coordenadas del jugador.")))
											.binding(coordsShortcut.getCoordsFormat(), () -> coordsShortcut
														.getCoordsFormat() , newVal -> {
															coordsShortcut.setCoordsFormat(newVal);;
															})
											.controller(StringControllerBuilder::create)
											.build())
									.build())
							.build())
					.build()
					.generateScreen(client.currentScreen));
			}
		});


		

	}


}