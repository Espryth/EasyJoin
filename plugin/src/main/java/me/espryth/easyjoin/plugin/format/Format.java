package me.espryth.easyjoin.plugin.format;

import me.espryth.easyjoin.plugin.action.Action;

import java.util.ArrayList;
import java.util.List;

public class Format {

    private final String identifier;
    private final int priority;
    private int delay;
    private final List<Action> joinActions;
    private final List<Action> quitActions;
    private final List<Action> authMeActions;
    private final boolean firstJoinFormat;

    public Format(String identifier,
                        int priority,
                        boolean firstJoinFormat) {
        this.identifier = identifier;
        this.priority = priority;
        this.delay = 0;
        this.joinActions = new ArrayList<>();
        this.quitActions = new ArrayList<>();
        this.authMeActions = new ArrayList<>();
        this.firstJoinFormat = firstJoinFormat;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getPriority() {
        return priority;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public List<Action> getJoinActions() {
        return joinActions;
    }

    public List<Action> getQuitActions() {
        return quitActions;
    }

    public List<Action> getAuthMeActions() {
        return authMeActions;
    }

    public boolean isFirstJoinFormat() {
        return firstJoinFormat;
    }
}
