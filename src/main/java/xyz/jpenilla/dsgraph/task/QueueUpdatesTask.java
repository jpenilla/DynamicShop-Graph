package xyz.jpenilla.dsgraph.task;

import xyz.jpenilla.dsgraph.DSGraph;
import org.bukkit.scheduler.BukkitRunnable;

public class QueueUpdatesTask extends BukkitRunnable {
    @Override
    public void run() {
        if (DSGraph.getInstance().getTaskManager().getRecordDataTask() != null) {
            DSGraph.getInstance().getCfg().getFiles().forEach(stockConfig -> {
                DSGraph.getInstance().getTaskManager().getRecordDataTask().queue(stockConfig);
            });
        }
    }
}
