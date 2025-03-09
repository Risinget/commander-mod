package risinget.commander;

import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.text.Text;

public enum GeminiModel implements NameableEnum {
    PRO("gemini-1.5-pro"),
    FLASH("gemini-1.5-flash"),
    FLASH_8B("gemini-1.5-flash-8b");

    private final String modelName;

    GeminiModel(String modelName) {
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal(name().toLowerCase());
    }
}
