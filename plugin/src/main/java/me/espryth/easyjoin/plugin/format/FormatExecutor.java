package me.espryth.easyjoin.plugin.format;

import me.espryth.easyjoin.plugin.EasyJoin;
import me.espryth.easyjoin.plugin.action.ActionType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;

public class FormatExecutor {

    private final EasyJoin plugin;

    public FormatExecutor() {
        plugin = CONTAINER.get(EasyJoin.class);
    }

    public void executeFormat(Player player, Collection<Format> formats, ActionType type) {

        List<Integer> priorityList = formats
                .stream()
                .filter(format -> player.hasPermission("easyjoin." + format.getIdentifier()))
                .map(Format::getPriority)
                .collect(Collectors.toList());

        if(priorityList.isEmpty()) return;

        int highPriority = Collections.<Integer>max(priorityList);

        for(Format format : formats) {
            if (format.getPriority() == highPriority) {

                switch (type) {
                    case AUTHME:
                        format.getAuthMeActions().forEach(action -> action.execute(player));
                        break;
                    case JOIN:
                        Bukkit.getScheduler().runTaskLater(plugin, () ->
                                        format.getJoinActions().forEach(action -> action.execute(player)),
                                format.getDelay());
                        break;
                    case QUIT:
                        format.getQuitActions().forEach(action -> action.execute(player));
                        break;
                }
                return;
            }
        }
    }
}
