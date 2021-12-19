package me.espryth.easyjoin.adapt.newer;

import me.espryth.easyjoin.adapt.ActionbarSender;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class NewerActionbarSender
  implements ActionbarSender {

  @Override
  public void send(Player player, String message) {
    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
  }
}
