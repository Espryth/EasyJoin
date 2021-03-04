package me.espryth.easyjoin.listeners;

import me.espryth.easyjoin.Core;
import me.espryth.easyjoin.event.PlayerFirstJoinEvent;
import me.espryth.easyjoin.format.Format;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerJoinListener implements Listener {

    private final Core core;

    public PlayerJoinListener(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        event.setJoinMessage(null);

        Player player  = event.getPlayer();

        if(!player.hasPlayedBefore()) {
            Bukkit.getPluginManager().callEvent(new PlayerFirstJoinEvent(player));
            return;
        }

        List<Integer> priorityList = new ArrayList<>();

        for(Format format : core.getFormatMap().values()) {
            if(player.hasPermission("easyjoin." + format.getIdentifier()) && !format.isFirstJoinFormat()) {
                priorityList.add(format.getPriority());
            }
        }

        if(priorityList.isEmpty()) return;

        int highPriority = Collections.<Integer>max(priorityList);

        for(Format format : core.getFormatMap().values()) {
            if (player.hasPermission("easyjoin." + format.getIdentifier()) && format.getPriority() == highPriority) {
                Bukkit.getScheduler().runTaskLater(core, () -> format.getJoinActions().forEach(action -> action.execute(player)),
                        format.getDelay());
                return;
            }
        }
    }
}
