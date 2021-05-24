package me.espryth.easyjoin.plugin.action.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.espryth.easyjoin.abstraction.NMS;
import me.espryth.easyjoin.plugin.action.AbstractAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;

public class BroadcastActionbarAction extends AbstractAction {

    private final NMS nms;

    public BroadcastActionbarAction() {
        this.nms = CONTAINER.get(NMS.class);
    }

    @Override
    public void execute(Player player) {
        setLine(PlaceholderAPI.setPlaceholders(player, getLine()));
        Bukkit.getOnlinePlayers().forEach(p -> nms.sendActionbar(player, getLine()));
    }
}
