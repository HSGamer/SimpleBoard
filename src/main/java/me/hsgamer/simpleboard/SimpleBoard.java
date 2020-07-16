package me.hsgamer.simpleboard;

import fr.mrmicky.fastboard.FastBoard;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public final class SimpleBoard extends JavaPlugin implements Listener {

  private final Map<UUID, FastBoard> boardMap = new HashMap<>();

  private final List<String> line = new ArrayList<>();
  private final List<String> enabledWorld = new ArrayList<>();
  private String title;
  private BukkitTask task;

  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(this, this);
    load();
  }

  @Override
  public void onDisable() {
    if (task != null) {
      task.cancel();
    }
    boardMap.values().forEach(FastBoard::delete);
    boardMap.clear();
    line.clear();
    enabledWorld.clear();
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
    config.addDefault("enabled-worlds", Collections.singletonList("world"));
    config.addDefault("update", 0);

    title = config.getString("title");
    int time = config.getInt("update");
    task = new BukkitRunnable() {
      @Override
      public void run() {
        boardMap.forEach((uuid, fastBoard) -> {
          fastBoard.updateTitle(format(uuid, title));
          fastBoard.updateLines(format(uuid, line));
        });
      }
    }.runTaskTimerAsynchronously(this, time, time);
    line.addAll(config.getStringList("lines"));
    enabledWorld.addAll(config.getStringList("enabled-worlds"));

    saveConfig();
  }

  private List<String> format(UUID uuid, List<String> list) {
    List<String> newList = new ArrayList<>(list);
    newList.replaceAll(s -> format(uuid, s));
    return newList;
  }

  private String format(UUID uuid, String string) {
    return colorize(PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(uuid), string));
  }

  private String colorize(String string) {
    return ChatColor.translateAlternateColorCodes('&', string);
  }

  private void addBoard(Player player) {
    UUID uuid = player.getUniqueId();

    FastBoard board = new FastBoard(player);

    board.updateTitle(format(uuid, title));
    board.updateLines(format(uuid, line));

    boardMap.put(player.getUniqueId(), board);
  }

  private void removeBoard(UUID uuid) {
    boardMap.computeIfPresent(uuid, (uuid1, fastBoard) -> {
      fastBoard.delete();
      return null;
    });
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    Player player = e.getPlayer();
    if (enabledWorld.contains(player.getWorld().getName())) {
      addBoard(player);
    }
  }

  @EventHandler
  public void onWorldChange(PlayerChangedWorldEvent e) {
    Player player = e.getPlayer();
    if (enabledWorld.contains(player.getWorld().getName())) {
      if (!boardMap.containsKey(player.getUniqueId())) {
        addBoard(player);
      }
    } else {
      removeBoard(player.getUniqueId());
    }
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent e) {
    removeBoard(e.getPlayer().getUniqueId());
  }
}
