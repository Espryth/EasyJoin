package me.espryth.easyjoin.plugin.module;

import dev.henko.storance.Binder;
import dev.henko.storance.StoranceModule;
import me.espryth.easyjoin.plugin.EasyJoin;
import me.espryth.easyjoin.plugin.format.FormatExecutor;
import me.espryth.easyjoin.plugin.utils.YamlFile;
import org.bukkit.plugin.Plugin;

public class MainModule implements StoranceModule {

    private final EasyJoin plugin;

    public MainModule(EasyJoin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void configure(Binder binder) {

        binder.bind(Plugin.class).toInstance(plugin);
        binder.bind(EasyJoin.class).toInstance(plugin);

        binder.install(AdapterModuleFactory.create());

        YamlFile config = new YamlFile(plugin, "config");
        YamlFile credentials = new YamlFile(plugin, "credentials");
        binder.bind(YamlFile.class).named("config").toInstance(config);
        binder.bind(YamlFile.class).named("book").toInstance(new YamlFile(plugin, "book"));

        binder.bind(FormatExecutor.class).toInstance(new FormatExecutor());

        binder.install(new ActionModule());
        binder.install(new FirstJoinCheckerModule(config, credentials));
    }
}
