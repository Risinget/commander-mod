
package risinget.commander.commands;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import risinget.commander.Commander;

public final class EnchantUtilityCommand
{
    private static final Logger LOGGER = Commander.getLogger(EnchantUtilityCommand.class);
    private static final MinecraftClient client = MinecraftClient.getInstance();
    public EnchantUtilityCommand(){
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("enchantBruh")
                .executes((context)->{

                    if(!context.getSource().getClient().player.getAbilities().creativeMode){
                        LOGGER.info("No es posible encantar, ponte creativo");
                        return 1;
                    }
                    enchant(getHeldItem(context.getSource().getClient()), 127);
                    context.getSource().sendFeedback(Text.literal("Encantado con 127 niveles"));

                    return 1;
                })
            );
        });
    }

    public static ItemStack getHeldItem(MinecraftClient client)
    {
        ItemStack stack = client.player.getMainHandStack();

        if(stack.isEmpty())
            stack = client.player.getOffHandStack();


        return stack;
    }

    public static void enchant(ItemStack stack, int level)
    {
        DynamicRegistryManager drm = client.world.getRegistryManager();
        Registry<Enchantment> registry = drm.getOrThrow(RegistryKeys.ENCHANTMENT);

        for(RegistryEntry<Enchantment> entry : registry.getIndexedEntries())
        {
            // Skip curses
            if(entry.isIn(EnchantmentTags.CURSE))
                continue;

            // Skip Silk Touch so it doesn't remove Fortune
            if(entry.getKey().orElse(null) == Enchantments.SILK_TOUCH)
                continue;

            // Skip Silk Touch so it doesn't remove Fortune
            if(entry.getKey().orElse(null) == Enchantments.WIND_BURST)
                continue;

            // Limit Quick Charge to level 5 so it doesn't break
            if(entry.getKey().orElse(null) == Enchantments.QUICK_CHARGE)
            {
                stack.addEnchantment(entry, Math.min(level, 5));
                continue;
            }

            stack.addEnchantment(entry, level);
        }
    }

}