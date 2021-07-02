package me.espryth.easyjoin.plugin.action.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.espryth.easyjoin.plugin.action.AbstractAction;
import me.espryth.easyjoin.plugin.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastAction extends AbstractAction {

    @Override
    public void execute(Player player) {

        String newLine = MessageUtils.formatString(player, getLine());

        if(getLine().startsWith("<c>")) {
            Bukkit.broadcastMessage(MessageUtils.getCenteredMessage(newLine.replace("<c>", "")));
            return;
        }
        Bukkit.broadcastMessage(newLine);
    }
}
