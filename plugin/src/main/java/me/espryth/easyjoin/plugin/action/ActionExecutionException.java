package me.espryth.easyjoin.plugin.action;

public class ActionExecutionException extends RuntimeException {

  public ActionExecutionException(String message) {
    super("An error has occurred while executing a action ( " + message + " )");
  }

}
