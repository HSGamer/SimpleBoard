package me.hsgamer.simpleboard;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class BoardHandler {
    private final List<String> lines;
    private final String title;
    private final long updateTime;

    private BoardHandler(String title, List<String> lines, long updateTime) {
        this.lines = lines;
        this.title = title;
        this.updateTime = updateTime;
    }

    public static BoardHandler load(Plugin plugin) {
        FileConfiguration config = plugin.getConfig();

        config.options().copyDefaults(true);
        config.addDefault("title", "&c&lTitle");
        config.addDefault("lines", Arrays.asList(
                "&cPlayer: {player_name}",
                "",
                "Hello"
        ));
        config.addDefault("update", 0L);
        plugin.saveConfig();

        return new BoardHandler(config.getString("title"), config.getStringList("lines"), config.getLong("update"));
    }

    public List<String> getLines() {
        return lines;
    }

    public String getTitle() {
        return title;
    }

    public long getUpdateTime() {
        return updateTime;
    }
}
