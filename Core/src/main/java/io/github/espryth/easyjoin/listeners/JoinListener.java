package io.github.espryth.easyjoin.listeners;

import io.github.espryth.easyjoin.Core;
import io.github.espryth.easyjoin.actions.ActionsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JoinListener implements Listener {

    private final Core plugin;

    public JoinListener(Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        ActionsManager actionsManager = new ActionsManager(plugin);
        if (!player.hasPlayedBefore() && plugin.getFilesManager().getConfig().getBoolean("FirstJoin.enable")) {
            for (String line : plugin.getFilesManager().getConfig().getStringList("FirstJoin.join.actions")) {
                int delay = 0;
                if(plugin.getFilesManager().getConfig().contains("FirstJoin.join.delay")) {
                    delay = plugin.getFilesManager().getConfig().getInt("FirstJoin.join.delay");
                }
                Bukkit.getScheduler().runTaskLater(plugin, ()-> {
                    actionsManager.sendAction(player, line);
                }, delay);
            }
        } else {
            List<Integer> priorityList = new ArrayList<>();
            for (String format : plugin.getFilesManager().getConfig().getConfigurationSection("Formats").getKeys(false)) {
                if (player.hasPermission("easyjoin." + format))
                    priorityList.add(plugin.getFilesManager().getConfig().getInt("Formats." + format + ".priority"));
            }
            for (String format : plugin.getFilesManager().getConfig().getConfigurationSection("Formats").getKeys(false)) {
                if (player.hasPermission("easyjoin." + format) && (Collections.<Integer>max(priorityList)) == plugin.getFilesManager().getConfig().getInt("Formats." + format + ".priority")) {
                    for (String line : plugin.getFilesManager().getConfig().getStringList("Formats." + format + ".join.actions")) {
                        int delay = 0;
                        if(plugin.getFilesManager().getConfig().contains("Formats."+format+".join.delay")) {
                            delay = plugin.getFilesManager().getConfig().getInt("Formats."+format+".join.delay");
                        }
                        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
                            actionsManager.sendAction(player, line);
                        }, delay);
                    }
                }
            }
        }
    }
}
