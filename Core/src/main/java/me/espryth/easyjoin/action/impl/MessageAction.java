package me.espryth.easyjoin.action.impl;

import me.espryth.easyjoin.action.Action;
import me.espryth.easyjoin.action.ActionType;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class MessageAction extends Action {

    public MessageAction(String line) {
        super(ActionType.MESSAGE, line);
    }

    @Override
    public void execute(Player player) {
        setLine(PlaceholderAPI.setPlaceholders(player, getLine()));
        player.sendMessage(getLine());
    }
}
