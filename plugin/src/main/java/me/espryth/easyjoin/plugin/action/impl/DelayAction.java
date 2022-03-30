package me.espryth.easyjoin.plugin.action.impl;

import me.espryth.easyjoin.plugin.action.AbstractAction;
import me.espryth.easyjoin.plugin.action.ActionExecutionException;
import me.espryth.easyjoin.plugin.action.ActionQueue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;

public class DelayAction extends AbstractAction {

  private final Plugin plugin;

  public DelayAction() {
    plugin = CONTAINER.get(Plugin.class);
  }

  @Override
  public void execute(Player player, ActionQueue queue) throws ActionExecutionException {
    int delay;
    try {
      delay = Integer.parseInt(getLine());
    } catch (NumberFormatException e) {
      throw new ActionExecutionException("Delay isn't a number");
    }
    queue.setPaused(true);
    Bukkit.getScheduler().runTaskLater(plugin, () -> {
      queue.setPaused(false);
      queue.executeAll(player);
    }, delay * 20L);

  }
}
