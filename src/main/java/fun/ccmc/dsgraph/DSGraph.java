package fun.ccmc.dsgraph;

import fun.ccmc.dsgraph.config.Config;
import fun.ccmc.dsgraph.task.CleanOldDataTask;
import fun.ccmc.dsgraph.task.QueueUpdatesTask;
import fun.ccmc.dsgraph.task.RecordDataTask;
import fun.ccmc.dsgraph.task.StockGraphTask;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class DSGraph extends JavaPlugin {
    @Getter
    private static DSGraph instance;
    @Getter
    private Config cfg;
    @Getter
    private RecordDataTask recordDataTask = null;

    @Override
    public void onEnable() {
        instance = this;
        this.cfg = new Config(this);

        startRecording();
        new QueueUpdatesTask().runTaskTimerAsynchronously(this, 0L, 20L * 60L);
        new CleanOldDataTask().runTaskTimerAsynchronously(this, 20L * 10L, 20L * 60L * 5L);

        cfg.getGraphConfigs().forEach(file -> {
            new StockGraphTask(file).runTaskTimerAsynchronously(this, 0L, 20L * file.getGraphRefreshTimeSeconds());
        });

        int pluginId = 7828;
        Metrics metrics = new Metrics(this, pluginId);

        new WebServer().runTaskAsynchronously(this);

        getServer().getPluginManager().registerEvents(new ShopListener(), this);
    }

    @Override
    public void onDisable() {
    }

    public void startRecording() {
        if (recordDataTask != null) {
            recordDataTask.cancel();
        }
        recordDataTask = new RecordDataTask();
        recordDataTask.runTaskTimerAsynchronously(this, 0L, 20L * 2L);
    }

    public void stopRecording() {
        recordDataTask.cancel();
        recordDataTask = null;
    }
}
