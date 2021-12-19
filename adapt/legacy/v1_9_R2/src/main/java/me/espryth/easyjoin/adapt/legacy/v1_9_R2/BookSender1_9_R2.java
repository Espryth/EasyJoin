package me.espryth.easyjoin.adapt.legacy.v1_9_R2;

import io.netty.buffer.Unpooled;
import me.espryth.easyjoin.adapt.BookSender;
import net.minecraft.server.v1_9_R2.PacketDataSerializer;
import net.minecraft.server.v1_9_R2.PacketPlayOutCustomPayload;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BookSender1_9_R2
  implements BookSender {
  @Override
  public void openBook(Player player, ItemStack book) {
    ItemStack itemInHand = player.getInventory().getItemInMainHand();
    player.getInventory().setItemInMainHand(book);
    Packets.send(player,
      new PacketPlayOutCustomPayload("MC|BOpen",
        new PacketDataSerializer(Unpooled.buffer()))
    );
    player.getInventory().setItemInMainHand(itemInHand);
  }
}
