package risinget.commander.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import risinget.commander.core.CopyCoords;
import risinget.commander.core.CoordsConverter;
import risinget.commander.gui.ScreenGUI;
public class KeyHandler {


    // Crear categoría (agrupa tus teclas en el menú de controles)
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("commander", "general")
    );

    private static final KeyMapping openGuiF8                 = KeyMappingHelper.registerKeyMapping(new KeyMapping("Open Commander Config",                           InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F8, CATEGORY));
    private static final KeyMapping copyAndConvertToNether    = KeyMappingHelper.registerKeyMapping(new KeyMapping("Copiar y convertir BlocksPosViewing a Nether",    InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F6, CATEGORY));
    private static final KeyMapping copyAndConvertToOverworld = KeyMappingHelper.registerKeyMapping(new KeyMapping("Copiar y convertir BlocksPosViewing a Overworld", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F7, CATEGORY));
    private static final KeyMapping copyPosKey                = KeyMappingHelper.registerKeyMapping(new KeyMapping("Copiar posición",                                 InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F9, CATEGORY));
    private static final KeyMapping copyPosViewing            = KeyMappingHelper.registerKeyMapping(new KeyMapping("Copiar posición del bloque donde miras",          InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F10, CATEGORY));
    private static final Minecraft client = Minecraft.getInstance();

    public static void onKey(int keyCode) {
        if (openGuiF8.consumeClick() || openGuiF8.isDown()) {
            ScreenGUI.openGui();
        }
        if(copyAndConvertToNether.consumeClick() || copyAndConvertToNether.isDown()){
            CoordsConverter.copyAndConvertToNether();
        }
        if(copyAndConvertToOverworld.consumeClick() || copyAndConvertToOverworld.isDown()){
            CoordsConverter.copyAndConvertToOverworld();
        }

        if(copyPosKey.consumeClick() || copyPosKey.isDown()){
            CopyCoords.copyCurrentPos();
        }

        if(copyPosViewing.consumeClick() || copyPosViewing.isDown()){
            CopyCoords.copyPosViewing();
        }
    }
}

