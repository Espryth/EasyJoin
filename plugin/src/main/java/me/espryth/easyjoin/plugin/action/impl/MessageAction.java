package me.espryth.easyjoin.plugin.action.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.espryth.easyjoin.plugin.action.AbstractAction;
import me.espryth.easyjoin.plugin.utils.MessageUtils;
import org.bukkit.entity.Player;

public class MessageAction extends AbstractAction {

    @Override
    public void execute(Player player) {

        String newLine = PlaceholderAPI.setPlaceholders(player, MessageUtils.colorize(getLine()));
        
        if(getLine().startsWith("<c>")) {
            player.sendMessage(MessageUtils.getCenteredMessage(newLine.replace("<c>", "")));
            return;
        }

        player.sendMessage(newLine);
    }
}
