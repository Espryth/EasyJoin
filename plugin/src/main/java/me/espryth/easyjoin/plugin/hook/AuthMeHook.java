package me.espryth.easyjoin.plugin.hook;

import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.RegisterEvent;
import me.espryth.easyjoin.plugin.event.PlayerLoginEvent;
import me.espryth.easyjoin.plugin.event.PlayerRegisterEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AuthMeHook
  implements Listener {

  @EventHandler
  public void handleLogin(LoginEvent event) {
    Bukkit.getPluginManager().callEvent(new PlayerLoginEvent(event.getPlayer()));
  }

  @EventHandler
  public void handleRegister(RegisterEvent event) {
    Bukkit.getPluginManager().callEvent(new PlayerRegisterEvent(event.getPlayer()));
  }
}
