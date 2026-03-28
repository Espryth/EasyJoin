package me.espryth.easyjoin.service;

import com.thewinterframework.configurate.Container;
import com.thewinterframework.service.annotation.Service;
import com.thewinterframework.service.annotation.lifecycle.OnEnable;
import jakarta.inject.Inject;
import me.espryth.easyjoin.action.Action;
import me.espryth.easyjoin.config.AppConfig;
import me.espryth.easyjoin.format.Format;
import org.bukkit.entity.Player;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FormatService {

    private final Container<AppConfig> config;
    private final ActionService actionService;
    private final Map<String, Format> formats = new HashMap<>();

    @Inject
    public FormatService(Container<AppConfig> config, ActionService actionService) {
        this.config = config;
        this.actionService = actionService;
    }

    @OnEnable
    public void loadFormats(Logger logger) {
        formats.clear();
        AppConfig appConfig = config.get();
        if (appConfig.formats() == null) {
            logger.error("No formats found in configuration!");
            return;
        }

        appConfig.formats().forEach((id, section) -> {
            List<Action> join = parseActions(section.join());
            List<Action> quit = parseActions(section.quit());

            formats.put(id, new Format(
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

    private List<Action> parseActions(AppConfig.ActionSection section) {
        if (section == null || section.actions() == null) return Collections.emptyList();
        return section.actions().stream()
                .map(actionService::parseAction)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
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
        return Optional.ofNullable(formats.get(id));
    }

    public List<String> getFormatIds() {
        return new ArrayList<>(formats.keySet());
    }
}
