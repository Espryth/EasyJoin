package me.espryth.easyjoin.plugin.action.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.espryth.easyjoin.plugin.action.AbstractAction;
import me.espryth.easyjoin.plugin.utils.MessageUtils;
import org.bukkit.entity.Player;

public class MessageAction extends AbstractAction {

    @Override
    public void execute(Player player) {
        setLine(PlaceholderAPI.setPlaceholders(player, getLine()));
        
        if(getLine().startsWith("<c>")) {
            setLine(MessageUtils.getCenteredMessage(getLine().replace("<c>", "")));
        }

        player.sendMessage(getLine());
    }
}
