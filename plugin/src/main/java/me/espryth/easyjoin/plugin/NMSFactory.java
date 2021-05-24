package me.espryth.easyjoin.plugin;

import me.espryth.easyjoin.abstraction.NMS;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NMSFactory {

    private static final Constructor<?> NMS_CONSTRUCTOR;

    static {
        try {
            NMS_CONSTRUCTOR = Class
                    .forName("me.espryth.easyjoin.v" + EasyJoin.SERVER_VERSION + ".NMS" + EasyJoin.SERVER_VERSION)
                    .getConstructor();
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            throw new IllegalArgumentException("EasyJoin cannot be executed on this server version!");
        }
    }

    public static NMS getNMS() {
        try {
            return (NMS) NMS_CONSTRUCTOR.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("An error has occurred while getting the NMS", e);
        }
    }

}
