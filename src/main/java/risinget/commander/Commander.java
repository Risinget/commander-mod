package risinget.commander;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import risinget.commander.commands.*;
import risinget.commander.config.ConfigCommander;
import risinget.commander.events.AutoDisconnect;
import risinget.commander.events.HistoryChat;
import risinget.commander.core.CoordsConverter;
import risinget.commander.keybinds.KeyHandler;
import java.util.Arrays;
import java.util.List;

public class Commander implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger(Commander.class);
	public static Logger getLogger(Class<?> clazz) {
		return LoggerFactory.getLogger(Commander.class.getSimpleName() + " -> " + clazz.getSimpleName());
	}

	@Override
	public void onInitializeClient() {
		// Variables de initialization al entrar a un mundo
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			ConfigCommander.HANDLER.load();
			ConfigCommander.setOn(false);
		});
		List<Runnable> initializers = Arrays.asList(
				KeyHandler::new,
				SmallCapsCommand::new,
				SymbolsCommand::new,
				FactorialCommand::new,
				AutoDisconnect::new,
				WordsListCommand::new,
				ColorsCommand::new,
				TestTextColorsCommand::new,
				DaysToTimeCommand::new,
				CoordsConverter::new,
				ListCommandsCommand::new,
				GeminiAICommand::new,
				TestCommand::new,
				SaveItemDataCommand::new,
				HistoryChat::new,
				EnchantUtilityCommand::new,
				TorchUtility::new
		);

		// Inicializar todas las clases
		initializers.forEach(Runnable::run);
	}
}