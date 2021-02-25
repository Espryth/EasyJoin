package me.espryth.easyjoin.listeners;

import me.espryth.easyjoin.Core;
import me.espryth.easyjoin.format.Format;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerQuitListener implements Listener {

    private final Core core;

    public PlayerQuitListener(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        List<Integer> priorityList = new ArrayList<>();

        for(Format format : core.getFormatMap().values()) {
            if(player.hasPermission("easyjoin." + format.getIdentifier())) {
                priorityList.add(format.getPriority());
            }
        }

        int highPriority = Collections.<Integer>max(priorityList);

        for(Format format : core.getFormatMap().values()) {
            if(player.hasPermission("easyjoin." + format.getIdentifier()) && format.getPriority() == highPriority) {
                format.getQuitActions().forEach(action -> action.execute(player));
                return;
            }
        }
    }
}
