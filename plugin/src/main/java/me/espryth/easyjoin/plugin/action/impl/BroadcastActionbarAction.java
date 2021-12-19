package me.espryth.easyjoin.plugin.action.impl;

import me.espryth.easyjoin.adapt.ActionbarSender;
import me.espryth.easyjoin.plugin.action.AbstractAction;
import me.espryth.easyjoin.plugin.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;

public class BroadcastActionbarAction extends AbstractAction {

    private final ActionbarSender actionbarSender;

    public BroadcastActionbarAction() {
        this.actionbarSender = CONTAINER.get(ActionbarSender.class);
    }

    @Override
    public void execute(Player player) {
        Bukkit.getOnlinePlayers().forEach(p ->
          actionbarSender.send(player, MessageUtils.formatString(player, getLine())));
    }
}
