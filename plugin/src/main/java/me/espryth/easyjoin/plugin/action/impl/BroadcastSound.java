package me.espryth.easyjoin.plugin.action.impl;

import me.espryth.easyjoin.plugin.action.AbstractAction;
import me.espryth.easyjoin.plugin.action.ActionExecutionException;
import me.espryth.easyjoin.plugin.action.ActionQueue;
import me.espryth.easyjoin.plugin.utils.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastSound extends AbstractAction {
    @Override
    public void execute(Player player, ActionQueue queue) throws ActionExecutionException {
        String[] values = getLine().split(";");
        Sound sound = Sound.parse(values);
        Bukkit.getOnlinePlayers().forEach(p -> sound.play(p, player.getLocation()));
    }
}
