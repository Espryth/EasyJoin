package io.github.espryth.easyjoin;

import io.github.espryth.easyjoin.commands.MainCommand;
import io.github.espryth.easyjoin.file.FilesManager;
import io.github.espryth.easyjoin.listeners.AuthMeListener;
import io.github.espryth.easyjoin.listeners.JoinListener;
import io.github.espryth.easyjoin.listeners.QuitListener;
import io.github.espryth.easyjoin.util.nms.NMSSetup;
import io.github.espryth.easyjoin.util.nms.SimpleNMSSetup;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {

    private NMSSetup nmsSetup;
    private FilesManager filesManager;

    @Override
    public void onEnable() {
        nmsSetup = new SimpleNMSSetup();
        nmsSetup.enableNMS();
        filesManager = new FilesManager(this);
        filesManager.setup();
        registerListeners();
        registerCommands();
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new JoinListener(this), this);
        pm.registerEvents(new QuitListener(this), this);
        if (filesManager.getConfig().getBoolean("AuthMeHook")) {
            pm.registerEvents(new AuthMeListener(this), this);
        }
    }

    private void registerCommands() {
        getCommand("easyjoin").setExecutor(new MainCommand(this));
    }

    public NMSSetup getNmsSetup() {
        return nmsSetup;
    }

    public FilesManager getFilesManager() {
        return filesManager;
    }
}
