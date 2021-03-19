package me.hsgamer.simpleboard;

import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import org.bukkit.event.Listener;

public final class SimpleBoard extends BasePlugin implements Listener {
    private final BoardManager manager = new BoardManager(this);
    private BoardHandler handler;

    @Override
    public void enable() {
        registerListener(new PlayerListener(this));
        handler = BoardHandler.load(this);
    }

    @Override
    public void disable() {
        manager.clear();
    }

    public BoardHandler getHandler() {
        return handler;
    }

    public BoardManager getManager() {
        return manager;
    }
}
