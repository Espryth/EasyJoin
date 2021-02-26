package me.espryth.easyjoin.action.impl;

import me.espryth.easyjoin.Core;
import me.espryth.easyjoin.action.AbstractAction;
import me.espryth.easyjoin.action.ActionType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BookAction extends AbstractAction {

    private final Core core;

    public BookAction(String line, Core core) {
        super(ActionType.BOOK, line);
        this.core = core;
    }

    @Override
    public void execute(Player player) {
        if(!core.getFilesManager().getBook().contains("book")) return;
        Bukkit.getScheduler().runTaskLater(core, ()-> {
            core.getNmsSetup().getNMSHandler().openBook(player, core.getFilesManager().getBook().getItemStack("book"));
        }, Integer.parseInt(getLine()));
    }
}
