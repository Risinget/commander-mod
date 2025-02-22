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
    public boolean myCoolBoolean = true;

    @SerialEntry
    public int myCoolInteger = 5;

    @SerialEntry(comment = "This string is amazing")
    public String myCoolString = "How amazing!";

}
