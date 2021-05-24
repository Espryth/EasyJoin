package me.espryth.easyjoin.plugin.module;

import me.espryth.commons.universal.module.AbstractModule;
import me.espryth.easyjoin.abstraction.NMS;
import me.espryth.easyjoin.plugin.EasyJoin;
import me.espryth.easyjoin.plugin.NMSFactory;
import me.espryth.easyjoin.plugin.format.FormatExecutor;
import me.espryth.easyjoin.plugin.utils.YamlFile;
import org.bukkit.plugin.Plugin;

public class MainModule extends AbstractModule {

    private final EasyJoin plugin;

    public MainModule(EasyJoin plugin) {
        this.plugin = plugin;
    }


    @Override
    public void configure() {

        bind(Plugin.class, plugin);
        bind(EasyJoin.class, plugin);

        bind(NMS.class, NMSFactory.getNMS());

        bind(YamlFile.class, new YamlFile(plugin, "config"), "config");
        bind(YamlFile.class, new YamlFile(plugin, "book"), "book");

        bind(FormatExecutor.class, new FormatExecutor());

        install(new ActionModule());
    }
}
