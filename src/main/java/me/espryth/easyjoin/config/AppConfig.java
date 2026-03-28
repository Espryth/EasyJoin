package me.espryth.easyjoin.config;

import com.thewinterframework.configurate.config.Configurate;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Map;

@ConfigSerializable
@Configurate("config")
public record AppConfig(
        @Setting("FirstJoinMode") String firstJoinMode,
        @Setting("Formats") Map<String, FormatSection> formats
) {
    @ConfigSerializable
    public record FormatSection(
            int priority,
            @Setting("isFirstJoinFormat") boolean isFirstJoinFormat,
            @Setting("join") ActionSection join,
            @Setting("quit") ActionSection quit
    ) {}

    @ConfigSerializable
    public record ActionSection(
            java.util.List<String> actions
    ) {}
}
