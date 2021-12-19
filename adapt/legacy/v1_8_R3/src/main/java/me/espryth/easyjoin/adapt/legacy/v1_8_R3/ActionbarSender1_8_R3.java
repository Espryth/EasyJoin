package me.espryth.easyjoin.adapt.legacy.v1_8_R3;

import me.espryth.easyjoin.adapt.ActionbarSender;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.entity.Player;

public class ActionbarSender1_8_R3
  implements ActionbarSender {

  @Override
  public void send(Player player, String message) {
    IChatBaseComponent actionbarText = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
    Packets.send(player, new PacketPlayOutChat(actionbarText, (byte) 2));
  }
}
