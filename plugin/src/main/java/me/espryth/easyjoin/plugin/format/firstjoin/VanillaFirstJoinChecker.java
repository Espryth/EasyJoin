package me.espryth.easyjoin.plugin.format.firstjoin;

import org.bukkit.entity.Player;

public class VanillaFirstJoinChecker
  implements FirstJoinChecker{

  @Override
  public boolean isFirstJoin(Player player) {
    return !player.hasPlayedBefore();
  }
}
