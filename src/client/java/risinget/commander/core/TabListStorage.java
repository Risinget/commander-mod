package risinget.commander.core;

import net.minecraft.network.chat.Component;

public class TabListStorage {
    private static Component header;
    private static Component footer;

    public static void setHeader(Component header) {
        TabListStorage.header = header;
    }

    public static void setFooter(Component footer) {
        TabListStorage.footer = footer;
    }

    public static Component getHeader() {
        return header;
    }

    public static Component getFooter() {
        return footer;
    }
}