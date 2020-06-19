package fun.ccmc.dsgraph;

import fun.ccmc.dsgraph.config.GraphConfig;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldNameConstants;
import me.sat7.dynamicshop.DynaShopAPI;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

@FieldNameConstants
public class StockEntry {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Getter
    private final int Median;
    @Getter
    private final int Stock;
    @Getter
    private final double Price;
    @Getter
    @NonNull
    private String Time;

    public StockEntry(String entry) {
        String[] data = entry.replace("\"", "").split(",");
        this.Time = data[0];
        this.Median = Integer.parseInt(data[1]);
        this.Stock = Integer.parseInt(data[2]);
        this.Price = Double.parseDouble(data[3]);
    }

    public StockEntry(String[] entry) {
        this.Time = entry[0];
        this.Median = Integer.parseInt(entry[1]);
        this.Stock = Integer.parseInt(entry[2]);
        this.Price = Double.parseDouble(entry[3]);
    }

    public StockEntry(@NonNull String shopName, @NonNull ItemStack item) {
        this.Median = DynaShopAPI.getMedian(shopName, item);
        this.Stock = DynaShopAPI.getStock(shopName, item);
        this.Price = (double) Math.round(DynaShopAPI.getBuyPrice(shopName, item) * 100) / 100;
        setTime();
    }

    public StockEntry(@NonNull String time, @NonNull int median, @NonNull int stock, @NonNull double price) {
        this.Time = time;
        this.Median = median;
        this.Stock = stock;
        this.Price = price;
    }

    public static String[] getHeader() {
        return (Fields.Time + "," + Fields.Median + "," + Fields.Stock + "," + Fields.Price).split(",");
    }

    public void setTime(LocalDateTime time) {
        this.Time = DATE_TIME_FORMATTER.format(time);
    }

    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.parse(Time, DATE_TIME_FORMATTER);
    }

    public Date getDate() {
        return Date.from(getLocalDateTime().atZone(ZoneId.systemDefault()).toInstant());
    }

    public void setTime() {
        setTime(LocalDateTime.now());
    }

    public String[] getRecord() {
        return (Time + "," + Median + "," + Stock + "," + Price).split(",");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockEntry that = (StockEntry) o;
        return Median == that.Median &&
                Stock == that.Stock &&
                Double.compare(that.Price, Price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Median, Stock, Price);
    }

    public double get(GraphConfig.GraphType type) {
        if (type.equals(GraphConfig.GraphType.PRICE)) {
            return Price;
        } else if (type.equals(GraphConfig.GraphType.MEDIAN)) {
            return Median;
        } else if (type.equals(GraphConfig.GraphType.STOCK)) {
            return Stock;
        } else {
            return 0;
        }
    }
}
