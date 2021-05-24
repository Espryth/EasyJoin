package me.espryth.easyjoin.plugin.loader;

import me.espryth.easyjoin.plugin.listeners.PlayerFirstJoinListener;
import me.espryth.easyjoin.plugin.listeners.PlayerJoinListener;
import me.espryth.easyjoin.plugin.listeners.PlayerQuitListener;
import me.espryth.easyjoin.plugin.listeners.authme.LoginListener;
import me.espryth.easyjoin.plugin.listeners.authme.RegisterListener;
import me.espryth.easyjoin.plugin.utils.YamlFile;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;

public class ListenerLoader implements Loader {

    private final Plugin plugin;
    private final YamlFile configFile;

    public ListenerLoader() {
        plugin = CONTAINER.get(Plugin.class);
        configFile = CONTAINER.get(YamlFile.class, "config");
    }

    @Override
    public void load() {

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerJoinListener(), plugin);
        pm.registerEvents(new PlayerFirstJoinListener(), plugin);
        pm.registerEvents(new PlayerQuitListener(), plugin);

        if(configFile.getBoolean("AuthMeHook")) {
            pm.registerEvents(new LoginListener(), plugin);
            pm.registerEvents(new RegisterListener(), plugin);
        }
    }

}
