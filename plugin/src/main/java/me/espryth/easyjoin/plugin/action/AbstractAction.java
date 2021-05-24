package me.espryth.easyjoin.plugin.action;

public abstract class AbstractAction implements Action {

    private String line;

    @Override
    public String getLine() {
        return line;
    }

    @Override
    public void setLine(String line) {
        this.line = line;
    }

}
