package me.hsgamer.simpleboard;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class SimpleBoard extends JavaPlugin implements Listener {
    private final Map<UUID, Board> boardMap = new HashMap<>();
    private BoardHandler handler;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        handler = BoardHandler.load(this);
    }

    @Override
    public void onDisable() {
        boardMap.values().forEach(Board::cancel);
        boardMap.clear();
        HandlerList.unregisterAll((Plugin) this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        boardMap.put(player.getUniqueId(), new Board(this, player));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Optional.ofNullable(boardMap.remove(e.getPlayer().getUniqueId())).ifPresent(Board::cancel);
    }

    public BoardHandler getHandler() {
        return handler;
    }
}
