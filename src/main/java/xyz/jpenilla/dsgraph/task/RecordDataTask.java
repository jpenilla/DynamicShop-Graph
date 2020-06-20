package xyz.jpenilla.dsgraph.task;

import com.google.gson.Gson;
import xyz.jpenilla.dsgraph.DSGraph;
import xyz.jpenilla.dsgraph.config.StockConfig;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecordDataTask extends BukkitRunnable {
    private final ArrayList<StockConfig> queue = new ArrayList<>();
    private boolean firstRun = true;

    @Override
    public void run() {
        if (queue.size() != 0) {
            if (firstRun) {
                firstRun = false;
                try {
                    List<String> datas = DSGraph.getInstance().getCfg().getFiles().stream().map(StockConfig::getName).collect(Collectors.toList());
                    Writer writer = new FileWriter(StockConfig.folderPath + "availableData.json");
                    new Gson().toJson(datas, writer);
                    writer.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            ArrayList<StockConfig> temp = new ArrayList<>(queue);
            temp.forEach(item -> {
                queue.remove(item);
                item.update();
            });
        }
    }

    public void queue(StockConfig config) {
        if (!queue.contains(config)) {
            queue.add(config);
        }
    }
}