package me.espryth.easyjoin.plugin.action.impl;

import me.espryth.easyjoin.plugin.action.AbstractAction;
import org.bukkit.entity.Player;

public class ClearChatAction extends AbstractAction {
    @Override
    public void execute(Player player) {
        for (int i = 0; i < Integer.parseInt(getLine()); i++) {
            player.sendMessage(" ");
        }
    }
}
