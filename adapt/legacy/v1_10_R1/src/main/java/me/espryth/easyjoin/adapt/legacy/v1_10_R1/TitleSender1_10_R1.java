package me.espryth.easyjoin.adapt.legacy.v1_10_R1;

import me.espryth.easyjoin.adapt.TitleSender;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle;
import org.bukkit.entity.Player;

public class TitleSender1_10_R1
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
