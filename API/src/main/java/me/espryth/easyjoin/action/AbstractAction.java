package me.espryth.easyjoin.action;

import me.espryth.easyjoin.util.ColorUtil;

public abstract class AbstractAction implements Action {

    private final ActionType type;

    private String line;

    public AbstractAction(ActionType type, String line) {
        this.type = type;
        this.line = ColorUtil.apply(line);
    }

    public ActionType getType() {
        return type;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
