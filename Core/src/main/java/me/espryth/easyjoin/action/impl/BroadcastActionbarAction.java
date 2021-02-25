package me.espryth.easyjoin.action.impl;

import me.espryth.easyjoin.Core;
import me.espryth.easyjoin.action.Action;
import me.espryth.easyjoin.action.ActionType;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class BroadcastActionbarAction extends Action {

    private final Core core;

    public BroadcastActionbarAction(String line, Core core) {
        super(ActionType.BROADCAST_ACTIONBAR, line);
        this.core = core;
    }

    @Override
    public void execute(Player player) {
        setLine(PlaceholderAPI.setPlaceholders(player, getLine()));
        core.getNmsSetup().getNMSHandler().sendActionBar(
                player, getLine()
        );
    }
}
