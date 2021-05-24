package me.espryth.easyjoin.plugin.action.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.espryth.easyjoin.plugin.action.AbstractAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConsoleCommandAction extends AbstractAction {
    @Override
    public void execute(Player player) {
        setLine(PlaceholderAPI.setPlaceholders(player, getLine()));
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), getLine());
    }
}
