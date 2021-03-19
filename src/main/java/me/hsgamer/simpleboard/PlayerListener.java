package me.hsgamer.simpleboard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final SimpleBoard instance;

    PlayerListener(SimpleBoard instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        instance.getManager().addBoard(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        instance.getManager().removeBoard(e.getPlayer());
    }
}
