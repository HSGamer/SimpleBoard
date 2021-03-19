package me.hsgamer.simpleboard;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BoardManager {
    private final Map<UUID, Board> boardMap = new HashMap<>();
    private final SimpleBoard instance;

    BoardManager(SimpleBoard instance) {
        this.instance = instance;
    }

    public void addBoard(Player player) {
        boardMap.put(player.getUniqueId(), new Board(instance, player));
    }

    public void removeBoard(Player player) {
        Optional.ofNullable(boardMap.remove(player.getUniqueId())).ifPresent(Board::cancel);
    }

    public void clear() {
        boardMap.values().forEach(Board::cancel);
        boardMap.clear();
    }
}
