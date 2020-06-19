package fun.ccmc.dsgraph;

import fun.ccmc.dsgraph.task.CleanOldDataTask;
import fun.ccmc.dsgraph.task.QueueUpdatesTask;
import fun.ccmc.dsgraph.task.RecordDataTask;
import fun.ccmc.dsgraph.task.WebServerTask;
import lombok.Getter;

public class TaskManager {
    private final DSGraph plugin;
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
        if (DSGraph.getInstance().getCfg().isWebServer()) {
            startWebServerTask();
        }
    }

    public void stop() {
        stopWebServerTask();
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
}
