package me.espryth.easyjoin.action.impl;

import me.espryth.easyjoin.action.AbstractAction;
import me.espryth.easyjoin.action.ActionType;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class MessageAction extends AbstractAction {

    public MessageAction(String line) {
        super(ActionType.MESSAGE, line);
    }

    @Override
    public void execute(Player player) {
        setLine(PlaceholderAPI.setPlaceholders(player, getLine()));
        player.sendMessage(getLine());
    }
}
