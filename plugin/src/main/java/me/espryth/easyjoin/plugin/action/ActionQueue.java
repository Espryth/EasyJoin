package me.espryth.easyjoin.plugin.action;

import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

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
      if(!isPaused()) {
        Action action = next();
        action.execute(player, this);
      }
    }
  }

  @Override
  public boolean hasNext() {
    return cursor >= 0 && cursor <= actions.size();
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
