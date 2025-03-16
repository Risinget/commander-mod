package risinget.commander.gui;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.text.Text;
import risinget.commander.config.ConfigCommander;
public class CloudinaryConfig {

    public static ConfigCategory category(){
        return ConfigCategory.createBuilder()
                .name(Text.of("Cloudinary Screenshots"))
                .tooltip(Text.of("Configuraciones para screenshots en la nube"))
                .group(OptionGroup.createBuilder()
                        .name(Text.of("Cloudinary Cloud"))
                        .description(OptionDescription.of(Text.of(
                                "Configuración de la API KEY. Esta configuración se aplica a Cloudinary.")))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Upload to Cloud"))
                                .description(OptionDescription.of(Text.of(
                                        "CLOUD NAME de tu Cloudinary. Para obtener información, visite https://console.cloudinary.com/settings/")))
                                .binding(
                                        ConfigCommander.DEFAULT_ENABLE_UPLOAD_TO_CLOUD,
                                        ConfigCommander::getEnableUploadToCloud,
                                        ConfigCommander::setEnableUploadToCloud)
                                .controller(BooleanControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.of("CLOUD NAME"))
                                .description(OptionDescription.of(Text.of(
                                        "CLOUD NAME de tu Cloudinary. Para obtener información, visite https://console.cloudinary.com/settings/")))
                                .binding(
                                        ConfigCommander.DEFAULT_CLOUD_NAME,
                                        ConfigCommander::getCloudName,
                                        ConfigCommander::setCloudName)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.of("API-KEY"))
                                .description(OptionDescription.of(Text.of(
                                        "API KEY de la API de Cloudinary. Para obtener una API KEY, visite https://console.cloudinary.com/settings/ -> API KEYS")))
                                .binding(
                                        ConfigCommander.DEFAULT_API_KEY,
                                        ConfigCommander::getApiKeyCloudinary,
                                        ConfigCommander::setApiKeyCloudinary)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.of("API SECRET"))
                                .description(OptionDescription.of(Text.of(
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
