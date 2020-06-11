package fun.ccmc.dsgraph;

import com.opencsv.CSVWriter;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;

public class RecordDataTask extends BukkitRunnable {
    @Override
    public void run() {
        DSGraph.getInstance().getCfg().getFiles().forEach(this::update);
    }

    private void update(StockConfig config) {
        String path = DSGraph.getInstance().getDataFolder() + "/" + config.getName() + ".csv";

        StockEntry lastEntry = null;

        File file = new File(path);
        if (!file.exists()) {
            try {
                CSVWriter csvWriter = new CSVWriter(new FileWriter(path), ',');
                csvWriter.writeNext(StockEntry.getHeader());
                csvWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String lastLine = new ReversedLinesFileReader(file, Charset.defaultCharset()).readLine();
                String[] data = lastLine.replace("\"", "").split(",");
                lastEntry = new StockEntry(data[0], Integer.parseInt(data[1]), Integer.parseInt(data[2]), Double.parseDouble(data[3]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        StockEntry newEntry = new StockEntry(config.getShopName(), new ItemStack(config.getMaterial()));
        if (!newEntry.equals(lastEntry)) {
            try {
                CSVWriter writer = new CSVWriter(new FileWriter(path, true));
                writer.writeNext(newEntry.getRecord());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
