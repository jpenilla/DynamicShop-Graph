package fun.ccmc.dsgraph;

import lombok.Getter;
import org.bukkit.Material;

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
}
