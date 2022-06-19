package me.espryth.easyjoin.plugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class RepeatingAction {

  private static final Queue<RepeatingAction> CURRENT = new ConcurrentLinkedDeque<>();

  private final Plugin plugin;
  private int seconds;

  private BukkitTask task;

  public RepeatingAction(Plugin plugin, int seconds) {
    this.plugin = plugin;
    this.seconds = seconds;
  }

  public static void execute(Plugin plugin, int seconds, Runnable runnable) {
    RepeatingAction repeatingAction= new RepeatingAction(plugin, seconds);
    repeatingAction.execute(runnable);
    CURRENT.add(repeatingAction);
  }

  public static void cancelAll() {
    CURRENT.forEach(RepeatingAction::cancel);
  }

  public void execute(Runnable runnable) {
    task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
      if(seconds > 0) {
        seconds--;
        runnable.run();
      } else {
        task.cancel();
      }
    },1, 20);
  }

  public void cancel() {
    if(CURRENT.remove(this)) {
      task.cancel();
    }
  }


}
