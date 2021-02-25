package me.espryth.easyjoin.action.impl;

import me.espryth.easyjoin.Core;
import me.espryth.easyjoin.action.Action;
import me.espryth.easyjoin.action.ActionType;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastTitleAction extends Action {

    private final Core core;

    public BroadcastTitleAction(String line, Core core) {
        super(ActionType.BROADCAST_TITLE, line);
        this.core = core;
    }

    @Override
    public void execute(Player player) {

        setLine(PlaceholderAPI.setPlaceholders(player, getLine()));
        String[] values = getLine().split(";");

        Bukkit.getOnlinePlayers().forEach(p -> core.getNmsSetup().getNMSHandler().sendTitle(
                p, values[0], values[1], Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4])
        ));
    }
}
