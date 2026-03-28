package me.espryth.easyjoin.service;

import com.thewinterframework.configurate.Container;
import com.thewinterframework.service.annotation.Service;
import jakarta.inject.Inject;
import me.espryth.easyjoin.config.AppConfig;
import me.espryth.easyjoin.database.DatabaseService;
import me.espryth.easyjoin.database.FirstJoinDao;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class FirstJoinService {

    private final Container<AppConfig> appConfig;
    private final DatabaseService databaseService;
    private final Set<UUID> cache = new HashSet<>();

    @Inject
    public FirstJoinService(Container<AppConfig> appConfig, DatabaseService databaseService) {
        this.appConfig = appConfig;
        this.databaseService = databaseService;
    }

    public CompletableFuture<Boolean> isFirstJoin(Player player) {
        if (!appConfig.get().firstJoinMode().equalsIgnoreCase("MYSQL")) {
            return CompletableFuture.completedFuture(!player.hasPlayedBefore());
        }

        if (cache.contains(player.getUniqueId())) {
            return CompletableFuture.completedFuture(false);
        }

        return CompletableFuture.supplyAsync(() -> {
            if (!databaseService.isEnabled()) {
                return !player.hasPlayedBefore();
            }

            return databaseService.getJdbi()
                .map(jdbi -> jdbi.withExtension(FirstJoinDao.class, dao -> {
                    boolean exists = dao.exists(player.getUniqueId().toString());
                    if (exists) {
                        cache.add(player.getUniqueId());
                    }
                    return !exists;
                }))
                .orElse(!player.hasPlayedBefore());
        });
    }

    public void markAsJoined(Player player) {
        if (!appConfig.get().firstJoinMode().equalsIgnoreCase("MYSQL")) {
            return;
        }

        cache.add(player.getUniqueId());

        CompletableFuture.runAsync(() -> {
            if (databaseService.isEnabled()) {
                databaseService.getJdbi().ifPresent(jdbi ->
                    jdbi.useExtension(FirstJoinDao.class, dao ->
                        dao.insert(player.getUniqueId().toString())
                    )
                );
            }
        });
    }
}
