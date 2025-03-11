package risinget.commander;

import dev.isxander.yacl3.gui.YACLScreen;
import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import risinget.commander.commands.*;
import risinget.commander.config.ConfigCommander;
import risinget.commander.events.AutoDisconnect;
import risinget.commander.events.HistoryChat;
import risinget.commander.core.CoordsConverter;
import risinget.commander.gui.ScreenGUI;
import risinget.commander.keybinds.KeyHandler;
public class Commander implements ClientModInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(Commander.class);
	private boolean isConfigScreenOpen = false;

	@Override
	public void onInitializeClient() {
		KeyHandler.registerKeybinds(); // Registra los keybinds en el menú de controles

		LOGGER.info("BVXD");
		SmallCapsConverter smallCapsConverter = new SmallCapsConverter();
		smallCapsConverter.converter();

		new EmojisCommand();

		new CopyCoords();

		new Factorial();

		new AutoDisconnect();

		new WordsList();

		new ColorsCommand();
		new TestTextColorsCommand();

		new DaysToTime();

		new CoordsConverter();

		new Commands();
		new GeminiAICommand();
		new CloudSS();

		new HistoryChat();

		// Inicialización del KeyBinding
		KeyBinding openGUIYacl = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"Open Commander Config", // Asegúrate de que esto coincida con tu archivo de idiomas
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_F8, // La tecla F8
				"Commander" // Asegúrate de que esto coincida con tu archivo de idiomas
		));

		final boolean isConfigScreenOpen = false;
		// Registrar el evento de tick del cliente
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (openGUIYacl.wasPressed()) {
				this.isConfigScreenOpen = true;
		        ConfigCommander.HANDLER.load();
				client.setScreen(ScreenGUI.yacl(client));
			}
			if (this.isConfigScreenOpen && !(client.currentScreen instanceof YACLScreen)) {
				// Aquí detectas cuando la pantalla se cierra
				System.out.println("La pantalla de configuración fue cerrada");
				this.isConfigScreenOpen = false;
				ConfigCommander.HANDLER.save();
			}
		});
	}
}