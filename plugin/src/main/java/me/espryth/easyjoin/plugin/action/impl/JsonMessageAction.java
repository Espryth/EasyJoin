package me.espryth.easyjoin.plugin.action.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.espryth.easyjoin.plugin.action.AbstractAction;
import me.espryth.easyjoin.plugin.utils.MessageUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Player;

public class JsonMessageAction extends AbstractAction {
    @Override
    public void execute(Player player) {

        String newLine = MessageUtils.formatString(player, getLine());

        BaseComponent[] components;

        if(getLine().startsWith("<c>")) {
            components = MessageUtils.getCenteredComponents(
                    ComponentSerializer.parse(newLine.replace("<c>", "")));
        } else {
            components = ComponentSerializer.parse(newLine);
        }

        player.spigot().sendMessage(components);
    }
}
