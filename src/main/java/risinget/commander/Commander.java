package risinget.commander;

import net.fabricmc.api.ModInitializer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

//

//
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.brigadier.context.CommandContext;

public class Commander implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("commander");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

    }

    
}