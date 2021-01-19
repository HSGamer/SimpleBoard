package me.hsgamer.simpleboard;

import me.clip.placeholderapi.PlaceholderAPI;
import me.tigerhix.lib.scoreboard.ScoreboardLib;
import me.tigerhix.lib.scoreboard.common.EntryBuilder;
import me.tigerhix.lib.scoreboard.type.Entry;
import me.tigerhix.lib.scoreboard.type.Scoreboard;
import me.tigerhix.lib.scoreboard.type.ScoreboardHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class SimpleBoard extends JavaPlugin implements Listener {

    private final Map<UUID, Scoreboard> boardMap = new HashMap<>();

    private final List<String> line = new ArrayList<>();
    private String title;
    private final ScoreboardHandler scoreboardHandler = new ScoreboardHandler() {
        @Override
        public String getTitle(Player player) {
            return format(player.getUniqueId(), title);
        }

        @Override
        public List<Entry> getEntries(Player player) {
            EntryBuilder builder = new EntryBuilder();
            for (String s : line) {
                builder.next(format(player.getUniqueId(), s));
            }
            return builder.build();
        }

        private String format(UUID uuid, String string) {
            return colorize(PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(uuid), string));
        }

        private String colorize(String string) {
            return ChatColor.translateAlternateColorCodes('&', string);
        }
    };
    private int updateTime;

    @Override
    public void onEnable() {
        ScoreboardLib.setPluginInstance(this);
        getServer().getPluginManager().registerEvents(this, this);
        load();
    }

    @Override
    public void onDisable() {
        boardMap.values().forEach(Scoreboard::deactivate);
        boardMap.clear();
        line.clear();
    }

    private void load() {
        FileConfiguration config = getConfig();

        config.options().copyDefaults(true);
        config.addDefault("title", "&c&lTitle");
        config.addDefault("lines", Arrays.asList(
                "&cPlayer: %player_name%",
                "",
                "Hello"
        ));
        config.addDefault("update", 0);

        title = config.getString("title");
        updateTime = config.getInt("update");
        line.addAll(config.getStringList("lines"));

        saveConfig();
    }

    private void addBoard(Player player) {
        boardMap.computeIfAbsent(player.getUniqueId(), uuid ->
                ScoreboardLib
                        .createScoreboard(player)
                        .setHandler(scoreboardHandler)
                        .setUpdateInterval(updateTime));
    }

    private void removeBoard(UUID uuid) {
        if (boardMap.containsKey(uuid)) {
            boardMap.remove(uuid).deactivate();
        }
    }

    private void start(UUID uuid) {
        if (boardMap.containsKey(uuid)) {
            boardMap.get(uuid).activate();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        addBoard(player);

        start(player.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        removeBoard(e.getPlayer().getUniqueId());
    }
}
