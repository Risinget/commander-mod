package risinget.commander.core;

import net.minecraft.text.Text;

public class TabListStorage {
    private static Text header;
    private static Text footer;

    public static void setHeader(Text header) {
        TabListStorage.header = header;
    }

    public static void setFooter(Text footer) {
        TabListStorage.footer = footer;
    }

    public static Text getHeader() {
        return header;
    }

    public static Text getFooter() {
        return footer;
    }
}