package risinget.commander.gui;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import net.minecraft.network.chat.Component;
import risinget.commander.config.ConfigCommander;
import risinget.commander.core.ServerSaver;
import risinget.commander.gui.options.BooleanOption;

public class ServerConfig {

    public static ConfigCategory category(){
        return ConfigCategory.createBuilder()
                .name(Component.nullToEmpty("Server"))
                .group(OptionGroup.createBuilder()
                        .name(Component.nullToEmpty("Server configurations"))
                        .description(OptionDescription.of(Component.nullToEmpty(
                                "Aquí configura, si desea capturar el historial del chat, guardar información actual del servidor, etc")))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.nullToEmpty("Enable History Chat"))
                                .description(OptionDescription.of(Component.nullToEmpty(
                                        "guarda el motd del servidor actual")))
                                .binding(
                                        ConfigCommander.DEFAULT_ENABLE_HISTORY_CHAT,
                                        ConfigCommander::getEnableHistoryChat,
                                        ConfigCommander::setEnableHistoryChat
                                )
                                .controller(bln -> BooleanControllerBuilder.create(bln).formatValue(BooleanOption::isEnabled))
                                .build())
                        .option(ButtonOption.createBuilder()
                                .name(Component.nullToEmpty("Save Icon Server"))
                                .description(OptionDescription.of(Component.nullToEmpty(
                                        "guarda el icono del servidor actual")))
                                .action((yacl,btn)-> ServerSaver.saveIcon())
                                .build())
                        .option(ButtonOption.createBuilder()
                                .name(Component.nullToEmpty("Save MOTD"))
                                .description(OptionDescription.of(Component.nullToEmpty(
                                        "guarda el motd del servidor actual")))
                                .action((yacl,btn)-> ServerSaver.saveMotd())
                                .build())
                        .option(ButtonOption.createBuilder()
                                .name(Component.nullToEmpty("Save Tablist"))
                                .description(OptionDescription.of(Component.nullToEmpty(
                                        "guarda el tablist del servidor actual")))
                                .action((yacl,btn)-> ServerSaver.saveTabList())
                                .build())
                        .option(ButtonOption.createBuilder()
                                .name(Component.literal("Open Folder Server"))
                                .description(OptionDescription.of(Component.literal(
                                        "Acá se almacena información como el icono SVG, MOTD e historial del chat con colores y sin colores")))
                                .action((yaclScreen, btn)-> ServerSaver.openFolderCurrentServer())
                                .build())
                        .build())
                .build();
    }
}
