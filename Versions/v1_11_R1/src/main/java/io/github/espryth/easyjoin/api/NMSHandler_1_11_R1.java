package io.github.espryth.easyjoin.api;

import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NMSHandler_1_11_R1 implements NMSHandler{
    @Override
    public void sendTitle(Player player, String title, String subTitle, int fadeIn, int fadeShow, int fadedOut) {
        player.sendTitle(title, subTitle, fadeIn, fadeShow, fadedOut);
    }

    @Override
    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    @Override
    public void openBook(Player player, ItemStack book) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(book);
        PacketPlayOutCustomPayload packetPlayOutCustomPayload = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(Unpooled.buffer()));
        sendPacket(player, packetPlayOutCustomPayload);
        player.getInventory().setItemInMainHand(itemInHand);
    }

    @Override
    public void sendPacket(Player player, Object object) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet) object);
    }
}
