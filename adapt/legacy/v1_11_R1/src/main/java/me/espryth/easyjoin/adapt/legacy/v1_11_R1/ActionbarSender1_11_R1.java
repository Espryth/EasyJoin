package me.espryth.easyjoin.adapt.legacy.v1_11_R1;

import me.espryth.easyjoin.adapt.ActionbarSender;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ActionbarSender1_11_R1
  implements ActionbarSender {
  @Override
  public void send(Player player, String message) {
    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
  }
}
