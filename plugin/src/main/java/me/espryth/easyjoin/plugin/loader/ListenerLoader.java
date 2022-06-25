package me.espryth.easyjoin.plugin.loader;

import me.espryth.easyjoin.plugin.hook.AuthMeHook;
import me.espryth.easyjoin.plugin.hook.nLoginHook;
import me.espryth.easyjoin.plugin.listeners.PlayerFirstJoinListener;
import me.espryth.easyjoin.plugin.listeners.PlayerJoinListener;
import me.espryth.easyjoin.plugin.listeners.PlayerQuitListener;
import me.espryth.easyjoin.plugin.listeners.authme.PlayerLoginListener;
import me.espryth.easyjoin.plugin.listeners.authme.PlayerRegisterListener;
import me.espryth.easyjoin.plugin.utils.YamlFile;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.logging.Level;

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

        String hook = configFile.getString("Hook", "");

        if (Bukkit.getPluginManager().getPlugin("AuthMe") != null) {
            pm.registerEvents(new AuthMeHook(), plugin);
            plugin.getLogger().log(Level.INFO, "Successfully hooked with AuthMe");
        } else if(Bukkit.getPluginManager().getPlugin("nLogin") != null) {
            pm.registerEvents(new nLoginHook(), plugin);
            plugin.getLogger().log(Level.INFO, "Successfully hooked with nLogin");
        }

        pm.registerEvents(new PlayerLoginListener(), plugin);
        pm.registerEvents(new PlayerRegisterListener(), plugin);

    }

}
