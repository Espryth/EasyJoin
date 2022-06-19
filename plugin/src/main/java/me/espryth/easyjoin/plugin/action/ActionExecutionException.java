package me.espryth.easyjoin.plugin.action;

public class ActionExecutionException extends Exception {

  public ActionExecutionException(String message) {
    super("An error has occurred while executing an action ( " + message + " )");
  }

}
