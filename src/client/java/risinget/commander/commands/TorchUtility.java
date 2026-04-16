package risinget.commander.commands;

import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import risinget.commander.config.ConfigCommander;

public class TorchUtility {

    public static boolean hasEnchantedTorch = false;
    public TorchUtility(){
        ClientTickEvents.END_CLIENT_TICK.register(TorchUtility::torch);
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommands.literal("torch")
                .executes((context)->{
                   torch(context.getSource().getClient());
                   return 1;
                })
            );
        });
    }

    public static void torch(Minecraft client) {
        if(!ConfigCommander.getEnabledTorchCreative()){return;}
        if (client.player != null && client.player.getAbilities().instabuild) {
            ItemStack torchStack = new ItemStack(Items.TORCH, 64);
            client.player.getInventory().setItem(client.player.getInventory().getSelectedSlot(), torchStack);
            EnchantUtilityCommand.enchant(torchStack, 127); // Asegúrate de que el método acepte ItemStack
            hasEnchantedTorch = true;
            ConfigCommander.setEnabledTorchCreative(false);
        }
    }
}
