package risinget.commander.gui;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.text.Text;
import risinget.commander.config.ConfigCommander;

public class CopyCoordsConfig {

    public static ConfigCategory category(){
        return ConfigCategory.createBuilder()
            .name(Text.of("Coords Copy"))
            .tooltip(Text.of("Configuraciones para Coords Copy"))
            .group(OptionGroup.createBuilder()
                    .name(Text.of("Coords Copy "))
                    .description(OptionDescription.of(Text.of(
                            "Configuración de la copia de coordenadas. Esta configuración se aplica a la copia de coordenadas.")))
                    .option(Option.<String>createBuilder()
                            .name(Text.of("Formato Coords de Posición"))
                            .description(OptionDescription.of(Text.of(
                                    "Hay tres variables X, Y y Z que se pueden usar en el formato de coordenadas. Estas variables se reemplazarán por las coordenadas del jugador.")))
                            .binding(ConfigCommander.DEFAULT_COORDS_FORMAT,
                                    ConfigCommander::getPosCoordsFormat,
                                    ConfigCommander::setPosCoordsFormat)
                            .controller(StringControllerBuilder::create)
                            .build())
                    .option(Option.<String>createBuilder()
                            .name(Text.of("Formato de Coords donde miras"))
                            .description(OptionDescription.of(Text.of(
                                    "Hay tres variables X, Y y Z que se pueden usar en el formato de coordenadas. Estas variables se reemplazarán por las coordenadas del jugador.")))
                            .binding(ConfigCommander.DEFAULT_COORDS_FORMAT,
                                    ConfigCommander::getPosCoordsViewing,
                                    ConfigCommander::setPosCoordsViewing)
                            .controller(StringControllerBuilder::create)
                            .build())

                    .option(Option.<String>createBuilder()
                            .name(Text.of("Format de Coords después de Convertir al overworld"))
                            .description(OptionDescription.of(Text.of(
                                    "Hay tres variables X, Y y Z que se pueden usar en el formato de coordenadas. Estas variables se reemplazarán por las coordenadas del jugador.")))
                            .binding(ConfigCommander.DEFAULT_COORDS_FORMAT,
                                    ConfigCommander::getFormatCoordsOverworld,
                                    ConfigCommander::setFormatCoordsOverworld)
                            .controller(StringControllerBuilder::create)
                            .build())

                    .option(Option.<String>createBuilder()
                            .name(Text.of("Format de Coords después de Convertir al nether"))
                            .description(OptionDescription.of(Text.of(
                                    "Hay tres variables X, Y y Z que se pueden usar en el formato de coordenadas. Estas variables se reemplazarán por las coordenadas del jugador.")))
                            .binding(ConfigCommander.DEFAULT_COORDS_FORMAT,
                                    ConfigCommander::getFormatCoordsNether,
                                    ConfigCommander::setFormatCoordsOverworld)
                            .controller(StringControllerBuilder::create)
                            .build())

                    .build())
            .build();
    }
}
