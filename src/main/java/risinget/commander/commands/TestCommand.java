package risinget.commander.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import net.minecraft.client.MinecraftClient;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class TestCommand {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    public TestCommand(){
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("prueba")
                .then(ClientCommandManager.argument("text", StringArgumentType.string())
                    .executes(context -> {
                        String base64 = "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";
                        return 1;
                    })
                ));
        });
    }
}
