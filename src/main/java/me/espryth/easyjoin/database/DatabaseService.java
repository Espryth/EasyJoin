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
    private volatile HikariDataSource dataSource;
    private volatile Jdbi jdbi;
    private volatile boolean enabled = false;

    @Inject
    public DatabaseService(Container<AppConfig> appConfig, Container<CredentialsConfig> credentialsConfig) {
        this.appConfig = appConfig;
        this.credentialsConfig = credentialsConfig;
    }

    @OnEnable
    public void setup(Logger logger) {
        shutdown(logger);
        AppConfig cfg = appConfig.get();
        if (cfg == null || !"MYSQL".equalsIgnoreCase(cfg.firstJoinMode())) {
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

            HikariDataSource ds = new HikariDataSource(config);
            Jdbi jdbiInstance = Jdbi.create(ds);
            jdbiInstance.installPlugin(new SqlObjectPlugin());
            jdbiInstance.useExtension(FirstJoinDao.class, FirstJoinDao::createTable);

            this.dataSource = ds;
            this.jdbi = jdbiInstance;
            this.enabled = true;
            logger.info("Database connection established successfully!");
        } catch (Exception e) {
            logger.error("Failed to initialize database connection", e);
            this.enabled = false;
        }
    }

    @OnDisable
    public void shutdown(Logger logger) {
        this.enabled = false;
        HikariDataSource ds = this.dataSource;
        this.dataSource = null;
        this.jdbi = null;
        if (ds != null && !ds.isClosed()) {
            ds.close();
            logger.info("Database connection closed");
        }
    }

    public boolean isEnabled() {
        HikariDataSource ds = this.dataSource;
        return enabled && ds != null && !ds.isClosed();
    }

    public Optional<Jdbi> getJdbi() {
        return isEnabled() ? Optional.ofNullable(jdbi) : Optional.empty();
    }
}
