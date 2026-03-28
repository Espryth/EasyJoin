package me.espryth.easyjoin.service;

import com.thewinterframework.service.annotation.Service;
import me.clip.placeholderapi.PlaceholderAPI;
import me.espryth.easyjoin.action.Action;
import me.espryth.easyjoin.util.AvatarUtils;
import me.espryth.easyjoin.util.MessageUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;

@Service
public class ActionService {

    private final Map<String, ActionFactory> actionFactories = new HashMap<>();

    public ActionService() {
        registerDefaultActions();
    }

    public void registerAction(String identifier, ActionFactory factory) {
        actionFactories.put(identifier.toUpperCase(), factory);
    }

    public Optional<Action> parseAction(String line) {
        String[] parts = line.split(" ", 2);
        String identifier = parts[0].toUpperCase();
        String data = parts.length > 1 ? parts[1] : "";

        ActionFactory factory = actionFactories.get(identifier);
        if (factory != null) {
            return Optional.of(factory.create(data));
        }
        return Optional.empty();
    }

    private void registerDefaultActions() {
        registerAction("[MESSAGE]", data -> (player, queue) -> sendMessage(player, data, player::sendMessage));

        registerAction("[BROADCAST]", data -> (player, queue) -> sendMessage(player, data, Bukkit::broadcast));

        registerAction("[JSON_MESSAGE]", data -> (player, queue) -> {
            String json = MessageUtils.formatString(player, data);
            player.sendMessage(GsonComponentSerializer.gson().deserialize(json));
        });

        registerAction("[JSON_BROADCAST]", data -> (player, queue) -> {
            String json = MessageUtils.formatString(player, data);
            Bukkit.broadcast(GsonComponentSerializer.gson().deserialize(json));
        });

        registerAction("[ACTIONBAR]", data -> (player, queue) -> {
            Component msg = MessageUtils.colorizeToComponent(PlaceholderAPI.setPlaceholders(player, data));
            player.sendActionBar(msg);
        });

        registerAction("[BROADCAST_ACTIONBAR]", data -> (player, queue) -> {
            Component msg = MessageUtils.colorizeToComponent(PlaceholderAPI.setPlaceholders(player, data));
            Bukkit.getOnlinePlayers().forEach(p -> p.sendActionBar(msg));
        });

        registerAction("[TITLE]", data -> (player, queue) -> showTitle(player, data, player::showTitle));

        registerAction("[BROADCAST_TITLE]", data -> (player, queue) -> showTitle(player, data,
                title -> Bukkit.getOnlinePlayers().forEach(p -> p.showTitle(title))));

        registerAction("[AVATAR_MESSAGE]",
                data -> (player, queue) -> sendAvatarMessage(player, data, player::sendMessage));

        registerAction("[AVATAR_BROADCAST]",
                data -> (player, queue) -> sendAvatarMessage(player, data, Bukkit::broadcast));

        registerAction("[CLEARCHAT]", data -> (player, queue) -> {
            int lines = Integer.parseInt(data);
            for (int i = 0; i < lines; i++)
                player.sendMessage(Component.text(" "));
        });

        registerAction("[FIREWORK]", data -> (player, queue) -> spawnFirework(player, data));

        registerAction("[CONSOLE]", data -> (player, queue) -> {
            String cmd = MessageUtils.formatString(player, data);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        });

        registerAction("[PLAYER]", data -> (player, queue) -> {
            String cmd = MessageUtils.formatString(player, data);
            player.performCommand(cmd);
        });

        registerAction("[SOUND]", data -> (player, queue) -> playSoundCommand(player, data));

        registerAction("[BROADCAST_SOUND]", data -> (player, queue) -> playBroadcastSoundCommand(data));

        registerAction("[DELAY]", data -> (player, queue) -> {
            int delay = Integer.parseInt(data);
            queue.setPaused(true);
            player.getScheduler().runDelayed(queue.getPlugin(), t -> {
                queue.setPaused(false);
                queue.executeAll(player);
            }, null, delay * 20L);
        });
    }

    private void sendMessage(Player player, String data, Consumer<Component> sender) {
        Component msg = MessageUtils.colorizeToComponent(PlaceholderAPI.setPlaceholders(player, data));
        if (data.startsWith("<c>")) {
            String legacy = MessageUtils.formatString(player, data);
            Component centeredMsg = MessageUtils
                    .colorizeToComponent(MessageUtils.getCenteredMessage(legacy.replace("<c>", "")));
            sender.accept(centeredMsg);
        } else {
            sender.accept(msg);
        }
    }

