package me.espryth.easyjoin.adapt;

import org.bukkit.entity.Player;

public interface TitleSender {

  void send(Player player, String title, String subTitle, int fadeIn, int fadeShow, int fadeOut);

}
