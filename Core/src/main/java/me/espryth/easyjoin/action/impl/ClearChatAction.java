package me.espryth.easyjoin.action.impl;

import me.espryth.easyjoin.action.Action;
import me.espryth.easyjoin.action.ActionType;
import org.bukkit.entity.Player;

public class ClearChatAction extends Action {

    public ClearChatAction(String line) {
        super(ActionType.CLEARCHAT, line);
    }

    @Override
    public void execute(Player player) {
        for (int i = 0; i < Integer.parseInt(getLine()); i++) {
            player.sendMessage(" ");
        }
    }
}
