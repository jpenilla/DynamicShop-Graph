package fun.ccmc.dsgraph.task;

import fun.ccmc.dsgraph.DSGraph;
import fun.ccmc.dsgraph.config.GraphConfig;
import fun.ccmc.dsgraph.config.StockConfig;
import fun.ccmc.dsgraph.StockEntry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class StockGraphTask extends BukkitRunnable {
    private final String folderPath = DSGraph.getInstance().getDataFolder() + "/web/img/";
    private final StockConfig stockConfig;
    private final GraphConfig graphConfig;
    private final LocalDateTime cutoff;

    public StockGraphTask(GraphConfig config) {
        this.graphConfig = config;
        this.stockConfig = config.getStockConfig();
        this.cutoff = LocalDateTime.now().minusMinutes(config.getGraphLengthMinutes());
    }

    @Override
    public void run() {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        TimeSeries series = new TimeSeries(stockConfig.getName());

        //Plot value history
        ArrayList<StockEntry> l = stockConfig.getHistory();
        l.forEach(entry -> {
            if (entry.getLocalDateTime().isAfter(cutoff)) {
                series.addOrUpdate(entry.getSecond(), entry.get(graphConfig.getType()));
            }
        });

        //Plot the current value
        StockEntry now = new StockEntry(stockConfig.getShopName(), new ItemStack(stockConfig.getMaterial()));
        series.addOrUpdate(now.getSecond(), now.get(graphConfig.getType()));

        series.setMaximumItemAge(60L * graphConfig.getGraphLengthMinutes());

        final XYDataset dataset = new TimeSeriesCollection(series);
        JFreeChart timechart = ChartFactory.createTimeSeriesChart(
                graphConfig.getName(),
                StockEntry.Fields.Time,
                graphConfig.getType().toString(),
                dataset,
                false,
                false,
                false);

        int width = 1280;   /* Width of the image */
        int height = 720;  /* Height of the image */

        File timeChart = new File(folderPath + graphConfig.getName() + ".jpeg");
        try {
            ChartUtils.saveChartAsJPEG(timeChart, timechart, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
