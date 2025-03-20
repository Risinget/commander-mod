package risinget.commander.gui.options;

import net.minecraft.text.Text;
import risinget.commander.utils.FormatterUtils;

public class BooleanOption {

    public static Text isEnabled(boolean bool){
        if(bool){
            return FormatterUtils.parseAndFormatText("&aON");
        }
        return FormatterUtils.parseAndFormatText("&cOFF");
    }
}
