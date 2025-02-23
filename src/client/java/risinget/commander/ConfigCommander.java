package risinget.commander;

import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;

public class ConfigCommander {

    public static final ConfigClassHandler<ConfigCommander> HANDLER = ConfigClassHandler.createBuilder(ConfigCommander.class)
           .id(Identifier.of("risinget", "comander/risinget_comander.json5"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
            .setPath(FabricLoader.getInstance().getConfigDir().resolve("risinget_comander.json5"))
            .appendGsonBuilder(GsonBuilder::setPrettyPrinting) // Corrected method name
            .setJson5(true)
            .build())
            .build();

    @SerialEntry
    public static boolean activarDesconexion = true;
    public static boolean isOn(){
        return activarDesconexion;
    }
    public static boolean getOn(){
        return activarDesconexion;
    }
    public static void setOn(boolean isOn){
        activarDesconexion = isOn;
    }

    @SerialEntry
    public static int desconectarMenorVidaQue = 5;

    public static int getHealthMin(){
        return desconectarMenorVidaQue;
    }
    public static void setHealthMin(int healthMin){
        desconectarMenorVidaQue = healthMin;
    }


    @SerialEntry(comment = "This string is amazing")
    public static String coordsFormat = "X: {X}, Y: {Y}, Z: {Z}";

    public static String getCoordsFormat(){
        return coordsFormat;
    }
    public static void setCoordsFormat(String format){
        coordsFormat = format;
    }

    @SerialEntry(comment = "API KEY para GEMINI")
    public static String API_KEY = "your-api-key-here";
    public static String getApiKey(){
        return API_KEY;
    }
    public static void setApiKey(String apiKey){
       API_KEY = apiKey;
    }

    @SerialEntry(comment = "Modelo de Gemini")
    public static String selectedModel = GeminiModel.PRO.getModelName();
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
}
