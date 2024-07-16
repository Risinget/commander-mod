package risinget.commander;
import org.lwjgl.glfw.GLFW;

import com.mojang.brigadier.arguments.StringArgumentType;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import net.minecraft.client.MinecraftClient;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;

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
			dispatcher.register(ClientCommandManager.literal("emojis").executes(context -> {
				String[] emojis = { "☄", "⭐" };

				for (String emoji : emojis) {
					MutableText emojiText = Text.literal(emoji + " ")
							.styled(style -> style
									.withColor(Formatting.RED)
									.withClickEvent(
											new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/copyemoji " + emoji))
									.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
											Text.literal("Click para copiar!"))));

					context.getSource().sendFeedback(emojiText);
				}

				return 1;
			}));

			dispatcher.register(ClientCommandManager.literal("copyemoji")
					.then(ClientCommandManager.argument("emoji", StringArgumentType.greedyString())
							.executes(context -> {
								String emoji = StringArgumentType.getString(context, "emoji");
								MinecraftClient.getInstance().keyboard.setClipboard(emoji); // Usa el método de
																							// Minecraft para copiar al
																							// portapapeles
								context.getSource().sendFeedback(Text.literal("Emoji copiado al portapapeles: " + emoji)
										.formatted(Formatting.GREEN));
								return 1;
							})));
		});

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("emojis2").executes(context -> {
				context.getSource().sendFeedback(Text.literal("ᴘʀᴇᴠɪᴇᴡ ᴛᴇxᴛ").formatted(Formatting.RED));
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
        KeyBinding copyPosKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Copiar posición", // La traducción del nombre de la tecla en el archivo de idiomas
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F9, // La tecla F9
                "Commander" // La categoría de la tecla en la configuración de controles
        ));

        // Registro del evento para escuchar el tick del cliente
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (copyPosKeyBinding.wasPressed()) {
                if (client.player != null) {
					// X: 0, Y: 0, Z: 0
					int posX = client.player.getBlockPos().getX();
					int posY = client.player.getBlockPos().getY();
					int posZ = client.player.getBlockPos().getZ();
					String pos = "X: " + posX + ", Y: " + posY + ", Z: " + posZ;

					client.keyboard.setClipboard(pos); // Use Minecraft's method to set clipboard content
				}
            }
        });
	}


}