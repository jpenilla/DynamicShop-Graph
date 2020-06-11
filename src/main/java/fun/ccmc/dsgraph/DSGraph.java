package fun.ccmc.dsgraph;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class DSGraph extends JavaPlugin {
    @Getter private static DSGraph instance;
    @Getter private Config cfg;

    private BukkitRunnable recordDataTask = null;

    @Override
    public void onEnable() {
        instance = this;
        this.cfg = new Config(this);

        new CleanOldDataTask().runTaskTimerAsynchronously(this, 20L * 10L, 20L * 60L * 5L);
        startRecording();

        cfg.getFiles().forEach(file -> {
            new StockGraphTask(file).runTaskTimerAsynchronously(this, 0L, 20L * 10L);
        });
    }

    @Override
    public void onDisable() {
    }

    public void startRecording() {
        if (recordDataTask != null) {
            recordDataTask.cancel();
        }
        recordDataTask = new RecordDataTask();
        recordDataTask.runTaskTimerAsynchronously(this, 0L, 20L * cfg.getUpdateDataIntervalSeconds());
    }

    public void stopRecording() {
        recordDataTask.cancel();
        recordDataTask = null;
    }
}
