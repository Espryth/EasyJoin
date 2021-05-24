package me.espryth.easyjoin.plugin.module;

import me.espryth.commons.universal.module.AbstractModule;
import me.espryth.easyjoin.plugin.action.Action;
import me.espryth.easyjoin.plugin.action.impl.*;

public class ActionModule extends AbstractModule {
    @Override
    public void configure() {
        bind(Action.class, new ActionbarAction(), "[ACTIONBAR]");
        bind(Action.class, new BroadcastActionbarAction(), "[BROADCAST_ACTIONBAR]");
        bind(Action.class, new BroadcastAction(), "[BROADCAST]");
        bind(Action.class, new JsonBroadcastAction(), "[JSON_BROADCAST]");
        bind(Action.class, new MessageAction(), "[MESSAGE]");
        bind(Action.class, new JsonMessageAction(), "[JSON_MESSAGE]");
        bind(Action.class, new TitleAction(), "[TITLE]");
        bind(Action.class, new BroadcastTitle(), "[BROADCAST_TITLE]");
        bind(Action.class, new SoundAction(), "[SOUND]");
        bind(Action.class, new BroadcastSound(), "[BROADCAST_SOUND]");
        bind(Action.class, new ClearChatAction(), "[CLEARCHAT]");
        bind(Action.class, new BookAction(), "[BOOK]");
        bind(Action.class, new PlayerCommandAction(), "[PLAYER]");
        bind(Action.class, new ConsoleCommandAction(), "[CONSOLE]");
        bind(Action.class, new FireworkAction(), "[FIREWORK]");
    }
}
