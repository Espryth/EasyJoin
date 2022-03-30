package me.espryth.easyjoin.plugin.action;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;

public class ActionQueue
  implements Iterator<Action> {

  private final List<Action> actions;
  private int cursor = 0;
  private boolean paused = false;

  public ActionQueue(List<Action> actions) {
    this.actions = actions;
  }

  public void executeAll(Player player) {
    while(hasNext()) {
      if(isPaused()) {
        break;
      }
      Action action = next();
      try {
        action.execute(player, this);
      } catch (ActionExecutionException e) {
        CONTAINER.get(Plugin.class).getLogger().log(Level.SEVERE, e.getMessage());
      }
    }
  }

  @Override
  public boolean hasNext() {
    return cursor >= 0 && cursor < actions.size();
  }

  @Override
  public Action next() {
    Action action = actions.get(cursor);
    cursor++;
    return action;
  }

  public boolean isPaused() {
    return paused;
  }

  public void setPaused(boolean paused) {
    this.paused = paused;
  }

}
