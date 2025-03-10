package risinget.commander.gui;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.text.Text;
import risinget.commander.config.ConfigCommander;
public class CloudinaryConfig {

    public static ConfigCategory category(){
        // Setter: Guarda el nuevo valor de la API KEY
        return ConfigCategory.createBuilder()
                .name(Text.of("Cloudinary Screenshots"))
                .tooltip(Text.of("Configuraciones para screenshots en la nube"))
                .group(OptionGroup.createBuilder()
                        .name(Text.of("Cloudinary Cloud")) // Nombre del grupo
                        .description(OptionDescription.of(Text.of(
                                "Configuración de la API KEY. Esta configuración se aplica a Cloudinary."))) // Descripción del grupo
                        .option(Option.<String>createBuilder()
                                .name(Text.of("CLOUD NAME")) // Nombre de la opción
                                .description(OptionDescription.of(Text.of(
                                        "CLOUD NAME de tu Cloudinary. Para obtener información, visite https://console.cloudinary.com/settings/"))) // Descripción de la opción
                                .binding(
                                        ConfigCommander.DEFAULT_CLOUD_NAME, // Valor predeterminado (puede ser una cadena vacía o un valor por defecto)
                                        ConfigCommander::getCloudName, // Getter: Obtiene el valor actual de la API KEY
                                        ConfigCommander::setCloudName)
                                .controller(StringControllerBuilder::create) // Controlador para campos de texto
                                .build())

                        .option(Option.<String>createBuilder()
                                .name(Text.of("API-KEY")) // Nombre de la opción
                                .description(OptionDescription.of(Text.of(
                                        "API KEY de la API de Cloudinary. Para obtener una API KEY, visite https://console.cloudinary.com/settings/ -> API KEYS"))) // Descripción de la opción
                                .binding(
                                        ConfigCommander.DEFAULT_API_KEY, // Valor predeterminado (puede ser una cadena vacía o un valor por defecto)
                                        ConfigCommander::getApiKeyCloudinary, // Getter: Obtiene el valor actual de la API KEY
                                        ConfigCommander::setApiKeyCloudinary)
                                .controller(StringControllerBuilder::create) // Controlador para campos de texto
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.of("API SECRET")) // Nombre de la opción
                                .description(OptionDescription.of(Text.of(
                                        "API SECRET de la API de Cloudinary. Para obtener una API KEY, visite https://console.cloudinary.com/settings/ -> API KEYS"))) // Descripción de la opción
                                .binding(
                                        ConfigCommander.DEFAULT_API_SECRET_CLOUDINARY, // Valor predeterminado (puede ser una cadena vacía o un valor por defecto)
                                        ConfigCommander::getApiSecretCloudinary, // Getter: Obtiene el valor actual de la API KEY
                                        ConfigCommander::setApiSecretCloudinary)
                                .controller(StringControllerBuilder::create) // Controlador para campos de texto
                                .build())
                        .build())
                .build();
    }
}
