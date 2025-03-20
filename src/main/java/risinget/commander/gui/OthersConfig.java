package risinget.commander.gui;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.text.Text;
import risinget.commander.config.ConfigCommander;
import risinget.commander.gui.options.BooleanOption;

public class OthersConfig {

    public static ConfigCategory category(){
        return ConfigCategory.createBuilder()
                .name(Text.of("Misc."))
                .group(OptionGroup.createBuilder()
                        .name(Text.of("Torch"))
                        .description(OptionDescription.of(Text.of(
                                "Gives you an torch with special enchants")))
                        .option(Option.<String>createBuilder()
                                .name(Text.of("Name for the Item"))
                                .description(OptionDescription.of(Text.of(
                                        "The name will be set in the item once given you")))
                                .binding(
                                        ConfigCommander.DEFAULT_CUSTOM_NAME,
                                        ConfigCommander::getCustomName,
                                        ConfigCommander::setCustomName)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Enabled detect when Creative"))
                                .description(OptionDescription.of(Text.of(
                                        "Once enabled this, automatically detect when you are in creative, if is true, you will have an torch with special enchants")))
                                .binding(
                                        ConfigCommander.DEFAULT_ENABLED_TORCH_CREATIVE,
                                        ConfigCommander::getEnabledTorchCreative,
                                        ConfigCommander::setEnabledTorchCreative)
                                .controller(bln -> BooleanControllerBuilder.create(bln).formatValue(BooleanOption::isEnabled))
                                .build())
                        .build())
                .build();
    }
}
