package me.espryth.easyjoin.v1_8_R3;

import io.netty.buffer.Unpooled;
import me.espryth.easyjoin.abstraction.NMS;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NMS1_8_R3 implements NMS {

    public void sendActionbar(Player player, String message) {
        IChatBaseComponent actionbarText = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(actionbarText, (byte) 2);
        sendPacket(player, packetPlayOutChat);
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
        ItemStack itemInHand = player.getItemInHand();
        player.setItemInHand(book);
        PacketPlayOutCustomPayload packetPlayOutCustomPayload = new PacketPlayOutCustomPayload("MC|BOpen",
                new PacketDataSerializer(Unpooled.buffer()));
        sendPacket(player, packetPlayOutCustomPayload);
        player.setItemInHand(itemInHand);
    }

    private void sendPacket(Player player, Object object) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet) object);
    }

}
