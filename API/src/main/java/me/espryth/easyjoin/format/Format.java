package me.espryth.easyjoin.format;

import me.espryth.easyjoin.action.Action;

import java.util.List;

public interface Format {

    String getIdentifier();

    int getPriority();

    int getDelay();

    void setDelay(int delay);

    List<Action> getJoinActions();

    List<Action> getQuitActions();

    List<Action> getAuthMeActions();

    boolean isFirstJoinFormat();
}
