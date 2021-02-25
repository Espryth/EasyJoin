package me.espryth.easyjoin.action.impl;

import me.espryth.easyjoin.action.Action;
import me.espryth.easyjoin.action.ActionType;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundAction extends Action {

    public SoundAction(String line) {
        super(ActionType.SOUND, line);
    }

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
