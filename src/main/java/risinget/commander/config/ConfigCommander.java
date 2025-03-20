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
    public static final boolean DEFAULT_ACTIVAR_DESCONEXION = false;
    public static final int DEFAULT_DESCONECTAR_MENOR_VIDA_QUE = 5;
    public static final String DEFAULT_COORDS_FORMAT = "X: {X}, Y: {Y}, Z: {Z}";
    public static final String DEFAULT_API_KEY = "your-api-key-here";
    public static final GeminiModel DEFAULT_SELECTED_MODEL = GeminiModel.PRO;
    public static final String DEFAULT_CLOUD_NAME = "cloud_name_cloudinary";
    public static final String DEFAULT_API_SECRET_CLOUDINARY = "api_secret_id";
    public static final boolean DEFAULT_ENABLE_UPLOAD_TO_CLOUD = false;
    public static final boolean DEFAULT_ENABLE_SCREENSHOT_BEFORE_DISCONNECT = true;
    public static final ConfigClassHandler<ConfigCommander> HANDLER = ConfigClassHandler.createBuilder(ConfigCommander.class)
            .id(Identifier.of("risinget", "commander/risinget_commander.json5"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
            .setPath(FabricLoader.getInstance().getConfigDir().resolve("commander/risinget_commander.json5"))
            .appendGsonBuilder(GsonBuilder::setPrettyPrinting) // Corrected method name
            .setJson5(true)
            .build())
            .build();

    @SerialEntry(comment = "--------- CONFIGURATION FOR AUTODISCONNECT ---------")
    public static boolean activarDesconexion = DEFAULT_ACTIVAR_DESCONEXION;
    public static boolean isOn(){return activarDesconexion;}
    public static boolean getOn(){ return activarDesconexion; }
    public static void setOn(boolean isOn){
        activarDesconexion = isOn;
    }

    @SerialEntry
    public static boolean enableSsBeforeDisc = DEFAULT_ENABLE_SCREENSHOT_BEFORE_DISCONNECT;
    public static boolean getEnableSsBeforeDisc(){return enableSsBeforeDisc;}
    public static void setEnableSsBeforeDisc(boolean bln){ enableSsBeforeDisc = bln;}

    @SerialEntry
    public static int desconectarMenorVidaQue = DEFAULT_DESCONECTAR_MENOR_VIDA_QUE;
    public static int getHealthMin(){ return desconectarMenorVidaQue; }
    public static void setHealthMin(int healthMin){
        desconectarMenorVidaQue = healthMin;
    }

    @SerialEntry(comment = "--------- CONFIGURATION FOR COORDS ---------")
    public static String posCoordsFormat = DEFAULT_COORDS_FORMAT;
    public static String getPosCoordsFormat(){return posCoordsFormat;}
    public static void setPosCoordsFormat(String newFormat ){posCoordsFormat = newFormat;}

    @SerialEntry
    public static String posCoordsViewing = DEFAULT_COORDS_FORMAT;
    public static String getPosCoordsViewing(){return posCoordsViewing;}
    public static void setPosCoordsViewing(String newFormat){ posCoordsViewing = newFormat;}

    @SerialEntry
    public static String formatCoordsNether = DEFAULT_COORDS_FORMAT;
    public static String getFormatCoordsNether(){return formatCoordsNether;}
    public static void setFormatCoordsNether(String newFormat){ formatCoordsNether = newFormat;}

    @SerialEntry
    public static String formatCoordsOverworld = DEFAULT_COORDS_FORMAT;
    public static String getFormatCoordsOverworld(){return formatCoordsOverworld;}
    public static void setFormatCoordsOverworld(String newFormat){formatCoordsOverworld = newFormat;}

    @SerialEntry(comment = "--------- CONFIGURATION FOR GEMINI AI ---------")
    public static String API_KEY_GEMINI = DEFAULT_API_KEY;
    public static String getApiKeyGemini(){
        return API_KEY_GEMINI;
    }
    public static void setApiKeyGemini(String apiKey){
        API_KEY_GEMINI = apiKey;
    }

    @SerialEntry
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

    @SerialEntry(comment = "--------- CONFIGURATION FOR CLOUDINARY ---------")
    public static boolean enableUploadToCloud = DEFAULT_ENABLE_UPLOAD_TO_CLOUD;
    public static boolean getEnableUploadToCloud(){ return enableUploadToCloud; }
    public static void setEnableUploadToCloud(boolean enable){enableUploadToCloud = enable;}

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


    public static boolean DEFAULT_ENABLE_HISTORY_CHAT = true;
    @SerialEntry
    public static boolean enableHistoryChat = DEFAULT_ENABLE_HISTORY_CHAT;
    public static boolean getEnableHistoryChat(){
        return enableHistoryChat;
    }
    public static void setEnableHistoryChat(boolean bl){
        enableHistoryChat = bl;
    }


    public static final boolean DEFAULT_ENABLED_TORCH_CREATIVE = false;
    @SerialEntry(comment = "--------- CONFIGURATION FOR MISC FUNCTIONS ---------")
    public static boolean enabledTorchCreative = DEFAULT_ENABLED_TORCH_CREATIVE;
    public static boolean getEnabledTorchCreative(){
        return enabledTorchCreative;
    }
    public static void setEnabledTorchCreative(boolean bln){
        enabledTorchCreative = bln;
    }

    @SerialEntry
    public static final String DEFAULT_CUSTOM_NAME = "A simple dream";
    public static String customName = DEFAULT_CUSTOM_NAME;
    public static String getCustomName(){ return customName;}
    public static void setCustomName(String name){customName = name;}

}
