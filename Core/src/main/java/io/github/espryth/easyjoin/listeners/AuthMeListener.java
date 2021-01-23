package io.github.espryth.easyjoin.listeners;

import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.RegisterEvent;
import io.github.espryth.easyjoin.Core;
import io.github.espryth.easyjoin.actions.ActionsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuthMeListener implements Listener {

    private final Core plugin;

    public AuthMeListener(Core plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(LoginEvent event) {
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
                if (!plugin.getFilesManager().getConfig().contains("Formats."+format+".authme.actions")) return;
                for (String line : plugin.getFilesManager().getConfig().getStringList("Formats." + format + ".authme.actions")) {
                    actionsManager.sendAction(player, line);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRegister(RegisterEvent event) {
        Player player = event.getPlayer();
        ActionsManager actionsManager = new ActionsManager(plugin);
        if (!plugin.getFilesManager().getConfig().contains("FirstJoin.authme.actions")) return;
        if (plugin.getFilesManager().getConfig().getBoolean("FirstJoin.enable")) {
            for (String line : plugin.getFilesManager().getConfig().getStringList("FirstJoin.authme.actions")) {
                actionsManager.sendAction(player, line);
            }
        } else {
            List<Integer> priorityList = new ArrayList<>();
            for (String format : plugin.getFilesManager().getConfig().getConfigurationSection("Formats").getKeys(false)) {
                if (player.hasPermission("easyjoin." + format)) {
                    priorityList.add(Integer.valueOf(plugin.getFilesManager().getConfig().getInt("Formats." + format + ".priority")));
                }
            }
            for (String format : plugin.getFilesManager().getConfig().getConfigurationSection("Formats").getKeys(false)) {
                if (player.hasPermission("easyjoin." + format) && (Collections.<Integer>max(priorityList)) == plugin.getFilesManager().getConfig().getInt("Formats." + format + ".priority")) {
                    if (!plugin.getFilesManager().getConfig().contains("Formats."+format+".authme.actions")) return;
                    for (String line : plugin.getFilesManager().getConfig().getStringList("Formats." + format + ".authme.actions")) {
                        actionsManager.sendAction(player, line);
                    }
                }
            }
        }
    }
}
