package me.espryth.easyjoin.plugin.action.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.espryth.easyjoin.abstraction.NMS;
import me.espryth.easyjoin.plugin.action.AbstractAction;
import me.espryth.easyjoin.plugin.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;

public class BroadcastTitle extends AbstractAction {

    private final NMS nms;

    public BroadcastTitle() {
        nms = CONTAINER.get(NMS.class);
    }

    @Override
    public void execute(Player player) {

        String[] values = MessageUtils.formatString(player, getLine()).split(";");

        Bukkit.getOnlinePlayers().forEach(p -> nms.sendTitle(
                p, values[0], values[1],
                Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4])
        ));
    }
}
