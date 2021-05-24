package me.espryth.easyjoin.plugin.action.impl;

import me.espryth.easyjoin.abstraction.NMS;
import me.espryth.easyjoin.plugin.action.AbstractAction;
import me.espryth.easyjoin.plugin.utils.YamlFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;

public class BookAction extends AbstractAction {

    private final Plugin plugin;
    private final YamlFile bookFile;
    private final NMS nms;

    public BookAction() {
        plugin = CONTAINER.get(Plugin.class);
        bookFile = CONTAINER.get(YamlFile.class, "book");
        nms = CONTAINER.get(NMS.class);
    }

    @Override
    public void execute(Player player) {

        if(!bookFile.contains("book")) return;

        Bukkit.getScheduler().runTaskLater(plugin, () ->
                nms.openBook(player, bookFile.getItemStack("book")),
                Integer.parseInt(getLine()));
    }
}
