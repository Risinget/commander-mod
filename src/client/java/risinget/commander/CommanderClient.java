package risinget.commander;
import org.lwjgl.glfw.GLFW;

import com.mojang.brigadier.arguments.StringArgumentType;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
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

		HistoryChat historyChat = new HistoryChat();

		SmallCapsConverter smallCapsConverter = new SmallCapsConverter();
		smallCapsConverter.converter();

		EmojisCommand emojis = new EmojisCommand();

		CoordsCopyShortcut coordsCopyShortcut = new CoordsCopyShortcut();

		Factorial factorial = new Factorial();

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

	}


	

}