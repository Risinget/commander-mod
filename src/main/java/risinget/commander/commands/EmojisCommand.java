package risinget.commander.commands;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class EmojisCommand {

    public EmojisCommand() {

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("emojis").executes(context -> {
                String[] emojis = { "⛏", "🔱", "🪓", "🛡", "🗡", "🏹", "🎣", "⚗", "🧪", "🔥", "⛄", "🌧", "⛈", "🍖",
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
                MutableText combinedText = Text.literal(""); // Texto combinado que contendrá todos los emojis
                for (String emoji : emojis) {
                    MutableText emojiText = Text.literal(emoji + " ")
                            .styled(style -> style
                                    .withColor(Formatting.RED)
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, emoji))
                                    .withInsertion(emoji)
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click para copiar!"))));
                    combinedText.append(emojiText);
                }
                context.getSource().sendFeedback(combinedText);
                return 1;
            }));
        });
    }
}
