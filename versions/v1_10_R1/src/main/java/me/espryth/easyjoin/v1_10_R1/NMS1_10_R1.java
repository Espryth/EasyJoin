package me.espryth.easyjoin.v1_10_R1;

import io.netty.buffer.Unpooled;
import me.espryth.easyjoin.abstraction.NMS;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_10_R1.*;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NMS1_10_R1 implements NMS {
    @Override
    public void sendActionbar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    @Override
    public void sendTitle(Player player, String title, String subTitle, int fadeIn, int fadeShow, int fadeOut) {
        IChatBaseComponent titleText = new ChatMessage(title);
        IChatBaseComponent subtitleText = new ChatMessage(subTitle);
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(
                PacketPlayOutTitle.EnumTitleAction.TITLE, titleText, fadeIn, fadeShow, fadeOut);

        PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(
                PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleText, fadeIn, fadeShow, fadeOut);

        sendPacket(player, titlePacket);
        sendPacket(player, subtitlePacket);
    }

    @Override
    public void openBook(Player player, ItemStack book) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(book);
        PacketPlayOutCustomPayload packetPlayOutCustomPayload = new PacketPlayOutCustomPayload("MC|BOpen",
                new PacketDataSerializer(Unpooled.buffer()));
        sendPacket(player, packetPlayOutCustomPayload);
        player.getInventory().setItemInMainHand(itemInHand);
    }
    private void sendPacket(Player player, Object object) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet) object);
    }

}
