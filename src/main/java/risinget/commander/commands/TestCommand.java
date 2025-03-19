package risinget.commander.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import risinget.commander.utils.HexUtil;

public class TestCommand {
    public TestCommand() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("prueba")
                .then(ClientCommandManager.argument("text", StringArgumentType.string())
                    .executes((context)->{
                        String hexColor = "#3AA9FF";
                        String minecraftColor = HexUtil.hexToMinecraftColor(hexColor);
                        System.out.println("Color HEX: " + hexColor);
                        System.out.println("Código de Minecraft más cercano: " + minecraftColor);
                        return 1;
                    })
                ));
        });
    }
}