package me.espryth.easyjoin;

import com.thewinterframework.paper.PaperWinterPlugin;
import com.thewinterframework.plugin.WinterBootPlugin;

@WinterBootPlugin
public final class EasyJoinPlugin extends PaperWinterPlugin {

    private static EasyJoinPlugin instance;

    public static <T> T getService(Class<T> type) {
        return instance.getInjector().getInstance(type);
    }

    @Override
    public void onPluginLoad() {
        instance = this;
    }
}
