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
    @Getter
    private final String path;

    public StockConfig(String shopName, String name, Material material) {
        this.name = name;
        this.material = material;
        this.shopName = shopName;
        this.path = DSGraph.getInstance().getDataFolder() + "/" + name + ".csv";
    }

    public ArrayList<StockEntry> getHistory() {
        ArrayList<StockEntry> results = new ArrayList<>();

        try {
            CSVReader reader = new CSVReader(new FileReader(path));
            String[] data;
            while ((data = reader.readNext()) != null) {
                if (!data[0].equals(StockEntry.Fields.Time)) {
                    results.add(new StockEntry(data));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public void update() {
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
                lastEntry = new StockEntry(lastLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        StockEntry newEntry = new StockEntry(shopName, new ItemStack(material));
        if (!newEntry.equals(lastEntry) || DSGraph.getInstance().getCfg().isSaveUnchangedData()) {
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

    public void clean() {
        try {
            ArrayList<String[]> al = new ArrayList<>();
            CSVReader reader2 = new CSVReader(new FileReader(path));
            reader2.readAll().forEach(line -> {
                if (!line[0].equals("Time")) {
                    StockEntry u = new StockEntry(line);
                    if (u.getLocalDateTime().isAfter(LocalDateTime.now().minusDays(DSGraph.getInstance().getCfg().getDeleteAfterDays()))) {
                        al.add(u.getRecord());
                    }
                }
            });

            FileWriter sw = new FileWriter(path);
            CSVWriter writer = new CSVWriter(sw);
            writer.writeAll(al);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
