package me.espryth.easyjoin;

import com.google.inject.Binder;
import com.google.inject.Scopes;
import com.thewinterframework.paper.PaperWinterPlugin;
import com.thewinterframework.plugin.WinterBootPlugin;
import com.thewinterframework.service.annotation.Service;

@WinterBootPlugin
public final class EasyJoinPlugin extends PaperWinterPlugin {

    private static EasyJoinPlugin instance;

    public static <T> T getService(Class<T> type) {
        return instance.getInjector().getInstance(type);
    }

    @Override
    public void onPluginLoad() {
        super.onPluginLoad();
        instance = this;
    }

    @Override
    public void configure(Binder binder) {
        binder.bindScope(Service.class, Scopes.SINGLETON);
    }
}
