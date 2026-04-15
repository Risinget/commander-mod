
package risinget.commander.commands;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.slf4j.Logger;
import risinget.commander.Commander;

public final class EnchantUtilityCommand
{
    private static final Logger LOGGER = Commander.getLogger(EnchantUtilityCommand.class);
    private static final Minecraft client = Minecraft.getInstance();
    public EnchantUtilityCommand(){
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("enchantBruh")
                .executes((context)->{

                    if(!context.getSource().getClient().player.getAbilities().instabuild){
                        LOGGER.info("No es posible encantar, ponte creativo");
                        return 1;
                    }
                    enchant(getHeldItem(context.getSource().getClient()), 127);
                    context.getSource().sendFeedback(Component.literal("Encantado con 127 niveles"));

                    return 1;
                })
            );
        });
    }

    public static ItemStack getHeldItem(Minecraft client)
    {
        ItemStack stack = client.player.getMainHandItem();

        if(stack.isEmpty())
            stack = client.player.getOffhandItem();


        return stack;
    }

    public static void enchant(ItemStack stack, int level)
    {
        RegistryAccess drm = client.level.registryAccess();
        Registry<Enchantment> registry = drm.lookupOrThrow(Registries.ENCHANTMENT);

        for(Holder<Enchantment> entry : registry.asHolderIdMap())
        {
            // Skip curses
            if(entry.is(EnchantmentTags.CURSE))
                continue;

            // Skip Silk Touch so it doesn't remove Fortune
            if(entry.unwrapKey().orElse(null) == Enchantments.SILK_TOUCH)
                continue;

            // Skip Silk Touch so it doesn't remove Fortune
            if(entry.unwrapKey().orElse(null) == Enchantments.WIND_BURST)
                continue;

            // Limit Quick Charge to level 5 so it doesn't break
            if(entry.unwrapKey().orElse(null) == Enchantments.QUICK_CHARGE)
            {
                stack.enchant(entry, Math.min(level, 5));
                continue;
            }

            stack.enchant(entry, level);
        }
    }

}