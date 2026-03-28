package me.espryth.easyjoin.config;

import com.thewinterframework.configurate.config.Configurate;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
@Configurate("credentials")
public record CredentialsConfig(
        String host,
        int port,
        String database,
        String username,
        String password,
        @Setting("use-ssl") boolean useSsl
) {}
