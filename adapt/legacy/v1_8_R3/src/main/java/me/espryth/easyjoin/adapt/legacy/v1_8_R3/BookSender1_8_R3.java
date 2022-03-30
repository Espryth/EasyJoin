package me.espryth.easyjoin.adapt.legacy.v1_8_R3;

import io.netty.buffer.Unpooled;
import me.espryth.easyjoin.adapt.BookSender;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BookSender1_8_R3
  implements BookSender {

  @Override
  public void send(Player player, ItemStack book) {
    ItemStack itemInHand = player.getItemInHand();
    player.setItemInHand(book);
    Packets.send(player,
      new PacketPlayOutCustomPayload("MC|BOpen",
        new PacketDataSerializer(Unpooled.buffer()))
    );
    player.setItemInHand(itemInHand);
  }
}
