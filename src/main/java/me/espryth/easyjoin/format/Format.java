package me.espryth.easyjoin.format;

import me.espryth.easyjoin.action.Action;

import java.util.List;

public record Format(
        String id,
        int priority,
        boolean isFirstJoinFormat,
        List<Action> joinActions,
        List<Action> quitActions
) {}
