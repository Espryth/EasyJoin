package me.espryth.easyjoin.action.impl;

import me.espryth.easyjoin.Core;
import me.espryth.easyjoin.action.Action;
import me.espryth.easyjoin.action.ActionType;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class ActionbarAction extends Action {

    private final Core core;

    public ActionbarAction(String line, Core core) {
        super(ActionType.ACTIONBAR, line);
        this.core = core;
    }

    @Override
    public void execute(Player player) {
        setLine(PlaceholderAPI.setPlaceholders(player, getLine()));
        core.getNmsSetup().getNMSHandler().sendActionBar(player, getLine());
    }
}
