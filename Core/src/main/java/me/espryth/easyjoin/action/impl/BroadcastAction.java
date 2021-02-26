package me.espryth.easyjoin.action.impl;

import me.espryth.easyjoin.action.AbstractAction;
import me.espryth.easyjoin.action.ActionType;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastAction extends AbstractAction {

    public BroadcastAction(String line) {
        super(ActionType.BROADCAST, line);
    }

    @Override
    public void execute(Player player) {
        setLine(PlaceholderAPI.setPlaceholders(player, getLine()));
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(getLine()));
    }
}
