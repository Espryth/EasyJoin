package me.espryth.easyjoin.service;

import com.thewinterframework.service.annotation.Service;
import jakarta.inject.Inject;
import me.espryth.easyjoin.action.Action;
import me.espryth.easyjoin.action.ActionQueue;
import me.espryth.easyjoin.util.AvatarUtils;
import me.espryth.easyjoin.util.MessageUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class ActionService {

    private static final Map<String, Key> BUKKIT_SOUND_KEYS = buildBukkitSoundKeyMap();

    private final Map<String, ActionFactory> actionFactories = new HashMap<>();
    private final Logger logger;

    @Inject
    public ActionService(Logger logger) {
        this.logger = logger;
        registerDefaultActions();
    }

    public void registerAction(String identifier, ActionFactory factory) {
        actionFactories.put(identifier.toUpperCase(Locale.ROOT), factory);
    }

    public Optional<Action> parseAction(String line) {
        if (line == null || line.isBlank()) {
            return Optional.empty();
        }
        String[] parts = line.trim().split(" ", 2);
        String rawIdentifier = parts[0].toUpperCase(Locale.ROOT);
        if (rawIdentifier.startsWith("<") && rawIdentifier.endsWith(">")) {
            rawIdentifier = "[" + rawIdentifier.substring(1, rawIdentifier.length() - 1) + "]";
        }
        String data = parts.length > 1 ? parts[1] : "";

        ActionFactory factory = actionFactories.get(rawIdentifier);
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
            Component msg = MessageUtils.colorizeToComponent(MessageUtils.parsePlaceholders(player, data));
            player.sendActionBar(msg);
        });

        registerAction("[BROADCAST_ACTIONBAR]", data -> (player, queue) -> {
            Component msg = MessageUtils.colorizeToComponent(MessageUtils.parsePlaceholders(player, data));
            Bukkit.getOnlinePlayers().forEach(p -> p.sendActionBar(msg));
        });

        registerAction("[TITLE]", data -> (player, queue) -> showTitle(player, data, player::showTitle));

        registerAction("[BROADCAST_TITLE]", data -> (player, queue) -> showTitle(player, data,
                title -> Bukkit.getOnlinePlayers().forEach(p -> p.showTitle(title))));

        registerAction("[AVATAR_MESSAGE]",
                data -> (player, queue) -> sendAvatarMessage(player, queue, data, player::sendMessage));

        registerAction("[AVATAR_BROADCAST]",
                data -> (player, queue) -> sendAvatarMessage(player, queue, data, Bukkit::broadcast));

        registerAction("[CLEARCHAT]", data -> (player, queue) -> {
            try {
                int lines = Integer.parseInt(data.trim());
                for (int i = 0; i < lines; i++) {
                    player.sendMessage(Component.text(" "));
                }
            } catch (NumberFormatException e) {
                logger.warn("Invalid line count for [CLEARCHAT]: {}", data);
            }
        });

        registerAction("[FIREWORK]", data -> (player, queue) -> spawnFirework(player, data));

        registerAction("[CONSOLE]", data -> (player, queue) -> {
            String cmd = MessageUtils.formatString(player, data);
            if (Bukkit.isPrimaryThread()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            } else {
                Bukkit.getGlobalRegionScheduler().execute(
                        queue.getPlugin(),
                        () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd)
                );
            }
        });

        registerAction("[PLAYER]", data -> (player, queue) -> {
            String cmd = MessageUtils.formatString(player, data);
            player.performCommand(cmd);
        });

        registerAction("[SOUND]", data -> (player, queue) -> playSoundCommand(player, data));

        registerAction("[BROADCAST_SOUND]", data -> (player, queue) -> playBroadcastSoundCommand(data));

        registerAction("[DELAY]", data -> (player, queue) -> {
            try {
                int delay = Integer.parseInt(data.trim());
                if (delay <= 0) {
                    return;
                }
                queue.setPaused(true);
                player.getScheduler().runDelayed(queue.getPlugin(), t -> {
                    queue.setPaused(false);
                    queue.executeAll(player);
                }, null, delay * 20L);
            } catch (NumberFormatException e) {
                logger.warn("Invalid seconds for [DELAY]: {}", data);
            }
        });
    }

    private void sendMessage(Player player, String data, Consumer<Component> sender) {
        if (data.startsWith("<c>")) {
            String stripped = data.substring(3);
            String withPlaceholders = MessageUtils.parsePlaceholders(player, stripped);
            String legacyForMeasure = MessageUtils.colorize(withPlaceholders);
            Component padding = MessageUtils.getCenteredSpaceComponent(legacyForMeasure, 0);
            Component content = MessageUtils.colorizeToComponent(withPlaceholders);
            sender.accept(padding.append(content));
        } else {
            Component msg = MessageUtils.colorizeToComponent(MessageUtils.parsePlaceholders(player, data));
            sender.accept(msg);
        }
    }

    private void showTitle(Player player, String data, Consumer<Title> titleSender) {
        String[] parts = data.split(";");
        String titleStr = parts.length > 0 ? parts[0] : "";
        String subtitleStr = parts.length > 1 ? parts[1] : "";
        int fadeIn = parseIntOrDefault(parts, 2, 10);
        int stay = parseIntOrDefault(parts, 3, 70);
        int fadeOut = parseIntOrDefault(parts, 4, 20);

        Component title = MessageUtils.colorizeToComponent(MessageUtils.parsePlaceholders(player, titleStr));
        Component subtitle = MessageUtils.colorizeToComponent(MessageUtils.parsePlaceholders(player, subtitleStr));

        var titleObj = Title.title(
                title,
                subtitle,
                Title.Times.times(
                        Duration.ofMillis(fadeIn * 50L),
                        Duration.ofMillis(stay * 50L),
                        Duration.ofMillis(fadeOut * 50L)));
        titleSender.accept(titleObj);
    }

    private void sendAvatarMessage(Player player, ActionQueue queue, String data, Consumer<Component> sender) {
        Optional<List<Component>> cached = AvatarUtils.getCachedAvatar(player.getUniqueId());
        if (cached.isPresent()) {
            deliverAvatarLines(player, cached.get(), data, sender);
            return;
        }

        queue.setPaused(true);
        AvatarUtils.getAvatarAsync(player).whenComplete((avatar, error) -> {
            Runnable resume = () -> {
                try {
                    if (avatar != null) {
                        deliverAvatarLines(player, avatar, data, sender);
                    }
                } finally {
                    queue.setPaused(false);
                    queue.executeAll(player);
                }
            };
            boolean scheduled = player.getScheduler().run(queue.getPlugin(), t -> resume.run(), null) != null;
            if (!scheduled) {
                Bukkit.getGlobalRegionScheduler().execute(queue.getPlugin(), resume);
            }
        });
    }

    private void deliverAvatarLines(Player player, List<Component> avatar, String data, Consumer<Component> sender) {
        String[] lines = data.split("<nl>");
        final int AVATAR_OFFSET_PX = 76;

        for (int i = 0; i < 8; i++) {
            Component avatarLine = avatar.size() > i ? avatar.get(i) : Component.text("        ");
            String messageLine = lines.length > i ? lines[i] : "";

            final Component formattedMessageLine;

            if (messageLine.startsWith("<c>")) {
                String stripped = messageLine.substring(3);
                String withPlaceholders = MessageUtils.parsePlaceholders(player, stripped);
                String legacyForMeasure = MessageUtils.colorize(withPlaceholders);
                Component padding = MessageUtils.getCenteredSpaceComponent(legacyForMeasure, AVATAR_OFFSET_PX);
                Component content = MessageUtils.colorizeToComponent(withPlaceholders);
                formattedMessageLine = padding.append(content);
            } else {
                formattedMessageLine = MessageUtils
                        .colorizeToComponent(MessageUtils.parsePlaceholders(player, messageLine));
            }

            sender.accept(avatarLine.append(Component.text(" ")).append(formattedMessageLine));
        }
    }

    private void spawnFirework(Player player, String data) {
        String[] parts = data.split(";");
        if (parts.length < 3) {
            return;
        }
        Location location = player.getLocation();
        World world = location.getWorld();
        if (world == null) {
            return;
        }

        try {
            FireworkEffect.Type type = FireworkEffect.Type.valueOf(parts[0].trim().toUpperCase(Locale.ROOT));
            int amount = Integer.parseInt(parts[1].trim());
            int power = Integer.parseInt(parts[2].trim());
            if (amount <= 0) {
                return;
            }

            List<Color> colors = new ArrayList<>();
            List<Color> fades = new ArrayList<>();

            for (int i = 3; i < parts.length; i++) {
                String[] rgb = parts[i].trim().split(",");
                if (rgb.length < 3) {
                    continue;
                }
                Color color = Color.fromRGB(
                        Integer.parseInt(rgb[0].trim()),
                        Integer.parseInt(rgb[1].trim()),
                        Integer.parseInt(rgb[2].trim())
                );
                if (i % 2 == 0) {
                    fades.add(color);
                } else {
                    colors.add(color);
                }
            }

            if (colors.isEmpty()) {
                colors.add(Color.WHITE);
            }

            FireworkEffect effect = FireworkEffect.builder()
                    .with(type)
                    .withColor(colors)
                    .withFade(fades)
                    .trail(true)
                    .build();

            for (int i = 0; i < amount; i++) {
                Firework firework = (Firework) world.spawnEntity(location, EntityType.FIREWORK_ROCKET);
                FireworkMeta meta = firework.getFireworkMeta();
                meta.setPower(power);
                meta.addEffect(effect);
                firework.setFireworkMeta(meta);
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid [FIREWORK] configuration '{}': {}", data, e.getMessage());
        }
    }

    private Sound parseSound(String data) {
        String[] parts = data.split(";");
        String soundName = parts.length > 0 ? parts[0].trim() : "";
        if (soundName.isEmpty()) {
            return null;
        }
        float volume = parseFloatOrDefault(parts, 1, 1.0f);
        float pitch = parseFloatOrDefault(parts, 2, 1.0f);
        String sourceName = parts.length > 3 ? parts[3].trim().toUpperCase(Locale.ROOT) : "MASTER";

        Key key = resolveSoundKey(soundName);
        if (key == null) {
            return null;
        }

        Sound.Source source = Sound.Source.MASTER;
        try {
            source = Sound.Source.valueOf(sourceName);
        } catch (IllegalArgumentException ignored) {
        }

        return Sound.sound(key, source, volume, pitch);
    }

    private Key resolveSoundKey(String soundName) {
        Key mapped = BUKKIT_SOUND_KEYS.get(soundName.toUpperCase(Locale.ROOT));
        if (mapped != null) {
            return mapped;
        }

        try {
            String namespace = "minecraft";
            String path = soundName;
            if (soundName.contains(":")) {
                String[] split = soundName.split(":", 2);
                namespace = split[0].toLowerCase(Locale.ROOT);
                path = split[1];
            }

            String lowerPath = path.toLowerCase(Locale.ROOT);
            NamespacedKey directKey = NamespacedKey.fromString(namespace + ":" + lowerPath);
            if (directKey != null && Registry.SOUNDS.get(directKey) != null) {
                return directKey;
            }

            String dotPath = lowerPath.replace('_', '.');
            return Key.key(namespace, dotPath);
        } catch (Exception e) {
            logger.warn("Invalid sound key '{}': {}", soundName, e.getMessage());
            return null;
        }
    }

    private static Map<String, Key> buildBukkitSoundKeyMap() {
        Map<String, Key> map = new HashMap<>();
        try {
            for (Field field : org.bukkit.Sound.class.getFields()) {
                if (Modifier.isStatic(field.getModifiers()) && Keyed.class.isAssignableFrom(field.getType())) {
                    Keyed keyed = (Keyed) field.get(null);
                    if (keyed != null) {
                        map.put(field.getName().toUpperCase(Locale.ROOT), keyed.key());
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return Map.copyOf(map);
    }

    private static int parseIntOrDefault(String[] parts, int index, int defaultValue) {
        if (parts.length <= index) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(parts[index].trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static float parseFloatOrDefault(String[] parts, int index, float defaultValue) {
        if (parts.length <= index) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(parts[index].trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void playSoundCommand(Player player, String data) {
        Sound sound = parseSound(data);
        if (sound != null) {
            player.playSound(sound);
        }
    }

    private void playBroadcastSoundCommand(String data) {
        Sound sound = parseSound(data);
        if (sound != null) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(sound);
            }
        }
    }

    @FunctionalInterface
    public interface ActionFactory {
        Action create(String data);
    }
}
