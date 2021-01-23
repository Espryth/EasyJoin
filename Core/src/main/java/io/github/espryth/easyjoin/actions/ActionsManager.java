package io.github.espryth.easyjoin.actions;

import io.github.espryth.easyjoin.Core;
import io.github.espryth.easyjoin.util.ColorUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ActionsManager {

    private final Core plugin;

    public ActionsManager(Core plugin) {
        this.plugin = plugin;
    }

    public void sendAction(Player player, String line) {
        line = PlaceholderAPI.setPlaceholders(player, line);
        line = line.replace("%count%", Integer.toString(Bukkit.getOfflinePlayers().length));
        if(line.startsWith("[BROADCAST] "))  {
            line = line.replace("[BROADCAST] ", "");
            executeBroadcast(line);
        } else if (line.startsWith("[JSON_BROADCAST] ")) {
            line = line.replace("[JSON_BROADCAST] ", "");
            executeJsonMessage(player, line);
        } else if(line.startsWith("[MESSAGE] ")) {
            line = line.replace("[MESSAGE] ", "");
            executeMessage(player, line);
        } else if(line.startsWith("[JSON_MESSAGE] ")) {
            line = line.replace("[JSON_MESSAGE] ", "");
            executeJsonMessage(player, line);
        } else if(line.startsWith("[TITLE] ")) {
            line = line.replace("[TITLE] ", "");
            executeTitle(player, line);
        } else if(line.startsWith("[BROADCAST_TITLE] ")) {
            line = line.replace("[BROADCAST_TITLE] ", "");
            executeTitleBroadcast(line);
        } else if(line.startsWith("[ACTIONBAR] ")) {
            line = line.replace("[ACTIONBAR] ", "");
            executeActionbar(player, line);
        } else if(line.startsWith("[BROADCAST_ACTIONBAR] ")) {
            line = line.replace("[BROADCAST_ACTIONBAR] ", "");
            executeActionbarBroadcast(line);
        } else if(line.startsWith("[SOUND] ")) {
            line = line.replace("[SOUND] ", "");
            executeSound(player, line);
        } else if(line.startsWith("[BROADCAST_SOUND] ")) {
            line = line.replace("[BROADCAST_SOUND] ", "");
            executeSoundBroadcast(line);
        } else if(line.startsWith("[CLEARCHAT] ")) {
            line = line.replace("[CLEARCHAT] ", "");
            executeClearchat(player, line);
        } else if(line.startsWith("[CONSOLE] ")) {
            line = line.replace("[CONSOLE] ", "");
            executeConsoleCommand(line);
        } else if(line.startsWith("[PLAYER] ")) {
            line = line.replace("[PLAYERR] ", "");
            executePlayerCommand(player, line);
        } else if(line.startsWith("[BOOK] ")) {
            executeOpenBook(player);
        }
    }
    
    private void executeBroadcast(String line) {
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
            executeMessage(onlinePlayer, line);
    }

    private void executeJsonBroadcast(String  line) {
        BaseComponent[] baseComponent = ComponentSerializer.parse(ColorUtil.apply(line));
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
            onlinePlayer.spigot().sendMessage(baseComponent);
    }

    private void executeMessage(Player player, String line) {
        player.sendMessage(ColorUtil.apply(line));
    }

    private void executeJsonMessage(Player player, String line) {
        BaseComponent[] baseComponent = ComponentSerializer.parse(ColorUtil.apply(line));
        player.spigot().sendMessage(baseComponent);
    }

    private void executeTitle(Player player, String line) {
        String[] values = line.split(";");
        plugin.getNmsSetup().getNMSHandler().sendTitle(player, ColorUtil.apply(values[0]), ColorUtil.apply(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4]) );
    }

    private void executeTitleBroadcast(String line) {
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
            executeTitle(onlinePlayer, line);
    }

    private void executeActionbar(Player player, String line) {
        plugin.getNmsSetup().getNMSHandler().sendActionBar(player, line);
    }

    private void executeActionbarBroadcast(String line) {
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
            executeActionbar(onlinePlayer, line);
    }

    private void executeSound(Player player, String line) {
        String[] values = line.split(";");
        try{
            player.playSound(player.getLocation(), Sound.valueOf(values[0]), Float.parseFloat(values[1]), Float.parseFloat(values[2]));
        } catch (Exception es) {
            plugin.getLogger().info("Error with sound " + values[0]);
        }
    }

    private void executeSoundBroadcast(String line) {
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
            executeSound(onlinePlayer, line);
    }

    private void executeClearchat(Player player, String line) {
        for (int i = 0; i < Integer.parseInt(line); i++)
            player.sendMessage(" ");
    }

    private void executePlayerCommand(Player player, String line) {
        Bukkit.getServer().dispatchCommand(player, line);
    }

    private void executeConsoleCommand(String line) {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), line);
    }

    private void executeOpenBook(Player player) {
        if(!plugin.getFilesManager().getBook().contains("book")) return;
        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
            plugin.getNmsSetup().getNMSHandler().openBook(player, plugin.getFilesManager().getBook().getItemStack("book"));
        }, 5L);
    }

}
