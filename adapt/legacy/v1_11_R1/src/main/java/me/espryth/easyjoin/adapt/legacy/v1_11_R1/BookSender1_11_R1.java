package me.espryth.easyjoin.adapt.legacy.v1_11_R1;

import io.netty.buffer.Unpooled;
import me.espryth.easyjoin.adapt.BookSender;
import net.minecraft.server.v1_11_R1.PacketDataSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutCustomPayload;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BookSender1_11_R1
  implements BookSender {
  @Override
  public void openBook(Player player, ItemStack book) {
    ItemStack itemInHand = player.getInventory().getItemInMainHand();
    player.getInventory().setItemInMainHand(book);
    ((CraftPlayer)player).getHandle().playerConnection.sendPacket(
      new PacketPlayOutCustomPayload("MC|BOpen",
        new PacketDataSerializer(Unpooled.buffer()))
    );
    player.getInventory().setItemInMainHand(itemInHand);
  }
}