    private void showTitle(Player player, String data, Consumer<Title> titleSender) {
        String[] parts = data.split(";");
        String titleStr = parts.length > 0 ? parts[0] : "";
        String subtitleStr = parts.length > 1 ? parts[1] : "";
        int fadeIn = parts.length > 2 ? Integer.parseInt(parts[2]) : 10;
        int stay = parts.length > 3 ? Integer.parseInt(parts[3]) : 70;
        int fadeOut = parts.length > 4 ? Integer.parseInt(parts[4]) : 20;

        Component title = MessageUtils.colorizeToComponent(PlaceholderAPI.setPlaceholders(player, titleStr));
        Component subtitle = MessageUtils.colorizeToComponent(PlaceholderAPI.setPlaceholders(player, subtitleStr));

        var titleObj = Title.title(
                title,
                subtitle,
                Title.Times.times(
                        Duration.ofMillis(fadeIn * 50L),
                        Duration.ofMillis(stay * 50L),
                        Duration.ofMillis(fadeOut * 50L)));
        titleSender.accept(titleObj);
    }

    private void sendAvatarMessage(Player player, String data, Consumer<Component> sender) {
        List<Component> avatar = AvatarUtils.getAvatar(player);
        String[] lines = data.split("<nl>");
        final int AVATAR_OFFSET_PX = 76;

        for (int i = 0; i < 8; i++) {
            Component avatarLine = avatar.size() > i ? avatar.get(i) : Component.text("        ");
            String messageLine = lines.length > i ? lines[i] : "";

            final Component formattedMessageLine;

            if (messageLine.startsWith("<c>")) {
                String stripped = messageLine.substring(3);
                String withPlaceholders = PlaceholderAPI.setPlaceholders(player, stripped);
                String legacyForMeasure = MessageUtils.colorize(withPlaceholders);
                Component padding = MessageUtils.getCenteredSpaceComponent(legacyForMeasure, AVATAR_OFFSET_PX);
                Component content = MessageUtils.colorizeToComponent(withPlaceholders);
                formattedMessageLine = padding.append(content);
            } else {
                formattedMessageLine = MessageUtils
                        .colorizeToComponent(PlaceholderAPI.setPlaceholders(player, messageLine));
            }

            sender.accept(avatarLine.append(Component.text(" ")).append(formattedMessageLine));
        }
    }

    private void spawnFirework(Player player, String data) {
        String[] parts = data.split(";");
        if (parts.length < 3)
            return;
        FireworkEffect.Type type = FireworkEffect.Type.valueOf(parts[0].toUpperCase());
        int amount = Integer.parseInt(parts[1]);
        int power = Integer.parseInt(parts[2]);
        List<Color> colors = new ArrayList<>();
        List<Color> fades = new ArrayList<>();

        for (int i = 3; i < parts.length; i++) {
            String[] rgb = parts[i].split(",");
            Color color = Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
            if (i % 2 == 0)
                fades.add(color);
            else
                colors.add(color);
        }

        Firework firework = (Firework) player.getLocation().getWorld().spawnEntity(player.getLocation(),
                EntityType.FIREWORK_ROCKET);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(power);
        meta.addEffect(FireworkEffect.builder().with(type).withColor(colors).withFade(fades).trail(true).build());
        firework.setFireworkMeta(meta);

        for (int i = 1; i < amount; i++) {
            Firework f = (Firework) player.getLocation().getWorld().spawnEntity(player.getLocation(),
                    EntityType.FIREWORK_ROCKET);
            f.setFireworkMeta(meta);
        }
    }

    private Sound parseSound(String data) {
        String[] parts = data.split(";");
        String soundName = parts.length > 0 ? parts[0].trim() : "";
        float volume = parts.length > 1 ? Float.parseFloat(parts[1]) : 1.0f;
        float pitch = parts.length > 2 ? Float.parseFloat(parts[2]) : 1.0f;
        String sourceName = parts.length > 3 ? parts[3].toUpperCase() : "MASTER";

        final Key key;
        String namespace = "minecraft";
        String path = soundName;
        if (soundName.contains(":")) {
            String[] split = soundName.split(":" , 2);
            namespace = split[0].toLowerCase(Locale.ROOT);
            path = split[1];
        }
        path = path.toLowerCase(Locale.ROOT).replace('_', '.');
        key = Key.key(namespace, path);

        Sound.Source source = Sound.Source.MASTER;
        source = Sound.Source.valueOf(sourceName);

        return Sound.sound(key, source, volume, pitch);
    }

    private void playSoundCommand(Player player, String data) {
        Sound sound = parseSound(data);
        player.playSound(sound);

    }

    private void playBroadcastSoundCommand(String data) {
        Sound sound = parseSound(data);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(sound);
        }

    }

    @FunctionalInterface
    public interface ActionFactory {
        Action create(String data);
    }
}

