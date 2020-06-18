package fun.ccmc.dsgraph.task;

import fun.ccmc.dsgraph.DSGraph;
import fun.ccmc.dsgraph.config.StockConfig;
import org.bukkit.scheduler.BukkitRunnable;

public class CleanOldDataTask extends BukkitRunnable {
    @Override
    public void run() {
        DSGraph.getInstance().getTaskManager().stopRecordDataTask();
        DSGraph.getInstance().getCfg().getFiles().forEach(StockConfig::clean);
        DSGraph.getInstance().getTaskManager().startRecordDataTask();
    }
}
