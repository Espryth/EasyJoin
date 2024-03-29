package me.espryth.easyjoin.plugin.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerFirstJoinEvent extends PlayerEvent {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  public PlayerFirstJoinEvent(@NotNull Player who) {
    super(who);
  }

  @NotNull
  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
}
