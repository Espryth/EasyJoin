package me.espryth.easyjoin.abstraction;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NMS {
    void sendActionbar(Player player, String message);

    void sendTitle(Player player, String title, String subTitle, int fadeIn, int fadeShow, int fadeOut);

    void openBook(Player player, ItemStack book);
}
