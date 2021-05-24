package me.espryth.easyjoin.plugin.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtils {

    private final static int CENTER_PX = 154;

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

    public static String getCenteredMessage(String message) {
        return getCenteredSpace(message) + message;
    }

    public static String getCenteredSpace(String message) {

        if(message == null || message.equals("")) return "";

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
                continue;
            }else if(previousCode){
                previousCode = false;
                if(c == 'l' || c == 'L'){
                    isBold = true;
                    continue;
                }else isBold = false;
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb.toString();
    }

    public static BaseComponent[] getCenteredComponents(BaseComponent[] components) {
        StringBuilder legacy = new StringBuilder();

        for (BaseComponent component : components) {
            legacy.append(component.toLegacyText());
        }

        String space = getCenteredSpace(legacy.toString());

        BaseComponent[] newComponents = new BaseComponent[components.length + 1];
        newComponents[0] = new TextComponent(space);

        System.arraycopy(components, 0, newComponents, 1, newComponents.length - 1);
        return newComponents;
    }
}
