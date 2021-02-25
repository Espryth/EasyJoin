package me.espryth.easyjoin.service;

import me.espryth.easyjoin.Core;
import me.espryth.easyjoin.commands.MainCommand;
import me.espryth.easyjoin.listeners.PlayerJoinListener;
import me.espryth.easyjoin.listeners.PlayerQuitListener;
import me.espryth.easyjoin.listeners.authme.LoginListener;
import me.espryth.easyjoin.listeners.authme.RegisterListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class MainService implements Service {

    private final Service formatService;

    private final Core core;

    public MainService(Core core) {
        this.core = core;
        this.formatService = new FormatService(core);
    }

    @Override
    public void start() {
        registerCommands();
        registerListeners();
        formatService.start();
    }

    private void registerCommands() {
        core.getCommand("easyjoin").setExecutor(new MainCommand(core));
    }

    private void registerListeners() {

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerJoinListener(core), core);
        pm.registerEvents(new PlayerQuitListener(core), core);

        if(core.getFilesManager().getConfig().getBoolean("AuthMeHook")) {
            pm.registerEvents(new LoginListener(core), core);
            pm.registerEvents(new RegisterListener(core), core);
        }
    }
}
