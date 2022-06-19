package me.espryth.easyjoin.plugin.action.impl;

import me.espryth.easyjoin.adapt.ActionbarSender;
import me.espryth.easyjoin.plugin.action.AbstractAction;
import me.espryth.easyjoin.plugin.action.ActionExecutionException;
import me.espryth.easyjoin.plugin.action.ActionQueue;
import me.espryth.easyjoin.plugin.utils.Actionbar;
import me.espryth.easyjoin.plugin.utils.MessageUtils;
import me.espryth.easyjoin.plugin.utils.RepeatingAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;

public class BroadcastActionbarAction extends AbstractAction {

    private final Plugin plugin;
    private final ActionbarSender actionbarSender;

    public BroadcastActionbarAction() {
        this.plugin = CONTAINER.get(Plugin.class);
        this.actionbarSender = CONTAINER.get(ActionbarSender.class);
    }

    @Override
    public void execute(Player player, ActionQueue queue) throws ActionExecutionException {
        String[] values = MessageUtils.formatString(player, getLine()).split(";");
        Actionbar actionbar = Actionbar.parse(values);
        RepeatingAction.execute(plugin, actionbar.getFadeShow(), () -> {
            Bukkit.getOnlinePlayers().forEach(p ->
              actionbarSender.send(player, actionbar.getText()));
        });
    }
}
