package me.espryth.easyjoin.plugin.listeners.authme;

import fr.xephi.authme.events.RegisterEvent;
import me.espryth.easyjoin.plugin.EasyJoin;
import me.espryth.easyjoin.plugin.action.ActionType;
import me.espryth.easyjoin.plugin.format.Format;
import me.espryth.easyjoin.plugin.format.FormatExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;

public class RegisterListener implements Listener {

    private final FormatExecutor formatExecutor;
    private final Map<String, Format> formatMap;

    public RegisterListener() {
        formatExecutor = CONTAINER.get(FormatExecutor.class);
        formatMap = CONTAINER.get(EasyJoin.class).getFormatMap();
    }

    @EventHandler
    public void onRegister(RegisterEvent event) {
        Player player = event.getPlayer();

        if(player.hasPlayedBefore()) return;

        Collection<Format> registerFormats = formatMap.values()
                .stream()
                .filter(Format::isFirstJoinFormat)
                .collect(Collectors.toList());

        if(registerFormats.isEmpty()) return;

        formatExecutor.executeFormat(player, registerFormats, ActionType.AUTHME);
    }
}
