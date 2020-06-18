package fun.ccmc.dsgraph;

import fun.ccmc.dsgraph.task.*;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class TaskManager {
    private final DSGraph plugin;
    @Getter
    private final ArrayList<StockGraphTask> stockGraphTasks = new ArrayList<>();
    @Getter
    private RecordDataTask recordDataTask = null;
    @Getter
    private QueueUpdatesTask queueUpdatesTask = null;
    @Getter
    private CleanOldDataTask cleanOldDataTask = null;
    @Getter
    private WebServerTask webServerTask = null;

    public TaskManager(DSGraph plugin) {
        this.plugin = plugin;
        start();
    }

    public void start() {
        startRecordDataTask();
        startQueueUpdatesTask();
        startCleanOldDataTask();
        startStockGraphTasks();
        startWebServerTask();
    }

    public void stop() {
        stopWebServerTask();
        stopStockGraphTasks();
        stopCleanOldDataTask();
        stopQueueUpdatesTask();
        stopRecordDataTask();
    }

    public void restart() {
        stop();
        start();
    }

    public void startWebServerTask() {
        stopWebServerTask();
        webServerTask = new WebServerTask();
        webServerTask.runTaskAsynchronously(plugin);
    }

    public void stopWebServerTask() {
        if (webServerTask != null) {
            webServerTask.cancel();
        }
        webServerTask = null;
    }

    public void startRecordDataTask() {
        stopRecordDataTask();
        recordDataTask = new RecordDataTask();
        recordDataTask.runTaskTimerAsynchronously(plugin, 0L, 20L * 2L);
    }

    public void stopRecordDataTask() {
        if (recordDataTask != null) {
            recordDataTask.cancel();
        }
        recordDataTask = null;
    }

    public void startQueueUpdatesTask() {
        stopQueueUpdatesTask();
        queueUpdatesTask = new QueueUpdatesTask();
        queueUpdatesTask.runTaskTimerAsynchronously(plugin, 0L, 20L * 60L);
    }

    public void stopQueueUpdatesTask() {
        if (queueUpdatesTask != null) {
            queueUpdatesTask.cancel();
        }
        queueUpdatesTask = null;
    }

    public void startCleanOldDataTask() {
        stopCleanOldDataTask();
        cleanOldDataTask = new CleanOldDataTask();
        cleanOldDataTask.runTaskTimerAsynchronously(plugin, 20L * 10L, 20L * 60L * 60L * 3L);
    }

    public void stopCleanOldDataTask() {
        if (cleanOldDataTask != null) {
            cleanOldDataTask.cancel();
        }
        cleanOldDataTask = null;
    }

    public void startStockGraphTasks() {
        stopStockGraphTasks();
        plugin.getCfg().getGraphConfigs().forEach(file -> {
            StockGraphTask task = new StockGraphTask(file);
            task.runTaskTimerAsynchronously(plugin, 0L, 20L * file.getGraphRefreshTimeSeconds());
            stockGraphTasks.add(task);
        });
    }

    public void stopStockGraphTasks() {
        stockGraphTasks.forEach(BukkitRunnable::cancel);
        stockGraphTasks.clear();
    }
}
