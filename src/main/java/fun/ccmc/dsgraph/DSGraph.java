package fun.ccmc.dsgraph;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class DSGraph extends JavaPlugin {
    @Getter private static DSGraph instance;
    @Getter private Config cfg;

    @Override
    public void onEnable() {
        instance = this;
        this.cfg = new Config(this);
        new RecordDataTask().runTaskTimerAsynchronously(this, 0L, 20L * 5L);
        cfg.getFiles().forEach(file -> {
            new StockGraphTask(file).runTaskTimerAsynchronously(this, 0L, 20L * 15L);
        });
    }

    @Override
    public void onDisable() {
    }
}
