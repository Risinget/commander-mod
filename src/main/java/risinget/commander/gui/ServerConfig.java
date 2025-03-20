package risinget.commander.gui;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import net.minecraft.text.Text;
import risinget.commander.config.ConfigCommander;
import risinget.commander.core.ServerSaver;
import risinget.commander.gui.options.BooleanOption;

public class ServerConfig {

    public static ConfigCategory category(){
        return ConfigCategory.createBuilder()
                .name(Text.of("Server"))
                .group(OptionGroup.createBuilder()
                        .name(Text.of("Server configurations"))
                        .description(OptionDescription.of(Text.of(
                                "Aquí configura, si desea capturar el historial del chat, guardar información actual del servidor, etc")))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Enable History Chat"))
                                .description(OptionDescription.of(Text.of(
                                        "guarda el motd del servidor actual")))
                                .binding(
                                        ConfigCommander.DEFAULT_ENABLE_HISTORY_CHAT,
                                        ConfigCommander::getEnableHistoryChat,
                                        ConfigCommander::setEnableHistoryChat
                                )
                                .controller(bln -> BooleanControllerBuilder.create(bln).formatValue(BooleanOption::isEnabled))
                                .build())
                        .option(ButtonOption.createBuilder()
                                .name(Text.of("Save Icon Server"))
                                .description(OptionDescription.of(Text.of(
                                        "guarda el icono del servidor actual")))
                                .action((yacl,btn)-> ServerSaver.saveIcon())
                                .build())
                        .option(ButtonOption.createBuilder()
                                .name(Text.of("Save MOTD"))
                                .description(OptionDescription.of(Text.of(
                                        "guarda el motd del servidor actual")))
                                .action((yacl,btn)-> ServerSaver.saveMotd())
                                .build())
                        .option(ButtonOption.createBuilder()
                                .name(Text.of("Save Tablist"))
                                .description(OptionDescription.of(Text.of(
                                        "guarda el tablist del servidor actual")))
                                .action((yacl,btn)-> ServerSaver.saveTabList())
                                .build())
                        .option(ButtonOption.createBuilder()
                                .name(Text.literal("Open Folder Server"))
                                .description(OptionDescription.of(Text.literal(
                                        "Acá se almacena información como el icono SVG, MOTD e historial del chat con colores y sin colores")))
                                .action((yaclScreen, btn)-> ServerSaver.openFolderCurrentServer())
                                .build())
                        .build())
                .build();
    }
}
