package risinget.commander.utils;

import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;

public class TextColor {




    public static String toJson(Text message) {
        // Obtener el RegistryManager local (BuiltinRegistries)
        RegistryWrapper.WrapperLookup registry = BuiltinRegistries.createWrapperLookup();

        if (registry == null) {
            throw new IllegalStateException("Local RegistryManager is not available");
        }

        // Serializar el Text a JSON
        String json = Text.Serialization.toJsonString(message, registry);
        return json;
    }
}