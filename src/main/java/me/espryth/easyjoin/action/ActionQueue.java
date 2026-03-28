package me.espryth.easyjoin.action;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ActionQueue {

    private final List<Action> actions;
    private final Plugin plugin;
    private int cursor = 0;
    private boolean paused = false;

    public ActionQueue(List<Action> actions, Plugin plugin) {
        this.actions = actions;
        this.plugin = plugin;
    }

    public void executeAll(Player player) {
        if (!Bukkit.isPrimaryThread()) {
            player.getScheduler().run(plugin, t -> executeAll(player), null);
            return;
        }
        while (cursor < actions.size()) {
            if (paused) break;
            Action action = actions.get(cursor++);
            action.execute(player, this);
        }
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
