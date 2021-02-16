package me.hsgamer.simpleboard;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public final class Utils {
    private Utils() {
        // EMPTY
    }

    public static String format(UUID uuid, String string) {
        return colorize(replacePlaceholders(uuid, string));
    }

    public static String replacePlaceholders(UUID uuid, String string) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        while (PlaceholderAPI.containsBracketPlaceholders(string)) {
            string = PlaceholderAPI.setPlaceholders(player, string);
        }
        return string;
    }

    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
