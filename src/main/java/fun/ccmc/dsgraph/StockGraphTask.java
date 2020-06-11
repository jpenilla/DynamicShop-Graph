package fun.ccmc.dsgraph;

import org.bukkit.scheduler.BukkitRunnable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StockGraphTask extends BukkitRunnable {
    private final StockConfig stockConfig;

    public StockGraphTask(StockConfig config) {
        this.stockConfig = config;
    }

    @Override
    public void run() {
        TimeSeries series = new TimeSeries(stockConfig.getName());
        ArrayList<StockEntry> l = stockConfig.getHistory();
        l.forEach(entry -> {
            series.add(entry.getSecond(), entry.getPrice());
        });

        final XYDataset dataset = (XYDataset) new TimeSeriesCollection(series);
        JFreeChart timechart = ChartFactory.createTimeSeriesChart(
                stockConfig.getName(),
                "Time",
                "Price",
                dataset,
                false,
                false,
                false);

        int width = 1280;   /* Width of the image */
        int height = 720;  /* Height of the image */
        File timeChart = new File(DSGraph.getInstance().getDataFolder() + "/" + stockConfig.getName() + ".jpeg");
        try {
            ChartUtils.saveChartAsJPEG(timeChart, timechart, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
