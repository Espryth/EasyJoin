package me.espryth.easyjoin.service;

import com.thewinterframework.configurate.Container;
import com.thewinterframework.service.annotation.Service;
import jakarta.inject.Inject;
import me.espryth.easyjoin.config.AppConfig;
import me.espryth.easyjoin.database.DatabaseService;
import me.espryth.easyjoin.database.FirstJoinDao;
import org.bukkit.entity.Player;
import org.slf4j.Logger;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FirstJoinService {

    private final Container<AppConfig> appConfig;
    private final DatabaseService databaseService;
    private final Logger logger;
    private final Set<UUID> cache = ConcurrentHashMap.newKeySet();

    @Inject
    public FirstJoinService(Container<AppConfig> appConfig, DatabaseService databaseService, Logger logger) {
        this.appConfig = appConfig;
        this.databaseService = databaseService;
        this.logger = logger;
    }

    public CompletableFuture<Boolean> isFirstJoin(Player player) {
        boolean playedBefore = player.hasPlayedBefore();
        if (!isMysqlMode()) {
            return CompletableFuture.completedFuture(!playedBefore);
        }

        UUID uuid = player.getUniqueId();
        if (cache.contains(uuid)) {
            return CompletableFuture.completedFuture(false);
        }

        String uuidStr = uuid.toString();
        return CompletableFuture.supplyAsync(() -> {
            if (!databaseService.isEnabled()) {
                return !playedBefore;
            }

            return databaseService.getJdbi()
                    .map(jdbi -> jdbi.withExtension(FirstJoinDao.class, dao -> {
                        boolean exists = dao.exists(uuidStr);
                        if (exists) {
                            cache.add(uuid);
                        }
                        return !exists;
                    }))
                    .orElse(!playedBefore);
        }).exceptionally(ex -> {
            logger.warn("Failed to check first join status for {}", uuidStr, ex);
            return !playedBefore;
        });
    }

    public void markAsJoined(Player player) {
        if (!isMysqlMode()) {
            return;
        }

        UUID uuid = player.getUniqueId();
        String uuidStr = uuid.toString();
        cache.add(uuid);

        CompletableFuture.runAsync(() -> {
            if (databaseService.isEnabled()) {
                databaseService.getJdbi().ifPresent(jdbi ->
                        jdbi.useExtension(FirstJoinDao.class, dao -> dao.insert(uuidStr))
                );
            }
        }).exceptionally(ex -> {
            logger.warn("Failed to mark player {} as joined in database", uuidStr, ex);
            return null;
        });
    }

    private boolean isMysqlMode() {
        AppConfig cfg = appConfig.get();
        return cfg != null && "MYSQL".equalsIgnoreCase(cfg.firstJoinMode());
    }
}
