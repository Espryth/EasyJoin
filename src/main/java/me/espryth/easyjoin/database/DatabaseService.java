package me.espryth.easyjoin.database;

import com.thewinterframework.configurate.Container;
import com.thewinterframework.service.annotation.Service;
import com.thewinterframework.service.annotation.lifecycle.OnDisable;
import com.thewinterframework.service.annotation.lifecycle.OnEnable;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.inject.Inject;
import me.espryth.easyjoin.config.AppConfig;
import me.espryth.easyjoin.config.CredentialsConfig;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;

import java.util.Optional;

@Service
public class DatabaseService {

    private final Container<AppConfig> appConfig;
    private final Container<CredentialsConfig> credentialsConfig;
    private HikariDataSource dataSource;
    private Jdbi jdbi;
    private boolean enabled = false;

    @Inject
    public DatabaseService(Container<AppConfig> appConfig, Container<CredentialsConfig> credentialsConfig) {
        this.appConfig = appConfig;
        this.credentialsConfig = credentialsConfig;
    }

    @OnEnable
    public void setup(Logger logger) {
        if (!appConfig.get().firstJoinMode().equalsIgnoreCase("MYSQL")) {
            logger.info("Database mode is not MYSQL, skipping database initialization");
            return;
        }

        try {
            CredentialsConfig creds = credentialsConfig.get();

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(String.format(
                "jdbc:mysql://%s:%d/%s?useSSL=%s&allowPublicKeyRetrieval=true",
                creds.host(), creds.port(), creds.database(), creds.useSsl()
            ));
            config.setUsername(creds.username());
            config.setPassword(creds.password());
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(300000);
            config.setConnectionTimeout(10000);
            config.setPoolName("EasyJoin-Pool");

            this.dataSource = new HikariDataSource(config);
            this.jdbi = Jdbi.create(dataSource);
            this.jdbi.installPlugin(new SqlObjectPlugin());

            jdbi.useExtension(FirstJoinDao.class, FirstJoinDao::createTable);

            this.enabled = true;
            logger.info("Database connection established successfully!");

        } catch (Exception e) {
            logger.error("Failed to initialize database connection", e);
            this.enabled = false;
        }
    }

    @OnDisable
    public void shutdown(Logger logger) {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Database connection closed");
        }
    }

    public boolean isEnabled() {
        return enabled && dataSource != null && !dataSource.isClosed();
    }

    public Optional<Jdbi> getJdbi() {
        return isEnabled() ? Optional.of(jdbi) : Optional.empty();
    }
}
