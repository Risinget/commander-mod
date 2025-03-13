package risinget.commander.keybinds;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import risinget.commander.core.CoordsConverter;
import risinget.commander.gui.ScreenGUI;

public class KeyHandler {
    private static KeyBinding openGuiF8 = KeyBindingHelper.registerKeyBinding(new KeyBinding("Open Commander Config", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F8, "Commander"));
    private static KeyBinding copyAndConvertToNether = KeyBindingHelper.registerKeyBinding(new KeyBinding("Copiar y convertir BlocksPosViewing a Nether", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F6, "Commander"));
    private static KeyBinding copyAndConvertToOverworld = KeyBindingHelper.registerKeyBinding(new KeyBinding("Copiar y convertir BlocksPosViewing a Overworld", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F7, "Commander"));
    private static MinecraftClient client = MinecraftClient.getInstance();

    public static void onKey(int keyCode) {
        if (openGuiF8.wasPressed() || openGuiF8.isPressed()) {
            ScreenGUI.openGui();
        }
        if(copyAndConvertToNether.wasPressed() || copyAndConvertToNether.isPressed()){
            CoordsConverter.copyAndConvertToNether();
        }
        if(copyAndConvertToOverworld.wasPressed() || copyAndConvertToOverworld.isPressed()){
            CoordsConverter.copyAndConvertToOverworld();
        }
    }
}

