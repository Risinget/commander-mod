package risinget.commander.gui;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import risinget.commander.commands.CoordsCopyShortcut;
import risinget.commander.config.ConfigCommander;

public class CopyCoords {

    public ConfigCategory category(MinecraftClient client){
        return ConfigCategory.createBuilder()
            .name(Text.of("Coords Copy"))
            .tooltip(Text.of("Configuraciones para Coords Copy"))
            .group(OptionGroup.createBuilder()
                    .name(Text.of("Coords Copy "))
                    .description(OptionDescription.of(Text.of(
                            "Configuración de la copia de coordenadas. Esta configuración se aplica a la copia de coordenadas.")))
                    .option(Option.<String>createBuilder()
                            .name(Text.of("Coords Format"))
                            .description(OptionDescription.of(Text.of(
                                    "Hay tres variables X, Y y Z que se pueden usar en el formato de coordenadas. Estas variables se reemplazarán por las coordenadas del jugador.")))
                            .binding(ConfigCommander.getCoordsFormat(),
                                    () -> ConfigCommander.getCoordsFormat(),
                                    newVal -> {
                                        ConfigCommander.setCoordsFormat(newVal);
                                        CoordsCopyShortcut.setCoordsFormat(newVal);
                                    })
                            .controller(StringControllerBuilder::create)
                            .build())
                    .build())
            .build();
    }
}
