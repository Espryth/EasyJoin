package me.espryth.easyjoin.service;

import com.thewinterframework.configurate.Container;
import com.thewinterframework.service.annotation.Service;
import com.thewinterframework.service.annotation.lifecycle.OnEnable;
import com.thewinterframework.service.annotation.lifecycle.OnReload;
import jakarta.inject.Inject;
import me.espryth.easyjoin.action.Action;
import me.espryth.easyjoin.config.AppConfig;
import me.espryth.easyjoin.format.Format;
import org.bukkit.entity.Player;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FormatService {

    private final Container<AppConfig> config;
    private final ActionService actionService;
    private final Map<String, Format> formats = new ConcurrentHashMap<>();

    @Inject
    public FormatService(Container<AppConfig> config, ActionService actionService) {
        this.config = config;
        this.actionService = actionService;
    }

    @OnEnable
    @OnReload
    public void loadFormats(Logger logger) {
        formats.clear();
        AppConfig appConfig = config.get();
        if (appConfig == null || appConfig.formats() == null) {
            logger.error("No formats found in configuration!");
            return;
        }

        appConfig.formats().forEach((id, section) -> {
            List<Action> join = parseActions(id, section.join(), logger);
            List<Action> quit = parseActions(id, section.quit(), logger);

            formats.put(id.toLowerCase(Locale.ROOT), new Format(
                    id,
                    section.priority(),
                    section.isFirstJoinFormat(),
                    join,
                    quit
            ));

            logger.info("Loaded format: {} (priority: {}, firstJoin: {})", id, section.priority(), section.isFirstJoinFormat());
        });

        logger.info("Total formats loaded: {}", formats.size());
    }

    private List<Action> parseActions(String formatId, AppConfig.ActionSection section, Logger logger) {
        if (section == null || section.actions() == null) {
            return Collections.emptyList();
        }
        List<Action> parsed = new ArrayList<>();
        for (String line : section.actions()) {
            Optional<Action> actionOpt = actionService.parseAction(line);
            if (actionOpt.isPresent()) {
                parsed.add(actionOpt.get());
            } else if (line != null && !line.isBlank()) {
                logger.warn("Unknown action '{}' in format '{}'", line, formatId);
            }
        }
        return List.copyOf(parsed);
    }

    public Optional<Format> getFormatForPlayer(Player player, boolean isFirstJoin) {
        return formats.values().stream()
                .filter(f -> isFirstJoin == f.isFirstJoinFormat())
                .filter(f -> f.id().equalsIgnoreCase("default") || player.hasPermission("easyjoin." + f.id()))
                .max(Comparator.comparingInt(Format::priority));
    }

    public Optional<Format> getDefaultFormat(boolean isFirstJoin) {
        return formats.values().stream()
                .filter(f -> isFirstJoin == f.isFirstJoinFormat())
                .filter(f -> f.id().equalsIgnoreCase("default"))
                .findFirst();
    }

    public Optional<Format> getFormatById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(formats.get(id.toLowerCase(Locale.ROOT)));
    }

    public List<String> getFormatIds() {
        return formats.values().stream()
                .map(Format::id)
                .toList();
    }
}
