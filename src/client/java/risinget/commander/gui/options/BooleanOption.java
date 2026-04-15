package risinget.commander.gui.options;

import net.minecraft.network.chat.Component;
import risinget.commander.utils.FormatterUtils;

public class BooleanOption {

    public static Component isEnabled(boolean bool){
        if(bool){
            return FormatterUtils.parseAndFormatText("&aON");
        }
        return FormatterUtils.parseAndFormatText("&cOFF");
    }
}
