package me.espryth.easyjoin.plugin.loader;

import me.espryth.easyjoin.plugin.EasyJoin;
import me.espryth.easyjoin.plugin.action.Action;
import me.espryth.easyjoin.plugin.action.ActionType;
import me.espryth.easyjoin.plugin.format.Format;
import me.espryth.easyjoin.plugin.utils.YamlFile;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

import static me.espryth.easyjoin.plugin.EasyJoin.CONTAINER;

public class FormatLoader implements Loader {

    private final EasyJoin plugin;
    private final YamlFile configFile;
    private final Map<String, Format> formatMap;

    public FormatLoader() {
        plugin = CONTAINER.get(EasyJoin.class);
        configFile = CONTAINER.get(YamlFile.class, "config");
        formatMap = plugin.getFormatMap();
    }

    @Override
    public void load() {

        for(String key : configFile.getConfigurationSection("Formats").getKeys(false)) {

            int priority = configFile.getInt("Formats." + key + ".priority");
            boolean isFirstJoinFormat = false;
            if (configFile.contains("Formats." + key + ".isFirstJoinFormat")) {
                isFirstJoinFormat = configFile.getBoolean("Formats." + key + ".isFirstJoinFormat");
            }

            Format format = new Format(key, priority, isFirstJoinFormat);

            loadActions(format, key, ActionType.AUTH);
            loadActions(format, key, ActionType.JOIN);
            loadActions(format, key, ActionType.QUIT);

            formatMap.put(key, format);
        }
    }

    private void loadActions(Format format, String key, ActionType type) {

        String path = "Formats." + key + "." + type.name().toLowerCase() + ".actions";

        if (!configFile.contains(path)) {
            return;
        }

        for(String s : configFile.getStringList(path)) {

            String[] strings = s.split("\\s+");
            Optional<Action> optionalAction = CONTAINER.getOptional(Action.class, strings[0]);

            if(!optionalAction.isPresent()) {
                plugin.getLogger().log(Level.WARNING, "A action with identifier " + strings[0] + " don't exists!");
                continue;
            }

            Action action = optionalAction.get().duplicate();

            String line = s.replace(strings[0] + " ", "");
            action.setLine(line);

            switch (type) {
                case AUTH:
                    format.getAuthActions().add(action);
                    break;
                case JOIN:
                    format.getJoinActions().add(action);
                    break;
                case QUIT:
                    format.getQuitActions().add(action);
                    break;
            }
        }
    }
}
