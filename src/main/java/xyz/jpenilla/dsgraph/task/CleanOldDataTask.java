package xyz.jpenilla.dsgraph.task;

import xyz.jpenilla.dsgraph.DSGraph;
import xyz.jpenilla.dsgraph.config.StockConfig;
import org.bukkit.scheduler.BukkitRunnable;

public class CleanOldDataTask extends BukkitRunnable {
    @Override
    public void run() {
        DSGraph.getInstance().getTaskManager().stopRecordDataTask();
        DSGraph.getInstance().getCfg().getFiles().forEach(StockConfig::clean);
        DSGraph.getInstance().getTaskManager().startRecordDataTask();
    }
}
