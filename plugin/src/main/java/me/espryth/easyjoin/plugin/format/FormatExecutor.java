package me.espryth.easyjoin.plugin.format;

import me.espryth.easyjoin.plugin.action.Action;
import me.espryth.easyjoin.plugin.action.ActionQueue;
import me.espryth.easyjoin.plugin.action.ActionType;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FormatExecutor {

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
                List<Action> actions = format.getActions(type);
                ActionQueue queue = new ActionQueue(actions);
                queue.executeAll(player);
                return;
            }
        }
    }
}
