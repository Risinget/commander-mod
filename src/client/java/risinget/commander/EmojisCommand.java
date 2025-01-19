package risinget.commander;
import risinget.commander.utils.Prefix;
import risinget.commander.utils.Formatter;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
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
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/copyemoji " + emoji))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click para copiar!"))));

                    combinedText.append(emojiText); // Añadir el texto del emoji al texto combinado
                }

                context.getSource().sendFeedback(combinedText); // Enviar el texto combinado como una sola línea

                return 1;
            }));

            dispatcher.register(ClientCommandManager.literal("copyemoji")
                .then(ClientCommandManager.argument("emoji", StringArgumentType.greedyString())
                    .executes(context -> {
                        String emoji = StringArgumentType.getString(context, "emoji");
                        MinecraftClient.getInstance().keyboard.setClipboard(emoji); // Usa el método de
                                                                                    // Minecraft para copiar al
                                                                                    // portapapeles

                        Formatter formatter = new Formatter();
                        MutableText emojiText = formatter.parseAndFormatText(Prefix.COMMANDER+"&7 Emoji copiado al portapapeles: " + emoji);
                        context.getSource().sendFeedback(emojiText);
                        return 1;
                    })));
        });
    }
}
