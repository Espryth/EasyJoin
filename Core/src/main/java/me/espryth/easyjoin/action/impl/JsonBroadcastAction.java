package me.espryth.easyjoin.action.impl;

import me.espryth.easyjoin.action.Action;
import me.espryth.easyjoin.action.ActionType;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class JsonBroadcastAction extends Action {

    public JsonBroadcastAction(String line) {
        super(ActionType.JSON_BROADCAST, line);
    }

    @Override
    public void execute(Player player) {
        setLine(PlaceholderAPI.setPlaceholders(player, getLine()));
        BaseComponent[] baseComponent = ComponentSerializer.parse(getLine());
        Bukkit.getOnlinePlayers().forEach(p -> p.spigot().sendMessage(baseComponent));
    }
}
