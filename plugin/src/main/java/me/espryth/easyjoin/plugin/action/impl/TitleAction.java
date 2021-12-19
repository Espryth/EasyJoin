package me.espryth.easyjoin.plugin.action.impl;

import me.espryth.easyjoin.adapt.TitleSender;
import me.espryth.easyjoin.plugin.action.AbstractAction;
import me.espryth.easyjoin.plugin.utils.MessageUtils;
import org.bukkit.entity.Player;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;

public class TitleAction extends AbstractAction {

    private final TitleSender titleSender;

    public TitleAction() {
        titleSender = CONTAINER.get(TitleSender.class);
    }

    @Override
    public void execute(Player player) {

        String[] values = MessageUtils.formatString(player, getLine()).split(";");

        titleSender.send(
                player, values[0], values[1],
                Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4]));
    }
}
