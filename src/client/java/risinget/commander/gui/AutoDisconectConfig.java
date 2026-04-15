package risinget.commander.gui;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import net.minecraft.network.chat.Component;
import risinget.commander.config.ConfigCommander;
import risinget.commander.gui.options.BooleanOption;

public class AutoDisconectConfig {

    public static ConfigCategory category(){
        return ConfigCategory.createBuilder()
            .name(Component.nullToEmpty("Autodisconnect"))
            .tooltip(Component.nullToEmpty("Configuraciones de Autodisconnect"))
            .group(OptionGroup.createBuilder()
                    .name(Component.nullToEmpty("Player Auto Disconnect Config"))
                    .description(OptionDescription.of(Component.nullToEmpty("Configuración de desconexión automática. Esta configuración se aplica a la desconexión automática.")))
                    .option(Option.<Boolean>createBuilder()
                            .name(Component.nullToEmpty("Activar desconexión automática"))
                            .description(OptionDescription.of(Component.nullToEmpty("Habilitar o deshabilitar la desconexión automática.")))
                            .binding(ConfigCommander.DEFAULT_ACTIVAR_DESCONEXION, ConfigCommander::getOn, ConfigCommander::setOn)
                            .controller(booleanOption -> BooleanControllerBuilder.create(booleanOption)
                                .formatValue(BooleanOption::isEnabled))
                            .build())
                    .option(Option.<Boolean>createBuilder()
                            .name(Component.nullToEmpty("Tomar screenshot antes de desconectarse"))
                            .description(OptionDescription.of(Component.nullToEmpty("Habilitar o deshabilitar una screenshot automática antes de desconectarse")))
                            .binding(ConfigCommander.DEFAULT_ENABLE_SCREENSHOT_BEFORE_DISCONNECT,
                                    ConfigCommander::getEnableSsBeforeDisc,
                                    ConfigCommander::setEnableSsBeforeDisc)
                            .controller(booleanOption -> BooleanControllerBuilder.create(booleanOption)
                                    .formatValue(BooleanOption::isEnabled))
                            .build())
                    .option(Option.<Integer>createBuilder()
                            .name(Component.nullToEmpty("Desconectarse al tener menor vida que"))
                            .description(OptionDescription.of(Component.nullToEmpty(
                                    "Determina la cantidad de vida que el jugador debe tener para desconectarse automáticamente.")))
                            .binding(ConfigCommander.DEFAULT_DESCONECTAR_MENOR_VIDA_QUE,
                                    ConfigCommander::getHealthMin,
                                    ConfigCommander::setHealthMin)
                            .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                    .range(1, 20)
                                    .step(1)
                                    .formatValue(val -> Component.literal(val+"")))
                            .build())
                    .build())
            .build();
    }


}
