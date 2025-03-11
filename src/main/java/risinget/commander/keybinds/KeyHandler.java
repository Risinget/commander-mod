package risinget.commander.keybinds;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class KeyHandler {
    // Mapa de teclas y sus acciones
    public static Map<KeyBinding, Runnable> keyActions = new HashMap<>();
    public static KeyBinding keyG = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.mymod.g_key", GLFW.GLFW_KEY_G, "category.mymod.keys"));
    public static KeyBinding keyF8 = KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
            "key.mymod.f8_key",
                        GLFW.GLFW_KEY_F8,
                "category.mymod.keys"));
    public static void registerKeybinds() {
        // Asociamos cada tecla con una acción
        keyActions.put(keyG, () -> System.out.println("¡Tecla G presionada!"));
        keyActions.put(keyF8, () -> System.out.println("¡Tecla F8 presionada!"));
        // Agregar más teclas y sus acciones
    }
}

