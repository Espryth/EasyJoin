package me.espryth.easyjoin.command;

import jakarta.inject.Inject;
import com.thewinterframework.command.CommandComponent;
import com.thewinterframework.service.ReloadServiceManager;
import me.espryth.easyjoin.action.ActionQueue;
import me.espryth.easyjoin.format.Format;
import me.espryth.easyjoin.service.FormatService;
import me.espryth.easyjoin.util.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;
import org.incendo.cloud.annotations.suggestion.Suggestions;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.paper.util.sender.Source;
import org.slf4j.Logger;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;

@CommandComponent
public class MainCommand {

    private final ReloadServiceManager reloadServiceManager;
    private final FormatService formatService;
    private final Logger logger;
    private final Plugin plugin;

    @Inject
    public MainCommand(ReloadServiceManager reloadServiceManager,
            FormatService formatService, Logger logger, Plugin plugin) {
        this.reloadServiceManager = reloadServiceManager;
        this.formatService = formatService;
        this.logger = logger;
        this.plugin = plugin;
    }

    @Command("easyjoin")
    public void mainHelp(Source source) {
        CommandSender sender = (CommandSender) source.source();
        sender.sendMessage(MessageUtils.colorize("&e&lEasy&6&lJoin &fV3"));
        if (sender.hasPermission("easyjoin.admin")) {
            sender.sendMessage(MessageUtils.colorize("&f- /ej reload"));
            sender.sendMessage(MessageUtils
                    .colorize("&f- /ej join <formatId> - simulate join for the given format (player only)"));
            sender.sendMessage(MessageUtils
                    .colorize("&f- /ej leave <formatId> - simulate quit for the given format (player only)"));
        }
    }

    @Command("easyjoin reload")
    @Permission("easyjoin.admin")
    public void reload(Source source) {
        CommandSender sender = (CommandSender) source.source();
        reloadServiceManager.reload();
        formatService.loadFormats(logger);
        sender.sendMessage(MessageUtils.colorize("&aConfiguration reloaded!"));
    }

    @Suggestions("formats")
    public List<String> suggestFormats(CommandContext<Source> context, CommandInput input) {
        return formatService.getFormatIds();
    }

    @Command("easyjoin testjoin <formatId>")
    @Permission("easyjoin.admin")
    public void testJoin(Source source, @Argument(value = "formatId", suggestions = "formats") String formatId) {
        CommandSender sender = source.source();
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.colorize("&cThis command can only be used by a player."));
            return;
        }

        Optional<Format> formatOpt = formatService.getFormatById(formatId);
        if (formatOpt.isEmpty()) {
            sender.sendMessage(MessageUtils.colorize("&cNo format found with id: " + formatId));
            return;
        }

        new ActionQueue(formatOpt.get().joinActions(), plugin).executeAll(player);
        sender.sendMessage(MessageUtils.colorize("&aExecuted join actions for format: " + formatId));
    }

    @Command("easyjoin testleave <formatId>")
    @Permission("easyjoin.admin")
    public void testLeave(Source source, @Argument(value = "formatId", suggestions = "formats") String formatId) {
        CommandSender sender = source.source();
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.colorize("&cThis command can only be used by a player."));
            return;
        }

        Optional<Format> formatOpt = formatService.getFormatById(formatId);
        if (formatOpt.isEmpty()) {
            sender.sendMessage(MessageUtils.colorize("&cNo format found with id: " + formatId));
            return;
        }

        new ActionQueue(formatOpt.get().quitActions(), plugin).executeAll(player);
        sender.sendMessage(MessageUtils.colorize("&aExecuted quit actions for format: " + formatId));
    }
}
