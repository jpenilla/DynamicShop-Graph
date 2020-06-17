package fun.ccmc.dsgraph.task;

import fun.ccmc.dsgraph.DSGraph;
import org.bukkit.scheduler.BukkitRunnable;

public class QueueUpdatesTask extends BukkitRunnable {
    @Override
    public void run() {
        if (DSGraph.getInstance().getRecordDataTask() != null) {
            DSGraph.getInstance().getCfg().getFiles().forEach(stockConfig -> {
                DSGraph.getInstance().getRecordDataTask().queue(stockConfig);
            });
        }
    }
}
