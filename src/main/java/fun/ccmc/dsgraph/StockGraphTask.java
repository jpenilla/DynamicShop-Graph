package fun.ccmc.dsgraph;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class StockGraphTask extends BukkitRunnable {
    private final StockConfig stockConfig;
    private final int cutoffMinutes = 30;
    private final LocalDateTime cutoff = LocalDateTime.now().minusMinutes(cutoffMinutes);

    public StockGraphTask(StockConfig config) {
        this.stockConfig = config;
    }

    @Override
    public void run() {
        TimeSeries series = new TimeSeries(stockConfig.getName());

        //Plot price history
        ArrayList<StockEntry> l = stockConfig.getHistory();
        l.forEach(entry -> {
            if (entry.getLocalDateTime().isAfter(cutoff)) {
                series.addOrUpdate(entry.getSecond(), entry.getPrice());
            }
        });

        //Plot the current price
        StockEntry now = new StockEntry(stockConfig.getShopName(), new ItemStack(stockConfig.getMaterial()));
        series.addOrUpdate(now.getSecond(), now.getPrice());

        series.setMaximumItemAge(60L * cutoffMinutes);

        final XYDataset dataset = new TimeSeriesCollection(series);
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

    public Date convertToDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }
}
