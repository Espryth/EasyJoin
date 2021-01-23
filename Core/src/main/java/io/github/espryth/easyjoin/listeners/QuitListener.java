package io.github.espryth.easyjoin.listeners;

import io.github.espryth.easyjoin.Core;
import io.github.espryth.easyjoin.actions.ActionsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuitListener implements Listener {

    private final Core plugin;

    public QuitListener(Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        ActionsManager actionsManager = new ActionsManager(plugin);
        List<Integer> priorityList = new ArrayList<>();
        for (String format : plugin.getFilesManager().getConfig().getConfigurationSection("Formats").getKeys(false)) {
            if (player.hasPermission("easyjoin." + format)) {
                priorityList.add(Integer.valueOf(plugin.getFilesManager().getConfig().getInt("Formats." + format + ".priority")));
            }
        }
        for (String format : plugin.getFilesManager().getConfig().getConfigurationSection("Formats").getKeys(false)) {
            if (player.hasPermission("easyjoin." + format) && (Collections.<Integer>max(priorityList)) == plugin.getFilesManager().getConfig().getInt("Formats." + format + ".priority")) {
                for (String line : plugin.getFilesManager().getConfig().getStringList("Formats." + format + ".quit.actions")) {
                    actionsManager.sendAction(player, line);
                }
            }
        }
    }
}
