package me.espryth.easyjoin.plugin.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtils {

    public static String colorize(String text) {
        if (Bukkit.getVersion().contains("1.16")) {
            Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");
            Matcher matcher = hexPattern.matcher(text);
            while (matcher.find()) {
                String color = text.substring(matcher.start(), matcher.end());
                text = text.replace(color, ChatColor.of(color) + "");
                matcher = hexPattern.matcher(text);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
