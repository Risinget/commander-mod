package risinget.commander.commands;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import risinget.commander.commands.EnchantUtilityCommand;
public class TorchUtility {

    public TorchUtility(){

//        ClientTickEvents.END_CLIENT_TICK.register((context)->{
//            torch(context);
//        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("torch")
                .executes((context)->{
                torch(context.getSource().getClient());
                   return 1;
                })
            );
        });
    }

    public static void torch(MinecraftClient client) {

//        if (client.player != null && client.player.getAbilities().creativeMode) {
            // Crear la antorcha
            ItemStack torchStack = new ItemStack(Items.TORCH, 64);

            // Colocar directamente en la mano principal
            client.player.getInventory().setStack(client.player.getInventory().selectedSlot, torchStack);

            // Aplicar encantamiento (si es necesario)
            EnchantUtilityCommand.enchant(torchStack, 127); // Asegúrate de que el método acepte ItemStack

            // Mensaje opcional
//            client.player.networkHandler.sendChatCommand("/gamemode survival");
//        } else {
//
//        }
    }
}
