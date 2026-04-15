package risinget.commander.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import risinget.commander.core.CopyCoords;
import risinget.commander.core.CoordsConverter;
import risinget.commander.gui.ScreenGUI;

public class KeyHandler {

    private static final KeyMapping openGuiF8                 = KeyBindingHelper.registerKeyBinding(new KeyMapping("Open Commander Config",                           InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F8, new KeyMapping.Category(Identifier.tryParse("commander"))));
    private static final KeyMapping copyAndConvertToNether    = KeyBindingHelper.registerKeyBinding(new KeyMapping("Copiar y convertir BlocksPosViewing a Nether",    InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F6, new KeyMapping.Category(Identifier.tryParse("commander"))));
    private static final KeyMapping copyAndConvertToOverworld = KeyBindingHelper.registerKeyBinding(new KeyMapping("Copiar y convertir BlocksPosViewing a Overworld", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F7, new KeyMapping.Category(Identifier.tryParse("commander"))));
    private static final KeyMapping copyPosKey                = KeyBindingHelper.registerKeyBinding(new KeyMapping("Copiar posición",                                 InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F9, new KeyMapping.Category(Identifier.tryParse("commander"))));
    private static final KeyMapping copyPosViewing            = KeyBindingHelper.registerKeyBinding(new KeyMapping("Copiar posición del bloque donde miras",          InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F10, new KeyMapping.Category(Identifier.tryParse("commander"))));
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

