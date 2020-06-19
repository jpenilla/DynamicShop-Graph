package fun.ccmc.dsgraph.config;

import fun.ccmc.dsgraph.DSGraph;
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
    private boolean saveUnchangedData;
    @Getter
    private int deleteAfterDays;
    @Getter
    private int port;
    @Getter
    private boolean webServer;

    public Config(DSGraph plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        plugin.saveConfig();
        load();
    }

    public void load() {
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

        saveUnchangedData = config.getBoolean(Fields.saveUnchangedData);
        deleteAfterDays = config.getInt(Fields.deleteAfterDays);
        port = config.getInt(Fields.port);
        webServer = config.getBoolean(Fields.webServer);
    }
}
