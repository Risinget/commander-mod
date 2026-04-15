package risinget.commander.gui;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.network.chat.Component;
import risinget.commander.config.ConfigCommander;
import risinget.commander.enums.GeminiModel;

public class GeminiAIConfig {

    public static ConfigCategory category(){
        return ConfigCategory.createBuilder()
                .name(Component.nullToEmpty("GeminiAI"))
                .tooltip(Component.nullToEmpty("Configuraciones para Gemini AI"))
                .group(OptionGroup.createBuilder()
                        .name(Component.nullToEmpty("Gemini AI")) // Nombre del grupo
                        .description(OptionDescription.of(Component.nullToEmpty(
                                "Configuración de la API KEY. Esta configuración se aplica a la GEMINI."))) // Descripción del grupo
                        .option(Option.<String>createBuilder()
                                .name(Component.nullToEmpty("Api-Key")) // Nombre de la opción
                                .description(OptionDescription.of(Component.nullToEmpty(
                                        "API KEY de la API de GEMINI. Para obtener una API KEY, visite https://generativelanguage.googleapis.com/"))) // Descripción de la opción
                                .binding(
                                        ConfigCommander.DEFAULT_API_KEY, // Valor predeterminado (puede ser una cadena vacía o un valor por defecto)
                                        ConfigCommander::getApiKeyGemini, // Getter: Obtiene el valor actual de la API KEY
                                        ConfigCommander::setApiKeyGemini
                                )
                                .controller(StringControllerBuilder::create) // Controlador para campos de texto
                                .build())
                        .option(Option.<GeminiModel>createBuilder() // 👈 Cambia <String> por <GeminiModel>
                                .name(Component.literal("Modelo GEMINI"))
                                .description(OptionDescription.of(Component.literal(
                                        "Selecciona el modelo de GEMINI que deseas utilizar.")))
                                .binding(
                                        ConfigCommander.DEFAULT_SELECTED_MODEL, // ✅ Usa GeminiModel en vez de "A"
                                        ConfigCommander::getSelectedModel, // ✅ Getter devuelve GeminiModel
                                        newVal -> ConfigCommander.setSelectedModel(newVal.getModelName())
                                        // ✅ Convierte GeminiModel a String
                                )
                                .controller(opt -> EnumControllerBuilder.create(opt)
                                        .enumClass(GeminiModel.class)
                                        .formatValue(v -> Component.literal(v.name())))
                                .build())


                        .build())
                .build();
    }
}
