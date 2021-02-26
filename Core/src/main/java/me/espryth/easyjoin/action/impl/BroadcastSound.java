package me.espryth.easyjoin.action.impl;

import me.espryth.easyjoin.action.AbstractAction;
import me.espryth.easyjoin.action.ActionType;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class BroadcastSound extends AbstractAction {

    public BroadcastSound(String line) {
        super(ActionType.BROADCAST_SOUND, line);
    }

    @Override
    public void execute(Player player) {
        String[] values = getLine().split(";");
        Bukkit.getOnlinePlayers().forEach(p -> {
            try{
                p.playSound(player.getLocation(), Sound.valueOf(values[0]), Float.parseFloat(values[1]), Float.parseFloat(values[2]));
            } catch (Exception es) {
                Bukkit.getLogger().info("[EasyJoin] Error with sound " + values[0]);
            }
        });
    }
}
