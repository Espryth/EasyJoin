package me.espryth.easyjoin.action;

import org.bukkit.entity.Player;

public interface Action {
    void execute(Player player);
    ActionType getType();

    String getLine();

    void setLine(String line);
}
