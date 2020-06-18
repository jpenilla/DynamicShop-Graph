package fun.ccmc.dsgraph.task;

import fun.ccmc.dsgraph.config.StockConfig;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class RecordDataTask extends BukkitRunnable {
    private final ArrayList<StockConfig> queue = new ArrayList<>();

    @Override
    public void run() {
        if (queue.size() != 0) {
            ArrayList<StockConfig> temp = new ArrayList<>(queue);
            queue.forEach(item -> {
                temp.remove(item);
                item.update();
            });
            queue.clear();
            queue.addAll(temp);
        }
    }

    public void queue(StockConfig config) {
        if (!queue.contains(config)) {
            queue.add(config);
        }
    }
}