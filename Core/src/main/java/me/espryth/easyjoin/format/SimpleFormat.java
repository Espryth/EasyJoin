package me.espryth.easyjoin.format;

import me.espryth.easyjoin.action.Action;

import java.util.ArrayList;
import java.util.List;

public class SimpleFormat implements Format {

    private final String identifier;

    private final int priority;

    private int delay;

    private final List<Action> joinActions, quitActions, authMeActions;

    private final boolean firstJoinFormat;

    public SimpleFormat(String identifier, int priority, boolean firstJoinFormat) {
        this.identifier = identifier;
        this.priority = priority;
        this.delay = 0;
        this.joinActions = new ArrayList<>();
        this.quitActions = new ArrayList<>();
        this.authMeActions = new ArrayList<>();
        this.firstJoinFormat = firstJoinFormat;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public int getDelay() {
        return delay;
    }

    @Override
    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public List<Action> getJoinActions() {
        return joinActions;
    }

    @Override
    public List<Action> getQuitActions() {
        return quitActions;
    }

    @Override
    public List<Action> getAuthMeActions() {
        return authMeActions;
    }

    @Override
    public boolean isFirstJoinFormat() {
        return firstJoinFormat;
    }
}
