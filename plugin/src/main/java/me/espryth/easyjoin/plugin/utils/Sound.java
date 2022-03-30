package me.espryth.easyjoin.plugin.utils;

import me.espryth.easyjoin.plugin.action.ActionExecutionException;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Sound {

  private final org.bukkit.Sound sound;
  private final float pitch;
  private final float yaw;

  public Sound(org.bukkit.Sound sound, float pitch, float yaw) {
    this.sound = sound;
    this.pitch = pitch;
    this.yaw = yaw;
  }

  public static Sound parse(String[] args) throws ActionExecutionException{

    if(args.length < 3) {
      throw new ActionExecutionException("Incorrect size of arguments for sound");
    }

    try {
      org.bukkit.Sound sound = org.bukkit.Sound.valueOf(args[0]);
      float pitch = Float.parseFloat(args[1]);
      float volume = Float.parseFloat(args[2]);
      return new Sound(sound, pitch, volume);
    } catch (Exception e) {
      String exceptionMessage;
      if(e instanceof NumberFormatException) {
        exceptionMessage = "Pitch or volume isn't a number!";
      } else {
        exceptionMessage = "Invalid sound";
      }
      throw new ActionExecutionException(exceptionMessage);
    }
  }

  public void play(Player player) {
    play(player, player.getLocation());
  }

  public void play(Player player, Location location) {
    player.playSound(location, sound, pitch, yaw);
  }

}
