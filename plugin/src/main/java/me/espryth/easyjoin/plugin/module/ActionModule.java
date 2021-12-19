package me.espryth.easyjoin.plugin.module;

import dev.henko.storance.Binder;
import dev.henko.storance.StoranceModule;
import me.espryth.easyjoin.plugin.action.Action;
import me.espryth.easyjoin.plugin.action.impl.*;

public class ActionModule implements StoranceModule {

    @Override
    public void configure(Binder binder) {
        binder.bind(Action.class).named("[ACTIONBAR]").toInstance(new ActionbarAction());
        binder.bind(Action.class).named("[BROADCAST_ACTIONBAR]").toInstance(new BroadcastActionbarAction());
        binder.bind(Action.class).named("[BROADCAST]").toInstance(new BroadcastAction());
        binder.bind(Action.class).named("[JSON_BROADCAST]").toInstance(new JsonBroadcastAction());
        binder.bind(Action.class).named("[MESSAGE]").toInstance(new MessageAction());
        binder.bind(Action.class).named("[JSON_MESSAGE]").toInstance(new JsonMessageAction());
        binder.bind(Action.class).named("[TITLE]").toInstance(new TitleAction());
        binder.bind(Action.class).named("[BROADCAST_TITLE]").toInstance(new BroadcastTitle());
        binder.bind(Action.class).named("[SOUND]").toInstance(new SoundAction());
        binder.bind(Action.class).named("[BROADCAST_SOUND]").toInstance(new BroadcastSound());
        binder.bind(Action.class).named("[CLEARCHAT]").toInstance(new ClearChatAction());
        binder.bind(Action.class).named("[BOOK]").toInstance(new BookAction());
        binder.bind(Action.class).named("[PLAYER]").toInstance( new PlayerCommandAction());
        binder.bind(Action.class).named("[CONSOLE]").toInstance(new ConsoleCommandAction());
        binder.bind(Action.class).named("[FIREWORK]").toInstance(new FireworkAction());
    }

}
