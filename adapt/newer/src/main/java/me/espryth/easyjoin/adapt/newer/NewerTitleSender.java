package me.espryth.easyjoin.adapt.newer;

import me.espryth.easyjoin.adapt.TitleSender;
import org.bukkit.entity.Player;

public class NewerTitleSender
  implements TitleSender {

  @Override
  public void send(Player player, String title, String subTitle, int fadeIn, int fadeShow, int fadeOut) {
    player.sendTitle(title, subTitle, fadeIn, fadeShow, fadeOut);
  }
}
