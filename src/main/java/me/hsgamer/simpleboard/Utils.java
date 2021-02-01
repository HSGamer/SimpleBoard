package me.hsgamer.simpleboard;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.UUID;

public final class Utils {
    private Utils() {
        // EMPTY
    }

    public static String format(UUID uuid, String string) {
        return colorize(PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(uuid), string));
    }

    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
