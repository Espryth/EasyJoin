package me.espryth.easyjoin.plugin.format;

import me.espryth.easyjoin.plugin.action.Action;
import me.espryth.easyjoin.plugin.action.ActionType;

import java.util.ArrayList;
import java.util.List;

public class Format {

    private final String identifier;
    private final int priority;
    private final List<Action> joinActions;
    private final List<Action> quitActions;
    private final List<Action> authActions;
    private final boolean firstJoinFormat;

    public Format(String identifier,
                        int priority,
                        boolean firstJoinFormat) {
        this.identifier = identifier;
        this.priority = priority;
        this.joinActions = new ArrayList<>();
        this.quitActions = new ArrayList<>();
        this.authActions = new ArrayList<>();
        this.firstJoinFormat = firstJoinFormat;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getPriority() {
        return priority;
    }


    public List<Action> getActions(ActionType actionType) {
        switch (actionType) {
            case AUTH:
                return authActions;
            case JOIN:
                return joinActions;
            case QUIT:
                return quitActions;
        }
        return null;
    }

    public List<Action> getJoinActions() {
        return joinActions;
    }

    public List<Action> getQuitActions() {
        return quitActions;
    }

    public List<Action> getAuthActions() {
        return authActions;
    }

    public boolean isFirstJoinFormat() {
        return firstJoinFormat;
    }
}
