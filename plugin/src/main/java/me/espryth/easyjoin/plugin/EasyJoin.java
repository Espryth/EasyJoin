package me.espryth.easyjoin.plugin;

import dev.henko.storance.Container;
import dev.henko.storance.ModuleInjector;
import dev.henko.storance.impl.ModuleInjectorImpl;
import me.espryth.easyjoin.plugin.command.MainCommand;
import me.espryth.easyjoin.plugin.format.Format;
import me.espryth.easyjoin.plugin.loader.FormatLoader;
import me.espryth.easyjoin.plugin.loader.ListenerLoader;
import me.espryth.easyjoin.plugin.loader.Loader;
import me.espryth.easyjoin.plugin.module.MainModule;
import me.espryth.easyjoin.plugin.utils.MessageUtils;
import me.espryth.easyjoin.plugin.utils.YamlFile;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class EasyJoin extends JavaPlugin {

    private static final ModuleInjector INJECTOR = new ModuleInjectorImpl();
    public static final Container CONTAINER = INJECTOR.getContainer();

    private final Map<String, Format> formatMap = new HashMap<>();

    private Loader listenerLoader;
    private Loader formatLoader;

    @Override
    public void onLoad() {
        INJECTOR.install(new MainModule(this));
        listenerLoader = new ListenerLoader();
        formatLoader = new FormatLoader();
    }

    @Override
    public void onEnable() {
        listenerLoader.load();
        formatLoader.load();
        sendInitMessage();
        getCommand("easyjoin").setExecutor(new MainCommand());
    }

    public void reload() {
        CONTAINER.get(YamlFile.class, "config").reload();
        CONTAINER.get(YamlFile.class, "book").reload();
        formatMap.clear();
        formatLoader.load();
    }

    public void sendInitMessage() {
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorize("&6| &6|"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorize("&6| &e| &eEasyJoin &f"+ getDescription().getVersion()+" by Espryth"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorize("&6| &6|"));
    }

    public Map<String, Format> getFormatMap() {
        return formatMap;
    }
}
