package me.espryth.easyjoin.plugin.action.impl;

import me.espryth.easyjoin.plugin.action.AbstractAction;
import me.espryth.easyjoin.plugin.action.ActionQueue;
import me.espryth.easyjoin.plugin.utils.MessageUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class JsonBroadcastAction extends AbstractAction {
    @Override
    public void execute(Player player, ActionQueue queue) {

        String newLine = MessageUtils.formatString(player, getLine());

        BaseComponent[] components;

        if(getLine().startsWith("<c>")) {
            components = MessageUtils.getCenteredComponents(
                    ComponentSerializer.parse(newLine.replace("<c>", "")));
        } else {
            components = ComponentSerializer.parse(newLine);
        }

        Bukkit.getOnlinePlayers().forEach(p -> p.spigot().sendMessage(components));
    }
}
