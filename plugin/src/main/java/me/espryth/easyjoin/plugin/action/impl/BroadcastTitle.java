package me.espryth.easyjoin.plugin.action.impl;

import me.espryth.easyjoin.adapt.TitleSender;
import me.espryth.easyjoin.plugin.action.AbstractAction;
import me.espryth.easyjoin.plugin.action.ActionExecutionException;
import me.espryth.easyjoin.plugin.action.ActionQueue;
import me.espryth.easyjoin.plugin.utils.MessageUtils;
import me.espryth.easyjoin.plugin.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;

public class BroadcastTitle extends AbstractAction {

    private final TitleSender titleSender;

    public BroadcastTitle() {
        titleSender = CONTAINER.get(TitleSender.class);
    }

    @Override
    public void execute(Player player, ActionQueue queue) throws ActionExecutionException{

        String[] values = MessageUtils.formatString(player, getLine()).split(";");

        Title title = Title.parse(values);

        Bukkit.getOnlinePlayers().forEach(p -> titleSender.send(
          p, title.getTitle(), title.getSubtitle(),
          title.getFadeIn(), title.getFadeShow(), title.getFadeOut())
        );
    }
}
