package me.espryth.easyjoin.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.skinsrestorer.api.PropertyUtils;
import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import net.skinsrestorer.api.property.SkinProperty;
import net.skinsrestorer.api.storage.PlayerStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class AvatarUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvatarUtils.class);
    private static final int CONNECT_TIMEOUT_MS = 3000;
    private static final int READ_TIMEOUT_MS = 3000;
    private static final long CACHE_TTL_MS = 10 * 60 * 1000L; // 10 minutes
    private static final long PLACEHOLDER_TTL_MS = 30 * 1000L; // 30 seconds
    private static final Map<UUID, CachedAvatar> AVATAR_CACHE = new ConcurrentHashMap<>();
    private static final Map<UUID, CompletableFuture<List<Component>>> IN_FLIGHT = new ConcurrentHashMap<>();

    /**
     * Returns the cached 8-line avatar if present and not expired.
     */
    public static Optional<List<Component>> getCachedAvatar(UUID uuid) {
        CachedAvatar cached = AVATAR_CACHE.get(uuid);
        if (cached != null && !cached.isExpired()) {
            return Optional.of(cached.lines());
        }
        return Optional.empty();
    }

    /**
     * Asynchronously resolves the 8-line avatar for the given player, using cache when available.
     */
    public static CompletableFuture<List<Component>> getAvatarAsync(Player player) {
        UUID uuid = player.getUniqueId();
        Optional<List<Component>> cached = getCachedAvatar(uuid);
        if (cached.isPresent()) {
            return CompletableFuture.completedFuture(cached.get());
        }

        String uuidStr = uuid.toString().replace("-", "");
        String playerName = player.getName();
        URL profileSkinUrl = extractProfileSkinUrl(player);
        boolean skinsRestorerEnabled = Bukkit.getPluginManager().isPluginEnabled("SkinsRestorer");

        return IN_FLIGHT.computeIfAbsent(uuid, key ->
                CompletableFuture.supplyAsync(() -> {
                    try {
                        return resolveAndCacheAvatar(uuid, uuidStr, playerName, profileSkinUrl, skinsRestorerEnabled);
                    } finally {
                        IN_FLIGHT.remove(uuid);
                    }
                })
        );
    }

    /**
     * Returns the 8-line avatar for the given player, using cache when available.
     */
    public static List<Component> getAvatar(Player player) {
        UUID uuid = player.getUniqueId();
        Optional<List<Component>> cached = getCachedAvatar(uuid);
        if (cached.isPresent()) {
            return cached.get();
        }

        String uuidStr = uuid.toString().replace("-", "");
        String playerName = player.getName();
        URL profileSkinUrl = extractProfileSkinUrl(player);
        boolean skinsRestorerEnabled = Bukkit.getPluginManager().isPluginEnabled("SkinsRestorer");

        return resolveAndCacheAvatar(uuid, uuidStr, playerName, profileSkinUrl, skinsRestorerEnabled);
    }

    /**
     * Clears the cached avatar for a specific player (e.g. on skin change).
     */
    public static void invalidateCache(UUID uuid) {
        AVATAR_CACHE.remove(uuid);
    }

    /**
     * Clears all cached avatars.
     */
    public static void clearCache() {
        AVATAR_CACHE.clear();
    }

    private static URL extractProfileSkinUrl(Player player) {
        try {
            return player.getPlayerProfile().getTextures().getSkin();
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static List<Component> resolveAndCacheAvatar(
            UUID uuid,
            String uuidStr,
            String playerName,
            URL profileSkinUrl,
            boolean skinsRestorerEnabled
    ) {
        BufferedImage image = null;
        if (skinsRestorerEnabled) {
            image = fetchFromSkinsRestorer(uuid, playerName);
            if (image != null) {
                List<Component> result = cropAndConvert(image);
                AVATAR_CACHE.put(uuid, new CachedAvatar(result, System.currentTimeMillis(), CACHE_TTL_MS));
                return result;
            }
        }

        if (profileSkinUrl != null) {
            image = downloadImage(profileSkinUrl);
            if (image != null) {
                List<Component> result = cropAndConvert(image);
                AVATAR_CACHE.put(uuid, new CachedAvatar(result, System.currentTimeMillis(), CACHE_TTL_MS));
                return result;
            }
        }

        image = fetchMcHeads(playerName, 8);
        if (image != null) {
            List<Component> result = imageToComponents(ensureSize(image, 8, 8));
            AVATAR_CACHE.put(uuid, new CachedAvatar(result, System.currentTimeMillis(), CACHE_TTL_MS));
            return result;
        }

        image = fetchSkinFromMojang(uuidStr);
        if (image != null) {
            List<Component> result = cropAndConvert(image);
            AVATAR_CACHE.put(uuid, new CachedAvatar(result, System.currentTimeMillis(), CACHE_TTL_MS));
            return result;
        }

        image = fetchCrafatar(uuidStr, 8);
        if (image != null) {
            List<Component> result = imageToComponents(ensureSize(image, 8, 8));
            AVATAR_CACHE.put(uuid, new CachedAvatar(result, System.currentTimeMillis(), CACHE_TTL_MS));
            return result;
        }

        List<Component> placeholder = generatePlaceholder(playerName, 8, 8);
        AVATAR_CACHE.put(uuid, new CachedAvatar(placeholder, System.currentTimeMillis(), PLACEHOLDER_TTL_MS));
        return placeholder;
    }

    private static List<Component> cropAndConvert(BufferedImage skin) {
        BufferedImage head = cropHeadWithOverlay(skin);
        BufferedImage small = resize(head, 8, 8);
        return imageToComponents(small);
    }

    private static BufferedImage fetchFromSkinsRestorer(UUID uuid, String playerName) {
        try {
            return fetchFromSkinsRestorerInternal(uuid, playerName);
        } catch (Throwable t) {
            LOGGER.debug("SkinsRestorer integration failed", t);
            return null;
        }
    }

    /**
     * Internal method that actually uses SkinsRestorer API classes.
     * Separated so the outer method can catch NoClassDefFoundError safely.
     */
    private static BufferedImage fetchFromSkinsRestorerInternal(UUID uuid, String playerName) {
        try {
            SkinsRestorer api = SkinsRestorerProvider.get();
            PlayerStorage playerStorage = api.getPlayerStorage();

            Optional<SkinProperty> propertyOpt = playerStorage.getSkinForPlayer(uuid, playerName);
            if (propertyOpt.isEmpty()) {
                return null;
            }

            String skinUrl = PropertyUtils.getSkinTextureUrl(propertyOpt.get());
            if (skinUrl == null || skinUrl.isBlank()) {
                return null;
            }

            return downloadImage(URI.create(skinUrl).toURL());
        } catch (Exception e) {
            LOGGER.debug("Failed to fetch skin from SkinsRestorer for {}", playerName, e);
            return null;
        }
    }

    /**
     * Fetches the player's head avatar from Crafatar with the overlay layer applied.
     */
    private static BufferedImage fetchCrafatar(String uuid, int size) {
        try {
            URL url = URI.create(
                    "https://crafatar.com/avatars/" + uuid + "?size=" + size + "&overlay"
            ).toURL();
            return downloadImage(url);
        } catch (Exception e) {
            LOGGER.debug("Crafatar fetch failed for {}", uuid, e);
            return null;
        }
    }

    /**
     * Fetches the player's head avatar from mc-heads.net.
     */
    private static BufferedImage fetchMcHeads(String playerName, int size) {
        try {
            URL url = URI.create(
                    "https://mc-heads.net/avatar/" + playerName + "/" + size
            ).toURL();
            return downloadImage(url);
        } catch (Exception e) {
            LOGGER.debug("mc-heads fetch failed for {}", playerName, e);
            return null;
        }
    }

    /**
     * Fetches the full skin texture from the Mojang session server.
     */
    private static BufferedImage fetchSkinFromMojang(String uuid) {
        try {
            URL profileUrl = URI.create(
                    "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid
            ).toURL();
            String profileJson = downloadString(profileUrl);
            if (profileJson == null) {
                return null;
            }

            String textureBase64 = extractBase64Texture(profileJson);
            if (textureBase64 == null) {
                return null;
            }

            String textureJson = new String(
                    Base64.getDecoder().decode(textureBase64), StandardCharsets.UTF_8
            );
            String skinUrl = extractSkinUrlFromTextureJson(textureJson);
            if (skinUrl == null) {
                return null;
            }

            return downloadImage(URI.create(skinUrl).toURL());
        } catch (Exception e) {
            LOGGER.debug("Mojang skin fetch failed for {}", uuid, e);
            return null;
        }
    }

    private static BufferedImage downloadImage(URL url) {
        HttpURLConnection conn = null;
        try {
            conn = openConnection(url);
            int code = conn.getResponseCode();
            if (code != 200) {
                return null;
            }
            try (InputStream in = conn.getInputStream()) {
                return ImageIO.read(in);
            }
        } catch (IOException e) {
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static String downloadString(URL url) {
        HttpURLConnection conn = null;
        try {
            conn = openConnection(url);
            int code = conn.getResponseCode();
            if (code != 200) {
                return null;
            }
            try (InputStream in = conn.getInputStream()) {
                return new String(in.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
        conn.setReadTimeout(READ_TIMEOUT_MS);
        conn.setRequestProperty("User-Agent", "EasyJoin-Plugin");
        conn.setInstanceFollowRedirects(true);
        return conn;
    }

    /**
     * Crops the head (8×8 at offset 8,8) from a full skin and composites the
     * overlay / hat layer (8×8 at offset 40,8) on top, respecting alpha.
     */
    private static BufferedImage cropHeadWithOverlay(BufferedImage skin) {
        int width = skin.getWidth();
        int scale = Math.max(1, width / 64);
        int headSize = 8 * scale;

        int baseX = 8 * scale;
        int baseY = 8 * scale;

        int overlayX = 40 * scale;
        int overlayY = 8 * scale;

        try {
            BufferedImage head = new BufferedImage(headSize, headSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = head.createGraphics();
            g.drawImage(skin.getSubimage(baseX, baseY, headSize, headSize), 0, 0, null);

            if (skin.getWidth() >= overlayX + headSize && skin.getHeight() >= overlayY + headSize) {
                BufferedImage overlay = skin.getSubimage(overlayX, overlayY, headSize, headSize);
                if (!isFullyTransparent(overlay)) {
                    g.drawImage(overlay, 0, 0, null);
                }
            }

            g.dispose();
            return head;
        } catch (Exception e) {
            try {
                return skin.getSubimage(baseX, baseY, headSize, headSize);
            } catch (Exception ex) {
                return skin;
            }
        }
    }

    private static boolean isFullyTransparent(BufferedImage img) {
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int alpha = (img.getRGB(x, y) >> 24) & 0xFF;
                if (alpha > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private static BufferedImage resize(BufferedImage img, int newW, int newH) {
        BufferedImage resized = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resized.createGraphics();
        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
        );
        g2.drawImage(img, 0, 0, newW, newH, null);
        g2.dispose();
        return resized;
    }

    private static BufferedImage ensureSize(BufferedImage img, int w, int h) {
        if (img.getWidth() == w && img.getHeight() == h) {
            return img;
        }
        return resize(img, w, h);
    }

    private static List<Component> imageToComponents(BufferedImage image) {
        List<Component> lines = new ArrayList<>();
        for (int y = 0; y < image.getHeight(); y++) {
            Component line = Component.empty();
            for (int x = 0; x < image.getWidth(); x++) {
                int rgba = image.getRGB(x, y);
                int alpha = (rgba >> 24) & 0xFF;
                if (alpha < 64) {
                    line = line.append(Component.text(" "));
                    continue;
                }
                int rgb = rgba & 0xFFFFFF;
                TextColor color = TextColor.color(rgb);
                line = line.append(Component.text("█").color(color));
            }
            lines.add(line);
        }
        return lines;
    }

    private static List<Component> generatePlaceholder(String seed, int width, int height) {
        List<Component> lines = new ArrayList<>();
        int hash = seed.hashCode();
        int baseR = (hash >> 16) & 0xFF;
        int baseG = (hash >> 8) & 0xFF;
        int baseB = hash & 0xFF;

        for (int y = 0; y < height; y++) {
            Component line = Component.empty();
            for (int x = 0; x < width; x++) {
                int r = (baseR + x * 10 + y * 5) & 0xFF;
                int g = (baseG + x * 7 + y * 3) & 0xFF;
                int b = (baseB + x * 4 + y * 9) & 0xFF;
                int rgb = (r << 16) | (g << 8) | b;
                TextColor color = TextColor.color(rgb);
                line = line.append(Component.text("█").color(color));
            }
            lines.add(line);
        }
        return lines;
    }

    private static String extractBase64Texture(String profileJson) {
        int props = profileJson.indexOf("\"properties\"");
        if (props < 0) {
            return null;
        }
        int valueIndex = profileJson.indexOf("\"value\"", props);
        if (valueIndex < 0) {
            return null;
        }
        int colon = profileJson.indexOf(':', valueIndex);
        if (colon < 0) {
            return null;
        }
        int firstQuote = profileJson.indexOf('"', colon);
        if (firstQuote < 0) {
            return null;
        }
        int secondQuote = profileJson.indexOf('"', firstQuote + 1);
        if (secondQuote < 0) {
            return null;
        }
        String base64 = profileJson.substring(firstQuote + 1, secondQuote);
        if (base64.isBlank()) {
            return null;
        }
        return base64;
    }

    private static String extractSkinUrlFromTextureJson(String textureJson) {
        int skinIdx = textureJson.indexOf("\"SKIN\"");
        if (skinIdx < 0) {
            return null;
        }
        int urlIdx = textureJson.indexOf("\"url\"", skinIdx);
        if (urlIdx < 0) {
            return null;
        }
        int colon = textureJson.indexOf(':', urlIdx);
        if (colon < 0) {
            return null;
        }
        int firstQuote = textureJson.indexOf('"', colon);
        if (firstQuote < 0) {
            return null;
        }
        int secondQuote = textureJson.indexOf('"', firstQuote + 1);
        if (secondQuote < 0) {
            return null;
        }
        return textureJson.substring(firstQuote + 1, secondQuote);
    }

    private record CachedAvatar(List<Component> lines, long timestamp, long ttlMs) {
        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > ttlMs;
        }
    }
}
