package me.espryth.easyjoin.plugin.listeners;

import me.espryth.easyjoin.plugin.EasyJoin;
import me.espryth.easyjoin.plugin.action.ActionType;
import me.espryth.easyjoin.plugin.event.PlayerFirstJoinEvent;
import me.espryth.easyjoin.plugin.format.Format;
import me.espryth.easyjoin.plugin.format.FormatExecutor;
import me.espryth.easyjoin.plugin.format.firstjoin.FirstJoinChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;

public class PlayerJoinListener implements Listener {

    private final FirstJoinChecker firstJoinChecker;
    private final FormatExecutor formatExecutor;
    private final Map<String, Format> formatMap;

    public PlayerJoinListener() {
        firstJoinChecker = CONTAINER.get(FirstJoinChecker.class);
        formatExecutor = CONTAINER.get(FormatExecutor.class);
        formatMap = CONTAINER.get(EasyJoin.class).getFormatMap();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        event.setJoinMessage(null);

        if(firstJoinChecker.isFirstJoin(player)) {
            Bukkit.getPluginManager().callEvent(new PlayerFirstJoinEvent(player));
            return;
        }

        Collection<Format> joinFormats = formatMap.values()
                .stream()
                .filter(format -> !format.isFirstJoinFormat())
                .collect(Collectors.toList());

        if(joinFormats.isEmpty()) return;

        formatExecutor.executeFormat(player, joinFormats, ActionType.JOIN);
    }

}
