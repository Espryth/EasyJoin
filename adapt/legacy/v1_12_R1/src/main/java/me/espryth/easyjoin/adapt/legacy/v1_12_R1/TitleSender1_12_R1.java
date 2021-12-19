package me.espryth.easyjoin.adapt.legacy.v1_12_R1;

import me.espryth.easyjoin.adapt.TitleSender;
import org.bukkit.entity.Player;

public class TitleSender1_12_R1
  implements TitleSender {
  @Override
  public void send(Player player, String title, String subTitle, int fadeIn, int fadeShow, int fadeOut) {
    player.sendTitle(title, subTitle, fadeIn, fadeShow, fadeOut);
  }
}
