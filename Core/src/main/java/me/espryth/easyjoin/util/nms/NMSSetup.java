package me.espryth.easyjoin.util.nms;

import me.espryth.easyjoin.api.NMSHandler;

public interface NMSSetup {
    boolean enableNMS();

    NMSHandler getNMSHandler();

    String getServerVersion();
}
