package me.espryth.easyjoin.plugin.listeners;

import me.espryth.easyjoin.plugin.EasyJoin;
import me.espryth.easyjoin.plugin.action.ActionType;
import me.espryth.easyjoin.plugin.format.Format;
import me.espryth.easyjoin.plugin.format.FormatExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;
import java.util.stream.Collectors;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;

public class PlayerQuitListener implements Listener {

    private final FormatExecutor formatExecutor;
    private final Map<String, Format> formatMap;

    public PlayerQuitListener() {
        formatExecutor = CONTAINER.get(FormatExecutor.class);
        formatMap = CONTAINER.get(EasyJoin.class).getFormatMap();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        event.setQuitMessage(null);

        Collection<Format> quitFormats = formatMap.values()
                .stream()
                .filter(format -> !format.isFirstJoinFormat())
                .collect(Collectors.toList());

        if(quitFormats.isEmpty()) return;

        formatExecutor.executeFormat(player, quitFormats, ActionType.QUIT);
    }

}
