package me.espryth.easyjoin.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtil {

    public static String apply(String message) {
        if (Bukkit.getVersion().contains("1.16")) {
            Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");
            Matcher matcher = hexPattern.matcher(message);
            while (matcher.find()) {
                String color = message.substring(matcher.start(), matcher.end());
                message = message.replace(color, ChatColor.of(color) + "");
                matcher = hexPattern.matcher(message);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
