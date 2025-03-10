package risinget.commander.config;

import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import risinget.commander.enums.GeminiModel;

public class ConfigCommander {

    // Valores predeterminados (inmutables)
    public static final boolean DEFAULT_ACTIVAR_DESCONEXION = true;
    public static final int DEFAULT_DESCONECTAR_MENOR_VIDA_QUE = 5;
    public static final String DEFAULT_COORDS_FORMAT = "X: {X}, Y: {Y}, Z: {Z}";
    public static final String DEFAULT_API_KEY = "your-api-key-here";
    public static final GeminiModel DEFAULT_SELECTED_MODEL = GeminiModel.PRO;
    public static final String DEFAULT_CLOUD_NAME = "cloud_name_cloudinary";
    public static final String DEFAULT_API_SECRET_CLOUDINARY = "api_secret_id";

    public static final ConfigClassHandler<ConfigCommander> HANDLER = ConfigClassHandler.createBuilder(ConfigCommander.class)
           .id(Identifier.of("risinget", "comander/risinget_comander.json5"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
            .setPath(FabricLoader.getInstance().getConfigDir().resolve("risinget_comander.json5"))
            .appendGsonBuilder(GsonBuilder::setPrettyPrinting) // Corrected method name
            .setJson5(true)
            .build())
            .build();

    @SerialEntry
    public static boolean activarDesconexion = DEFAULT_ACTIVAR_DESCONEXION;
    public static boolean isOn(){return activarDesconexion;}
    public static boolean getOn(){ return activarDesconexion; }
    public static void setOn(boolean isOn){
        activarDesconexion = isOn;
    }

    @SerialEntry
    public static int desconectarMenorVidaQue = DEFAULT_DESCONECTAR_MENOR_VIDA_QUE;
    public static int getHealthMin(){ return desconectarMenorVidaQue; }
    public static void setHealthMin(int healthMin){
        desconectarMenorVidaQue = healthMin;
    }

    @SerialEntry(comment = "Default Coords Format")
    public static String coordsFormat = DEFAULT_COORDS_FORMAT;
    public static String getCoordsFormat(){
        return coordsFormat;
    }
    public static void setCoordsFormat(String format){
        coordsFormat = format;
    }

    @SerialEntry(comment = "API KEY para GEMINI")
    public static String API_KEY_GEMINI = DEFAULT_API_KEY;
    public static String getApiKeyGemini(){
        return API_KEY_GEMINI;
    }
    public static void setApiKeyGemini(String apiKey){
        API_KEY_GEMINI = apiKey;
    }

    @SerialEntry(comment = "Modelo de Gemini")
    public static String selectedModel = DEFAULT_SELECTED_MODEL.getModelName();
    public static GeminiModel getSelectedModel() {
        for (GeminiModel model : GeminiModel.values()) {
            if (model.getModelName().equals(selectedModel)) {
                return model;
            }
        }
        return GeminiModel.PRO; // Valor por defecto si no se encuentra
    }
    public static void setSelectedModel(String modelName) {
        selectedModel = modelName;
    }

    @SerialEntry
    public static String CLOUD_NAME = DEFAULT_CLOUD_NAME;
    public static String getCloudName(){return CLOUD_NAME; }
    public static void setCloudName(String cloudName){ CLOUD_NAME = cloudName;}

    @SerialEntry
    public static String API_KEY_CLOUDINARY = DEFAULT_API_KEY;
    public static String getApiKeyCloudinary(){ return API_KEY_CLOUDINARY; }
    public static void setApiKeyCloudinary(String apiKey){ API_KEY_CLOUDINARY = apiKey; }

    @SerialEntry
    public static String API_SECRET_CLOUDINARY = DEFAULT_API_SECRET_CLOUDINARY;
    public static String getApiSecretCloudinary(){return API_SECRET_CLOUDINARY;}
    public static void setApiSecretCloudinary(String apiSecret){API_SECRET_CLOUDINARY = apiSecret;}






}
