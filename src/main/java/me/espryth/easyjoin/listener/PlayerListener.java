package me.espryth.easyjoin.listener;

import com.thewinterframework.paper.listener.ListenerComponent;
import jakarta.inject.Inject;
import me.espryth.easyjoin.action.ActionQueue;
import me.espryth.easyjoin.format.Format;
import me.espryth.easyjoin.service.FirstJoinService;
import me.espryth.easyjoin.service.FormatService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

@ListenerComponent
public class PlayerListener implements Listener {

    private final Plugin plugin;
    private final FormatService formatService;
    private final FirstJoinService firstJoinService;

    @Inject
    public PlayerListener(Plugin plugin, FormatService formatService, FirstJoinService firstJoinService) {
        this.plugin = plugin;
        this.formatService = formatService;
        this.firstJoinService = firstJoinService;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.joinMessage(null);

        firstJoinService.isFirstJoin(player).thenAccept(isFirst -> {
            Optional<Format> formatOpt = formatService.getFormatForPlayer(player, isFirst);
            if (formatOpt.isEmpty()) formatOpt = formatService.getDefaultFormat(isFirst);

            formatOpt.ifPresent(format -> {
                new ActionQueue(format.joinActions(), plugin).executeAll(player);
            });

            if (isFirst) firstJoinService.markAsJoined(player);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.quitMessage(null);

        Optional<Format> formatOpt = formatService.getFormatForPlayer(player, false);
        if (formatOpt.isEmpty()) formatOpt = formatService.getDefaultFormat(false);

        formatOpt.ifPresent(format -> {
            new ActionQueue(format.quitActions(), plugin).executeAll(player);
        });
    }
}
