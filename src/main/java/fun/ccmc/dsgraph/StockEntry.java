package fun.ccmc.dsgraph;

import lombok.NonNull;
import lombok.experimental.FieldNameConstants;
import me.sat7.dynamicshop.DynaShopAPI;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@FieldNameConstants
public class StockEntry {
    private final int Median;
    private final int Stock;
    private final double Price;
    private String Time;

    public StockEntry(@NonNull String shopName, @NonNull ItemStack item) {
        this.Median = DynaShopAPI.getMedian(shopName, item);
        this.Stock = DynaShopAPI.getStock(shopName, item);
        this.Price = DynaShopAPI.getBuyPrice(shopName, item);
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

    private void setTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.Time = dtf.format(now);
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
}
