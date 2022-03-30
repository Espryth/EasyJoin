package me.espryth.easyjoin.plugin.utils;

import me.espryth.easyjoin.plugin.action.ActionExecutionException;

public class Title {

  private final String title;
  private final String subtitle;
  private final int fadeIn;
  private final int fadeShow;
  private final int fadeOut;

  public Title(
    String title,
    String subtitle,
    int fadeIn,
    int fadeShow,
    int fadeOut
  ) {
    this.title = title;
    this.subtitle = subtitle;
    this.fadeIn = fadeIn;
    this.fadeShow = fadeShow;
    this.fadeOut = fadeOut;
  }

  public static Title parse(String[] args) throws ActionExecutionException{

    if(args.length < 5) {
      throw new ActionExecutionException("Incorrect size of arguments for title");
    }

    try {
      String title = args[0];
      String subtitle = args[1];
      int fadeIn = Integer.parseInt(args[2]);
      int fadeShow = Integer.parseInt(args[3]);
      int fadeOut = Integer.parseInt(args[4]);
      return new Title(
        title, subtitle, fadeIn,
        fadeShow, fadeOut
      );
    } catch (NumberFormatException e) {
      throw new ActionExecutionException("Fade isn't a number!");
    }
  }

  public String getTitle() {
    return title;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public int getFadeIn() {
    return fadeIn;
  }

  public int getFadeShow() {
    return fadeShow;
  }

  public int getFadeOut() {
    return fadeOut;
  }
}
