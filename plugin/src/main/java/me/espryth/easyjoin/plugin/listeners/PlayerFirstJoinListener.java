package me.espryth.easyjoin.plugin.listeners;

import me.espryth.easyjoin.plugin.EasyJoin;
import me.espryth.easyjoin.plugin.action.ActionType;
import me.espryth.easyjoin.plugin.format.Format;
import me.espryth.easyjoin.plugin.format.FormatExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;

public class PlayerFirstJoinListener implements Listener {

    private final FormatExecutor formatExecutor;
    private final Map<String, Format> formatMap;

    public PlayerFirstJoinListener() {
        formatExecutor = CONTAINER.get(FormatExecutor.class);
        formatMap = CONTAINER.get(EasyJoin.class).getFormatMap();
    }

    @EventHandler
    public void onPlayerFirstJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        event.setJoinMessage(null);

        if(player.hasPlayedBefore()) return;

        Collection<Format> firstJoinFormats = formatMap.values()
                .stream()
                .filter(Format::isFirstJoinFormat)
                .collect(Collectors.toList());

        if(firstJoinFormats.isEmpty()) return;

        formatExecutor.executeFormat(player, firstJoinFormats, ActionType.JOIN);

    }
}
