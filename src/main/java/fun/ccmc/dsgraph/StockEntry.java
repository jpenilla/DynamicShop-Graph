package fun.ccmc.dsgraph;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldNameConstants;
import me.sat7.dynamicshop.DynaShopAPI;
import org.bukkit.inventory.ItemStack;
import org.jfree.data.time.Second;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@FieldNameConstants
public class StockEntry {
    @Getter
    private final int Median;
    @Getter
    private final int Stock;
    @Getter
    private final double Price;
    @Getter
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

    public void setTime(LocalDateTime time) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        this.Time = dtf.format(time);
    }

    public void setTime() {
        LocalDateTime now = LocalDateTime.now();
        setTime(now);
    }

    public Second getSecond() {
        String[] s = Time.split("[ /:]");
        return new Second(Integer.parseInt(s[5]), Integer.parseInt(s[4]), Integer.parseInt(s[3]), Integer.parseInt(s[1]), Integer.parseInt(s[0]), Integer.parseInt(s[2]));
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
