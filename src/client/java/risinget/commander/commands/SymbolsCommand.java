package risinget.commander.commands;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;

public class SymbolsCommand {

    public SymbolsCommand() {

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("symbols").executes(context -> {
                String[] symbols = { "⛏", "🔱", "🪓", "🛡", "🗡", "🏹", "🎣", "⚗", "🧪", "🔥", "⛄", "🌧", "⛈", "🍖",
                        "🔔", "🪣", "❌", "⏭", "⏯", "⏮", "⏸", "⏹", "⏺", "⅐", "❤", "❣", "⭐", "⚡", "✎", "☠", "⚠", "⌛", "⌚",
                        "⚓", "✝", "☃", "🌊", "☮", "☯", "Ⓜ", "ℹ", "Ω", "☽", "☀", "❄", "☁", "☂", "☔", "☄", "☺", "☹", "☻",
                        "♀", "♂", "♫", "♩", "♪", "♬", "⚀", "⚁", "⚂", "⚃", "⚄", "⚅", "→", "←", "↓", "↑", "←", "↔", "☞",
                        "☜", "⊻", "⊼", "⊽", "⌀", "⌂", "™", "©", "®", "☑", "☒", "☐", "✔", "✘", "⏏", "◆", "◇", "■", "□",
                        "♠", "♤", "♣", "♧", "♥", "♡", "♦", "♢", "★", "☆", "¢", "ψ", "∞", "▲", "△", "▼", "▽", "○", "◎",
                        "●", "Δ", "▶", "Ⓐ", "Ⓑ", "Ⓒ", "Ⓓ", "Ⓔ", "Ⓕ", "Ⓖ", "Ⓗ", "Ⓘ", "Ⓙ", "Ⓚ", "Ⓛ", "Ⓜ", "Ⓝ", "Ⓞ", "Ⓟ",
                        "Ⓠ", "Ⓡ", "Ⓢ", "Ⓣ", "Ⓤ", "Ⓥ", "Ⓦ", "Ⓧ", "Ⓨ", "Ⓩ", "ⓐ", "ⓑ", "ⓒ", "ⓓ", "ⓔ", "ⓕ", "ⓖ", "ⓗ", "ⓘ",
                        "ⓙ", "ⓚ", "ⓛ", "ⓜ", "ⓝ", "ⓞ", "ⓟ", "ⓠ", "ⓡ", "ⓢ", "ⓣ", "ⓤ", "ⓥ", "ⓦ", "ⓧ", "ⓨ", "ⓩ", "⁰", "¹",
                        "²", "³", "⁴", "⁵", "⁶", "⁷", "⁸", "⁹", "₀", "₁", "₂", "₃", "₄", "₅", "₆", "₇", "₈", "₉", "⅟",
                        "½", "⅓", "¼", "⅕", "⅙", "⅛", "⓪", "①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨", "⑩", "⑪", "⑫",
                        "⑬", "⑭", "⑮", "⑯", "⑰", "⑱", "⑲", "⑳", "ʊ", "ღ", "₪", "∧", "∨", "∩", "⊂", "⊃", "⊥", "∀", "Ξ",
                        "Γ", "Σ", "Π" };
                MutableComponent combinedText = Component.literal(""); // Texto combinado que contendrá todos los emojis
                for (String symbol : symbols) {
                    MutableComponent symbolText = Component.literal(symbol + " ")
                            .withStyle(style -> style
                                    .withColor(ChatFormatting.RED)
                                    .withClickEvent(new ClickEvent.CopyToClipboard(symbol))
                                    .withInsertion(symbol)
                                    .withHoverEvent(new HoverEvent.ShowText(Component.literal("Click para copiar!"))));
                    combinedText.append(symbolText);
                }
                context.getSource().sendFeedback(combinedText);
                return 1;
            }));
        });
    }
}
