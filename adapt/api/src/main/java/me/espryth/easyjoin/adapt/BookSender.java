package me.espryth.easyjoin.adapt;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface BookSender {

  void send(Player player, ItemStack book);

}
