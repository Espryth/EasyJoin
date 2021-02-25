package me.espryth.easyjoin.action;

import me.espryth.easyjoin.util.ColorUtil;
import org.bukkit.entity.Player;

public class Action {

    private final ActionType type;

    private String line;

    public Action(ActionType type, String line) {
        this.type = type;
        this.line = ColorUtil.apply(line);
    }

    public void execute(Player player){}

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
