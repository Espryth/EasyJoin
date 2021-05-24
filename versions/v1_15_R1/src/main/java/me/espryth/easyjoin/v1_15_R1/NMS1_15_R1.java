package me.espryth.easyjoin.v1_15_R1;

import me.espryth.easyjoin.abstraction.NMS;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NMS1_15_R1 implements NMS {
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
        player.openBook(book);
    }
}
