package me.espryth.easyjoin.action;

import org.bukkit.entity.Player;

public interface Action {
    void execute(Player player, ActionQueue queue);
}
