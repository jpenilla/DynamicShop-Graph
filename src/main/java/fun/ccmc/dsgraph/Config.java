package fun.ccmc.dsgraph;

import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

@FieldNameConstants
public class Config {
    private final DSGraph plugin;

    @Getter
    private final ArrayList<StockConfig> files = new ArrayList<>();
    @Getter
    private final ArrayList<GraphConfig> graphConfigs = new ArrayList<>();
    @Getter
    private int updateDataIntervalSeconds;
    @Getter
    private boolean saveUnchangedData;
    @Getter
    private int deleteAfterDays;

    public Config(DSGraph plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        plugin.saveConfig();
        load();
    }

    private void load() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        files.clear();
        config.getConfigurationSection(Fields.files).getKeys(false).forEach(key -> {
            String shopName = config.getString(Fields.files + "." + key + "." + StockConfig.Fields.shopName);
            Material material = Material.getMaterial(config.getString(Fields.files + "." + key + "." + StockConfig.Fields.material));
            if (material != null) {
                files.add(new StockConfig(shopName, key, material));
            }
        });

        graphConfigs.clear();
        files.forEach(f -> {
            config.getConfigurationSection(Fields.graphConfigs).getKeys(false).forEach(key -> {
                String name = config.getString(Fields.graphConfigs + "." + key + "." + GraphConfig.Fields.stockConfig);
                if (f.getName().equals(name)) {
                    int length = config.getInt(Fields.graphConfigs + "." + key + "." + GraphConfig.Fields.graphLengthMinutes);
                    GraphConfig.GraphType type = GraphConfig.GraphType.valueOf(config.getString(Fields.graphConfigs + "." + key + "." + GraphConfig.Fields.type));
                    graphConfigs.add(new GraphConfig(key, f, length, type));
                }
            });
        });

        updateDataIntervalSeconds = config.getInt(Fields.updateDataIntervalSeconds);
        saveUnchangedData = config.getBoolean(Fields.saveUnchangedData);
        deleteAfterDays = config.getInt(Fields.deleteAfterDays);
    }
}
