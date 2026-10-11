package me.espryth.easyjoin.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class MessageUtils {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private static final Pattern BRACKET_HEX_COLOR_PATTERN = Pattern.compile(
            "&\\[([\\dA-Fa-f])([\\dA-Fa-f]),([\\dA-Fa-f])([\\dA-Fa-f]),([\\dA-Fa-f])([\\dA-Fa-f])]"
    );
    private static final Pattern AMPERSAND_HEX_COLOR_PATTERN = Pattern.compile(
            "&#([\\dA-Fa-f])([\\dA-Fa-f])([\\dA-Fa-f])([\\dA-Fa-f])([\\dA-Fa-f])([\\dA-Fa-f])"
    );
    private static final Pattern LEGACY_AMPERSAND_CODE_PATTERN = Pattern.compile(
            "&([0-9a-fk-orxA-FK-ORX])"
    );

    private static final String BUKKIT_HEX_COLOR = "§x§$1§$2§$3§$4§$5§$6";

    private static final int CENTER_PX = 154;

    public static String parsePlaceholders(Player player, String text) {
        if (text == null || text.isEmpty() || player == null) {
            return text == null ? "" : text;
        }
        try {
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                return applyPlaceholderApi(player, text);
            }
        } catch (Throwable ignored) {
        }
        return text;
    }

    private static String applyPlaceholderApi(Player player, String text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    private static String replaceCustomHex(String text) {
        String result = BRACKET_HEX_COLOR_PATTERN.matcher(text).replaceAll(BUKKIT_HEX_COLOR);
        return AMPERSAND_HEX_COLOR_PATTERN.matcher(result).replaceAll(BUKKIT_HEX_COLOR);
    }

    public static String colorize(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        String newText = replaceCustomHex(text);
        if (newText.indexOf('§') >= 0 || LEGACY_AMPERSAND_CODE_PATTERN.matcher(newText).find()) {
            String normalized = LEGACY_AMPERSAND_CODE_PATTERN.matcher(newText).replaceAll("§$1");
            return LegacyComponentSerializer.legacySection().serialize(
                    LegacyComponentSerializer.legacySection().deserialize(normalized)
            );
        }
        Component component = MINI_MESSAGE.deserialize(newText);
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public static Component colorizeToComponent(String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }
        String newText = replaceCustomHex(text);
        if (newText.indexOf('§') >= 0 || LEGACY_AMPERSAND_CODE_PATTERN.matcher(newText).find()) {
            String normalized = LEGACY_AMPERSAND_CODE_PATTERN.matcher(newText).replaceAll("§$1");
            return LegacyComponentSerializer.legacySection().deserialize(normalized);
        }
        return MINI_MESSAGE.deserialize(newText);
    }

    public static String formatString(Player player, String s) {
        return colorize(parsePlaceholders(player, s));
    }

    public static String getCenteredMessage(String message) {
        return getCenteredSpace(message, CENTER_PX) + message;
    }

    /**
     * Centers a message accounting for content already occupying the left side
     * (e.g. an avatar).
     *
     * @param message  the text to center (with legacy § codes already applied)
     * @param offsetPx pixels already consumed on the left (avatar width +
     *                 separator)
     */
    public static String getCenteredMessageWithOffset(String message, int offsetPx) {
        int effectiveCenter = CENTER_PX - (offsetPx / 2);
        return getCenteredSpace(message, effectiveCenter) + message;
    }

    /**
     * Returns a Component containing only the centering spaces needed to center
     * {@code miniMessageText} with the given left offset, without converting the
     * text itself to legacy format.
     *
     * @param legacyText the text already serialized to legacy § codes, used only
     *                   for pixel-width measurement
     * @param offsetPx   pixels already consumed on the left (avatar width +
     *                   separator)
     * @return a plain Component of spaces to prepend before the real Component
     */
    public static Component getCenteredSpaceComponent(String legacyText, int offsetPx) {
        int effectiveCenter = CENTER_PX - (offsetPx / 2);
        return Component.text(getCenteredSpace(legacyText, effectiveCenter));
    }

    public static String getCenteredSpace(String message) {
        return getCenteredSpace(message, CENTER_PX);
    }

    private static String getCenteredSpace(String message, int centerPx) {
        if (message == null || message.isEmpty()) {
            return "";
        }

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                char lower = Character.toLowerCase(c);
                if (lower == 'l') {
                    isBold = true;
                } else if ((lower >= '0' && lower <= '9')
                        || (lower >= 'a' && lower <= 'f')
                        || lower == 'r'
                        || lower == 'x') {
                    isBold = false;
                }
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = centerPx - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(' ');
            compensated += spaceLength;
        }
        return sb.toString();
    }
}
