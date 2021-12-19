package me.espryth.easyjoin.adapt.newer;

import me.espryth.easyjoin.adapt.BookSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NewerBookSender
  implements BookSender {

  @Override
  public void openBook(Player player, ItemStack book) {
    player.openBook(book);
  }
}
