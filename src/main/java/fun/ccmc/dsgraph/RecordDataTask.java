package fun.ccmc.dsgraph;

import org.bukkit.scheduler.BukkitRunnable;

public class RecordDataTask extends BukkitRunnable {
    @Override
    public void run() {
        DSGraph.getInstance().getCfg().getFiles().forEach(StockConfig::update);
    }
}