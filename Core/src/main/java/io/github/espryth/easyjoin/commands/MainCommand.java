package io.github.espryth.easyjoin.commands;

import io.github.espryth.easyjoin.Core;
import io.github.espryth.easyjoin.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainCommand implements CommandExecutor {

    private final Core plugin;

    public MainCommand(Core plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender.hasPermission("easyjoin.admin")) {
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("reload")) {
                    plugin.getFilesManager().getConfig().reload();
                    plugin.getFilesManager().getBook().reload();
                    sender.sendMessage(ColorUtil.apply("&aConfiguration reloaded!"));

                    return true;
                } else if(args[0].equalsIgnoreCase("setbook")) {
                    if(sender instanceof Player) {
                        Player player = (Player) sender;
                        ItemStack item = player.getItemInHand();
                        plugin.getFilesManager().getBook().set("book", item);
                        plugin.getFilesManager().getBook().save();
                        sender.sendMessage(ColorUtil.apply("&aBook register!"));
                        return true;
                    }
                    sender.sendMessage("This command only can be executed by player");
                    return true;
                }
            }
            sender.sendMessage(ColorUtil.apply("&e&lEasy&6&lJoin &f" + plugin.getDescription().getVersion() + " by Espryth"));
            sender.sendMessage(ColorUtil.apply("&f- /ej reload"));
            sender.sendMessage(ColorUtil.apply("&f- /ej setbook"));
            return true;
        }
        sender.sendMessage(ColorUtil.apply("&e&lEasy&6&lJoin &f" + plugin.getDescription().getVersion() + " by Espryth"));
        return true;
    }
}
