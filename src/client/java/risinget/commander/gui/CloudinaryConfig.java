package risinget.commander.gui;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.network.chat.Component;
import risinget.commander.config.ConfigCommander;
import risinget.commander.gui.options.BooleanOption;

public class CloudinaryConfig {

    public static ConfigCategory category(){
        return ConfigCategory.createBuilder()
                .name(Component.nullToEmpty("Cloudinary Screenshots"))
                .tooltip(Component.nullToEmpty("Configuraciones para screenshots en la nube"))
                .group(OptionGroup.createBuilder()
                        .name(Component.nullToEmpty("Cloudinary Cloud"))
                        .description(OptionDescription.of(Component.nullToEmpty(
                                "Configuración de la API KEY. Esta configuración se aplica a Cloudinary.")))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.nullToEmpty("Upload to Cloud"))
                                .description(OptionDescription.of(Component.nullToEmpty(
                                        "CLOUD NAME de tu Cloudinary. Para obtener información, visite https://console.cloudinary.com/settings/")))
                                .binding(
                                        ConfigCommander.DEFAULT_ENABLE_UPLOAD_TO_CLOUD,
                                        ConfigCommander::getEnableUploadToCloud,
                                        ConfigCommander::setEnableUploadToCloud)
                                .controller(bln -> BooleanControllerBuilder.create(bln).formatValue(BooleanOption::isEnabled))
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Component.nullToEmpty("CLOUD NAME"))
                                .description(OptionDescription.of(Component.nullToEmpty(
                                        "CLOUD NAME de tu Cloudinary. Para obtener información, visite https://console.cloudinary.com/settings/")))
                                .binding(
                                        ConfigCommander.DEFAULT_CLOUD_NAME,
                                        ConfigCommander::getCloudName,
                                        ConfigCommander::setCloudName)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Component.nullToEmpty("API-KEY"))
                                .description(OptionDescription.of(Component.nullToEmpty(
                                        "API KEY de la API de Cloudinary. Para obtener una API KEY, visite https://console.cloudinary.com/settings/ -> API KEYS")))
                                .binding(
                                        ConfigCommander.DEFAULT_API_KEY,
                                        ConfigCommander::getApiKeyCloudinary,
                                        ConfigCommander::setApiKeyCloudinary)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Component.nullToEmpty("API SECRET"))
                                .description(OptionDescription.of(Component.nullToEmpty(
                                        "API SECRET de la API de Cloudinary. Para obtener una API KEY, visite https://console.cloudinary.com/settings/ -> API KEYS"))) // Descripción de la opción
                                .binding(
                                        ConfigCommander.DEFAULT_API_SECRET_CLOUDINARY,
                                        ConfigCommander::getApiSecretCloudinary,
                                        ConfigCommander::setApiSecretCloudinary)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .build())
                .build();
    }
}
