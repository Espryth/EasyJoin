package io.github.espryth.easyjoin.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NMSHandler {

    void sendTitle(Player player, String title, String subTitle, int fadeIn, int fadeShow, int fadedOut);

    void sendActionBar(Player player, String message);

    void openBook(Player player, ItemStack book);

    void sendPacket(Player player, Object object);
}
