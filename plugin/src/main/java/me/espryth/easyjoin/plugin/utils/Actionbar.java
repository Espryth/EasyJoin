package me.espryth.easyjoin.plugin.utils;

import me.espryth.easyjoin.plugin.action.ActionExecutionException;

public class Actionbar {

  private final String text;
  private final int fadeShow;

  public Actionbar(
    String text,
    int fadeShow
  ) {
    this.text = text;
    this.fadeShow = fadeShow;
  }

  public static Actionbar parse(String[] args) throws ActionExecutionException {

    if(args.length < 2) {
      throw new ActionExecutionException("Incorrect size of arguments for actionbar");
    }

    try {
      String text = args[0];
      int fadeShow = Integer.parseInt(args[1]);
      return new Actionbar(text, fadeShow);
    } catch (NumberFormatException e) {
      throw new ActionExecutionException("Fade show isn't a number!");
    }
  }

  public String getText() {
    return text;
  }

  public int getFadeShow() {
    return fadeShow;
  }
}
