package me.espryth.easyjoin.plugin.action.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.espryth.easyjoin.plugin.action.AbstractAction;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Player;

public class JsonMessageAction extends AbstractAction {
    @Override
    public void execute(Player player) {
        setLine(PlaceholderAPI.setPlaceholders(player, getLine()));
        BaseComponent[] baseComponent = ComponentSerializer.parse(getLine());
        player.spigot().sendMessage(baseComponent);
    }
}
