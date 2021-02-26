package me.espryth.easyjoin.service;

import me.espryth.easyjoin.Core;
import me.espryth.easyjoin.action.AbstractAction;
import me.espryth.easyjoin.action.Action;
import me.espryth.easyjoin.action.ActionManager;
import me.espryth.easyjoin.action.ActionType;
import me.espryth.easyjoin.file.Files;
import me.espryth.easyjoin.file.FilesManager;
import me.espryth.easyjoin.format.Format;
import me.espryth.easyjoin.format.SimpleFormat;

import java.util.Map;

public class FormatService implements Service {

    private final FilesManager filesManager;

    private final ActionManager actionManager;

    private final Map<String, Format> formatMap;

    public FormatService(Core core) {
        this.filesManager = core.getFilesManager();
        this.formatMap = core.getFormatMap();
        this.actionManager = new ActionManager(core);
    }

    @Override
    public void start() {

        Files config = filesManager.getConfig();
        for(String path : config.getConfigurationSection("Formats").getKeys(false)) {

            String identifier = config.getString("Formats." + path);
            int priority = config.getInt("Formats." + path + ".priority");
            boolean isFirstJoinFormat = false;
            if (config.contains("Formats." + path + "isFirstJoinFormat")) {
                isFirstJoinFormat = config.getBoolean("Formats." + path + ".isFirstJoinFormat");
            }

            Format format = new SimpleFormat(identifier, priority, isFirstJoinFormat);

            if (config.contains("Formats." + path + ".join")) {
                if (config.contains("Formats." + path + ".join.delay")) {
                    format.setDelay(config.getInt("Formats." + path + ".join.delay"));
                }
                for (String joinAction : config.getStringList("Formats." + path + ".join.actions")) {
                    for (ActionType actionType : ActionType.values()) {
                        if (joinAction.startsWith(actionType.getIdentifier())) {
                            String line = joinAction.replace(actionType.getIdentifier(), "");
                            Action action = actionManager.getActionByType(actionType, line);
                            format.getJoinActions().add(action);
                        }
                    }
                }
            }

            if (config.contains("Formats." + path + ".quit.actions")) {
                for (String quitAction : config.getStringList("Formats." + path + ".quit.actions")) {
                    for (ActionType actionType : ActionType.values()) {
                        if (quitAction.startsWith(actionType.getIdentifier())) {
                            String line = quitAction.replace(actionType.getIdentifier(), "");
                            Action action = actionManager.getActionByType(actionType, line);
                            format.getQuitActions().add(action);
                        }
                    }
                }
            }

            if (config.contains("Formats." + path + ".authme.actions")) {
                for (String authMeAction : config.getStringList("Formats." + path + ".authme.actions")) {
                    for (ActionType actionType : ActionType.values()) {
                        if (authMeAction.startsWith(actionType.getIdentifier())) {
                            String line = authMeAction.replace(actionType.getIdentifier(), "");
                            Action action = actionManager.getActionByType(actionType, line);
                            format.getQuitActions().add(action);
                        }
                    }
                }
            }

            formatMap.put(identifier, format);
        }
    }
}
