package me.espryth.easyjoin.action;

public enum ActionType {
    BROADCAST("[BROADCAST] "),
    JSON_BROADCAST("[JSON_BROADCAST] "),
    MESSAGE("[MESSAGE] "),
    JSON_MESSAGE("[JSON_MESSAGE] "),
    TITLE("[TITLE] "),
    BROADCAST_TITLE("[BROADCAST_TITLE] "),
    ACTIONBAR("[ACTIONBAR] "),
    BROADCAST_ACTIONBAR("[BROADCAST_ACTIONBAR] "),
    SOUND("[SOUND] "),
    BROADCAST_SOUND("[BROADCAST_SOUND] "),
    CLEARCHAT("[CLEARCHAT] "),
    BOOK("[BOOK] "),
    PLAYER_COMMAND("[PLAYER] "),
    CONSOLE_COMMAND("[CONSOLE] "),
    FIREWORK("[FIREWORK] ");


    private final String identifier;

    ActionType(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
