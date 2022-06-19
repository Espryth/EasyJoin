package me.espryth.easyjoin.plugin.command;

import me.espryth.easyjoin.plugin.EasyJoin;
import me.espryth.easyjoin.plugin.utils.MessageUtils;
import me.espryth.easyjoin.plugin.utils.RepeatingAction;
import me.espryth.easyjoin.plugin.utils.YamlFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;

public class MainCommand implements CommandExecutor {

    private final EasyJoin plugin;
    private final YamlFile bookFile;

    public MainCommand() {
        plugin = CONTAINER.get(EasyJoin.class);
        bookFile = CONTAINER.get(YamlFile.class, "book");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender.hasPermission("easyjoin.admin")) {
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("reload")) {
                    plugin.reload();
                    RepeatingAction.cancelAll();
                    sender.sendMessage(MessageUtils.colorize("&aConfiguration reloaded!"));
                    return true;
                } else if(args[0].equalsIgnoreCase("setbook")) {
                    if(sender instanceof Player) {
                        Player player = (Player) sender;
                        ItemStack item = player.getItemInHand();
                        bookFile.set("book", item);
                        bookFile.save();
                        sender.sendMessage(MessageUtils.colorize("&aBook register!"));
                        return true;
                    }
                    sender.sendMessage("This command only can be executed by player");
                    return true;
                }
            }
            sender.sendMessage(MessageUtils.colorize("&e&lEasy&6&lJoin &f" + plugin.getDescription().getVersion() + " by Espryth"));
            sender.sendMessage(MessageUtils.colorize("&f- /ej reload"));
            sender.sendMessage(MessageUtils.colorize("&f- /ej setbook"));
            return true;
        }
        sender.sendMessage(MessageUtils.colorize("&e&lEasy&6&lJoin &f" + plugin.getDescription().getVersion() + " by Espryth"));
        return true;
    }
}