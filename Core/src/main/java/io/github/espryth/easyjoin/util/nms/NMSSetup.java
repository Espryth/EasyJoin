package io.github.espryth.easyjoin.util.nms;

import io.github.espryth.easyjoin.api.NMSHandler;

public interface NMSSetup {
    boolean enableNMS();

    NMSHandler getNMSHandler();

    String getServerVersion();
}
