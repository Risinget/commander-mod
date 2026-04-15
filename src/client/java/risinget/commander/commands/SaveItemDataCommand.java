package risinget.commander.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import org.slf4j.Logger;
import risinget.commander.Commander;
import risinget.commander.utils.FileUtils;
import risinget.commander.utils.FormatterUtils;

import java.io.File;
import java.text.Format;
import java.util.List;

public class SaveItemDataCommand {

    private static final Logger LOGGER = Commander.getLogger(SaveItemDataCommand.class);
    private static final String SERVER_DIR = "config/commander/servers/";

    public SaveItemDataCommand() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("saveItem")
                    .then(ClientCommandManager.argument("texto", StringArgumentType.string())
                            .executes((context)->{

//                                String texto = StringArgumentType.getString(context, "texto");
////                                context.getSource().getPlayer().getMainHandStack().getItem().appendTooltip();
//                                MinecraftClient client = context.getSource().getClient();
//                                ItemStack stack = client.player.getMainHandStack();
//
//                                if (!stack.isEmpty()) {
//                                    // Obtén los tooltips del ítem
//                                    List<Text> tooltips = stack.getTooltip(Item.TooltipContext.DEFAULT, client.player, TooltipType.ADVANCED);
//
//
//                                    tooltips.add(Text.literal("prueba añadido"));
//                                    // Imprimir cada tooltip en la consola
//                                    // Modificar el itemstack para que tenga el tooltip actualizado
//                                    stack.getItem().appendTooltip(stack,Item.TooltipContext.DEFAULT, tooltips,TooltipType.ADVANCED);
//
//                                    for (Text tooltip : tooltips) {
//                                        System.out.println(tooltip.getString()); // Imprime el texto del tooltip
//                                    }
//                                } else {
//                                    System.out.println("No tienes ningún ítem en la mano.");
//                                }

                                String texto = StringArgumentType.getString(context, "texto");
                                Minecraft client = context.getSource().getClient();
                                if (client.player != null && client.player.getMainHandItem() != null) {
                                    DataComponentMap components = client.player.getMainHandItem().getItem().getDefaultInstance().getComponents();
                                    ItemStack stack = client.player.getMainHandItem();

                                    components.forEach((c) -> {;
                                        LOGGER.info("Component Type: {}", c.toString());
                                        LOGGER.info("Component Data: {}", c.value().toString());
                                    });

                                    List<Component> loreList = List.of(
                                            FormatterUtils.parseAndFormatColor("&b&k!!&r &cDDLS ON TOP &b&k!!")
                                    );
                                    setLore(loreList, stack);
                                    setCustomName(FormatterUtils.parseAndFormatText("&b&k!!&r &cDDLS ON TOP &b&k!!"), stack);

                                    String itemName = stack.getCustomName().getString();

                                    if(client.isLocalServer()){
                                        String path = "config/commander/servers/singleplayer/singleplayer_"+ itemName + ".txt";
                                        FileUtils.saveText(FormatterUtils.getColoredText(stack.getCustomName()), new File(path));
                                        List<Component> lines = getLore(stack);
                                        for (Component text : lines) {
                                            FileUtils.saveText(FormatterUtils.getColoredText(text), new File(path));
                                        }
                                    }

                                    if (client.getConnection() != null && client.getConnection().getServerData() != null) {
                                        String serverName = client.getConnection().getServerData().ip.replace(":", ".");
                                        String path = "config/commander/servers/" + serverName + "/" + serverName + "_" + itemName + ".txt";
                                        FileUtils.saveText(FormatterUtils.getColoredText(stack.getCustomName()), new File(path));
                                        List<Component> lines = getLore(stack);
                                        for (Component text : lines) {
                                            FileUtils.saveText(FormatterUtils.getColoredText(text), new File(path));
                                        }
                                    }
                                }
                                return 1;
                            })
                    ));
        });

    }

    private static Component getCustomName(ItemStack itemStack){
        return itemStack.getCustomName();
    }

    private static void setCustomName(Component text, ItemStack itemStack){
        itemStack.set(DataComponents.CUSTOM_NAME, text);
    }

    private static void setLore(List<Component> textList, ItemStack itemStack){
        ItemLore loreList = new ItemLore(textList);
        itemStack.set(DataComponents.LORE, loreList);
    }

    private static List<Component> getLore(ItemStack itemStack){
        List<Component> lines = null;
        if(itemStack.get(DataComponents.LORE) != null){
            lines = itemStack.get(DataComponents.LORE).lines();
        }
        return lines;
    }


    private void printItemDetails(FabricClientCommandSource source, ItemStack itemStack) {
//        // Nombre del ítem
//        Text itemName = itemStack.getName();
//        source.sendFeedback(Text.literal("Nombre: ").formatted(Formatting.GOLD).append(itemName));
//
//        // Tipo de ítem (ID)
////        String itemId = Registry.ITEM.getId(itemStack.getItem()).toString();
////        source.sendFeedback(Text.literal("Tipo: ").formatted(Formatting.YELLOW).append(itemId));
//
//        // Cantidad
//        int count = itemStack.getCount();
//        source.sendFeedback(Text.literal("Cantidad: ").formatted(Formatting.GREEN).append(String.valueOf(count)));
//
//        // Durabilidad (si es un ítem que tiene durabilidad)
//        if (itemStack.isDamageable()) {
//            int maxDurability = itemStack.getMaxDamage();
//            int currentDurability = maxDurability - itemStack.getDamage();
//            source.sendFeedback(Text.literal("Durabilidad: ").formatted(Formatting.BLUE)
//                    .append(currentDurability + " / " + maxDurability));
//        }
//
//
//        // Obtener o crear el NBT del ítem
//        NbtElement nbt1 = itemStack.toNbt(source.getRegistryManager());
//
//        Item.TooltipContext context = Item.TooltipContext.DEFAULT;// Donde `player` es un PlayerEntity
//        List<Text> tooltip = itemStack.getTooltip(
//                context,                  // Item.TooltipContext
//                source.getPlayer(),      // PlayerEntity (puede ser null)
//                TooltipType.BASIC        // TooltipType
//        );
//        tooltip.add(Text.literal("xdd"));
//        tooltip.
//
//        itemStack.streamTags().forEach((tags)->{
//            LOGGER.info(tags.getName().toString());
//        });
//
//        // Encantamientos
//        if (itemStack.hasEnchantments()) {
//            source.sendFeedback(Text.literal("Encantamientos:").formatted(Formatting.LIGHT_PURPLE));
//            for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemStack.getEnchantments().getEnchantmentEntries()) {
//                RegistryEntry<Enchantment> enchantment = entry.getKey();
//                int level = entry.getIntValue();
//                if(enchantment.getKey().isPresent()){
//                    source.sendFeedback(Text.literal("Encantamiento: " + enchantment.getKey().get().getValue() + ", Nivel: " + level));
//                }
//            }
//        } else {
//            source.sendFeedback(Text.literal("Encantamientos: Ninguno").formatted(Formatting.GRAY));
//        }
//
//        // NBT Data (datos adicionales del ítem)
//        NbtElement nbt = itemStack.toNbt(source.getRegistryManager());
//        if (nbt != null) {
//            source.sendFeedback(Text.literal("NBT Data: ").formatted(Formatting.DARK_AQUA).append(nbt.toString()));
//        } else {
//            source.sendFeedback(Text.literal("NBT Data: Ninguno").formatted(Formatting.GRAY));
//        }
    }
}