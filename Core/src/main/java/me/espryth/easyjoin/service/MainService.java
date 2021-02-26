package me.espryth.easyjoin.service;

import me.espryth.easyjoin.Core;
import me.espryth.easyjoin.commands.MainCommand;
import me.espryth.easyjoin.listeners.PlayerJoinListener;
import me.espryth.easyjoin.listeners.PlayerQuitListener;
import me.espryth.easyjoin.listeners.authme.LoginListener;
import me.espryth.easyjoin.listeners.authme.RegisterListener;
import me.espryth.easyjoin.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class MainService implements Service {

    private final Core core;

    public MainService(Core core) {
        this.core = core;
    }

    @Override
    public void start() {
        registerCommands();
        registerListeners();
        sendInitMessage();
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

    public void sendInitMessage() {
        Bukkit.getConsoleSender().sendMessage(ColorUtil.apply("&6| &6|"));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.apply("&6| &e| &eEasyJoin &f2.1.0 by Espryth"));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.apply("&6| &6|"));
    }
}
