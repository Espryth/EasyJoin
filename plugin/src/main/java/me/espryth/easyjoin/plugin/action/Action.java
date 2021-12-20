package me.espryth.easyjoin.plugin.action;

import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public interface Action {

    void execute(Player player, ActionQueue queue);

    String getLine();

    void setLine(String line);

    default Action duplicate() {
        try {
            return getClass().getConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException("An error has occurred when duplicate the action " + getClass().getName(), e);
        }
    }

}
