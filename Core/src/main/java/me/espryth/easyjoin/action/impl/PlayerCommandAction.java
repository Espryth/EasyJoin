package me.espryth.easyjoin.action.impl;

import me.espryth.easyjoin.action.AbstractAction;
import me.espryth.easyjoin.action.ActionType;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerCommandAction extends AbstractAction {

    public PlayerCommandAction(String line) {
        super(ActionType.PLAYER_COMMAND, line);
    }

    @Override
    public void execute(Player player) {
        setLine(PlaceholderAPI.setPlaceholders(player, getLine()));
        Bukkit.getServer().dispatchCommand(player, getLine());
    }
}
