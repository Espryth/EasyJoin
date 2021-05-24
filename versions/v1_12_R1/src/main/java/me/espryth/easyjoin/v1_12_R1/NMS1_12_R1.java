package me.espryth.easyjoin.v1_12_R1;

import io.netty.buffer.Unpooled;
import me.espryth.easyjoin.abstraction.NMS;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutCustomPayload;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NMS1_12_R1 implements NMS {
    @Override
    public void sendActionbar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    @Override
    public void sendTitle(Player player, String title, String subTitle, int fadeIn, int fadeShow, int fadeOut) {
        player.sendTitle(title, subTitle, fadeIn, fadeShow, fadeOut);
    }

    @Override
    public void openBook(Player player, ItemStack book) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(book);
        PacketPlayOutCustomPayload packetPlayOutCustomPayload = new PacketPlayOutCustomPayload("MC|BOpen",
                new PacketDataSerializer(Unpooled.buffer()));
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packetPlayOutCustomPayload);
        player.getInventory().setItemInMainHand(itemInHand);
    }
}
