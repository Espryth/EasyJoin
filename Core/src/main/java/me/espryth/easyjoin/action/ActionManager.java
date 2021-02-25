package me.espryth.easyjoin.action;

import me.espryth.easyjoin.Core;
import io.github.espryth.easyjoin.action.impl.*;
import me.espryth.easyjoin.action.impl.*;

public class ActionManager {

    private Core core;

    public Action getActionByType(ActionType type, String line) {
        switch (type) {
            case ACTIONBAR:
                return new ActionbarAction(line, core);
            case BROADCAST_ACTIONBAR:
                return new BroadcastActionbarAction(line, core);
            case MESSAGE:
                return new MessageAction(line);
            case JSON_MESSAGE:
                return new JsonMessageAction(line);
            case BROADCAST:
                return new BroadcastAction(line);
            case JSON_BROADCAST:
                return new JsonBroadcastAction(line);
            case TITLE:
                return new TitleAction(line, core);
            case BROADCAST_TITLE:
                return new BroadcastTitleAction(line, core);
            case BOOK:
                return new BookAction(line, core);
            case SOUND:
                return new SoundAction(line);
            case BROADCAST_SOUND:
                return new BroadcastSound(line);
            case CLEARCHAT:
                return new ClearChatAction(line);
            case PLAYER_COMMAND:
                return new PlayerCommandAction(line);
            case CONSOLE_COMMAND:
                return new ConsoleCommandAction(line);

        }
        return null;
    }
}
