package me.espryth.easyjoin.plugin.action.impl;

import me.espryth.easyjoin.plugin.action.AbstractAction;
import me.espryth.easyjoin.plugin.action.ActionQueue;
import org.bukkit.entity.Player;

public class ClearChatAction extends AbstractAction {
    @Override
    public void execute(Player player, ActionQueue queue) {
        for (int i = 0; i < Integer.parseInt(getLine()); i++) {
            player.sendMessage(" ");
        }
    }
}
