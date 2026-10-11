package me.espryth.easyjoin.action;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ActionQueue {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionQueue.class);

    private final List<Action> actions;
    private final Plugin plugin;
    private int cursor = 0;
    private boolean paused = false;

    public ActionQueue(List<Action> actions, Plugin plugin) {
        this.actions = actions != null ? List.copyOf(actions) : List.of();
        this.plugin = plugin;
    }

    public void executeAll(Player player) {
        if (actions.isEmpty() || cursor >= actions.size()) {
            return;
        }
        if (!Bukkit.isOwnedByCurrentRegion(player)) {
            boolean scheduled = player.getScheduler().run(plugin, t -> executeInternal(player), null) != null;
            if (!scheduled) {
                Bukkit.getGlobalRegionScheduler().execute(plugin, () -> executeInternal(player));
            }
            return;
        }
        executeInternal(player);
    }

    private void executeInternal(Player player) {
        while (cursor < actions.size()) {
            if (paused) {
                break;
            }
            Action action = actions.get(cursor++);
            try {
                action.execute(player, this);
            } catch (Exception e) {
                LOGGER.warn("Failed to execute action for player {}", player.getName(), e);
            }
        }
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
