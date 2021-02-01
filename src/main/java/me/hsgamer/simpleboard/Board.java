package me.hsgamer.simpleboard;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Board extends FastBoard {
    private final SimpleBoard instance;
    private final BukkitTask task;

    public Board(SimpleBoard instance, Player player) {
        super(player);
        this.instance = instance;
        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(instance, this::update, 0, instance.getHandler().getUpdateTime());
    }

    public void update() {
        updateTitle(Utils.format(getPlayer().getUniqueId(), instance.getHandler().getTitle()));
        List<String> lines = new ArrayList<>(instance.getHandler().getLines());
        lines.replaceAll(s -> Utils.format(getPlayer().getUniqueId(), s));
        updateLines(lines);
    }

    public void cancel() {
        task.cancel();
    }
}
