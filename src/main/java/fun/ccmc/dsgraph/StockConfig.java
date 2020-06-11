package fun.ccmc.dsgraph;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.Getter;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class StockConfig {
    @Getter
    private final String name;
    @Getter
    private final Material material;
    @Getter
    private final String shopName;

    public StockConfig(String shopName, String name, Material material) {
        this.name = name;
        this.material = material;
        this.shopName = shopName;
    }

    public ArrayList<StockEntry> getHistory() {
        String path = DSGraph.getInstance().getDataFolder() + "/" + name + ".csv";

        ArrayList<StockEntry> results = new ArrayList<>();

        try {
            CSVReader reader = new CSVReader(new FileReader(path));
            String[] data;
            while ((data = reader.readNext()) != null) {
                if (!data[0].equals(StockEntry.Fields.Time)) {
                    results.add(new StockEntry(data[0], Integer.parseInt(data[1]), Integer.parseInt(data[2]), Double.parseDouble(data[3])));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public void update() {
        String path = DSGraph.getInstance().getDataFolder() + "/" + name + ".csv";

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

        StockEntry newEntry = new StockEntry(shopName, new ItemStack(material));
        if (!newEntry.equals(lastEntry)) {
            try {
                CSVWriter writer = new CSVWriter(new FileWriter(path, true));
                if (lastEntry != null) {
                    lastEntry.setTime(LocalDateTime.now().minusSeconds(1L));
                    writer.writeNext(lastEntry.getRecord());
                }
                writer.writeNext(newEntry.getRecord());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
