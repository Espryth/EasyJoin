package me.espryth.easyjoin.plugin.listeners.authme;

import me.espryth.easyjoin.plugin.EasyJoin;
import me.espryth.easyjoin.plugin.action.ActionType;
import me.espryth.easyjoin.plugin.event.PlayerLoginEvent;
import me.espryth.easyjoin.plugin.format.Format;
import me.espryth.easyjoin.plugin.format.FormatExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;

public class PlayerLoginListener implements Listener {

    private final FormatExecutor formatExecutor;
    private final Map<String, Format> formatMap;

    public PlayerLoginListener() {
        formatExecutor = CONTAINER.get(FormatExecutor.class);
        formatMap = CONTAINER.get(EasyJoin.class).getFormatMap();
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        Collection<Format> loginFormats = formatMap.values()
                .stream()
                .filter(format -> !format.isFirstJoinFormat())
                .collect(Collectors.toList());

        if(loginFormats.isEmpty()) return;

        formatExecutor.executeFormat(player, loginFormats, ActionType.AUTHME);
    }
}
