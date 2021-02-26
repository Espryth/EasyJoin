package me.espryth.easyjoin.action.impl;

import me.espryth.easyjoin.action.AbstractAction;
import me.espryth.easyjoin.action.ActionType;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Player;

public class JsonMessageAction extends AbstractAction {

    public JsonMessageAction(String line) {
        super(ActionType.JSON_MESSAGE, line);
    }

    @Override
    public void execute(Player player) {
        setLine(PlaceholderAPI.setPlaceholders(player, getLine()));
        BaseComponent[] baseComponent = ComponentSerializer.parse(getLine());
        player.spigot().sendMessage(baseComponent);
    }
}
