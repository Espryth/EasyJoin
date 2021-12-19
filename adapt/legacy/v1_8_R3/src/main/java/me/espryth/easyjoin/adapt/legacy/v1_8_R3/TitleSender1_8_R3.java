package me.espryth.easyjoin.adapt.legacy.v1_8_R3;

import me.espryth.easyjoin.adapt.TitleSender;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.entity.Player;

public class TitleSender1_8_R3
  implements TitleSender {

  @Override
  public void send(Player player, String title, String subTitle, int fadeIn, int fadeShow, int fadeOut) {
    Packets.send(
      player,
      new PacketPlayOutTitle(
        PacketPlayOutTitle.EnumTitleAction.TITLE,
        IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}"),
        fadeIn, fadeShow, fadeOut
      ),
      new PacketPlayOutTitle(
        PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
        IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subTitle + "\"}"),
        fadeIn, fadeShow, fadeOut
      )
    );
  }
}
