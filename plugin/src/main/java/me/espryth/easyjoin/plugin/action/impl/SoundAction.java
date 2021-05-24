package me.espryth.easyjoin.plugin.action.impl;

import me.espryth.easyjoin.plugin.action.AbstractAction;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundAction extends AbstractAction {
    @Override
    public void execute(Player player) {
        String[] values = getLine().split(";");
        try{
            player.playSound(player.getLocation(), Sound.valueOf(values[0]), Float.parseFloat(values[1]), Float.parseFloat(values[2]));
        } catch (Exception es) {
            Bukkit.getLogger().info("[EasyJoin] Error with sound " + values[0]);
        }
    }
}
