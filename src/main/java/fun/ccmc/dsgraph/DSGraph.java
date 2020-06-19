package fun.ccmc.dsgraph;

import co.aikar.commands.PaperCommandManager;
import fun.ccmc.dsgraph.command.CommandDSGraph;
import fun.ccmc.dsgraph.command.CommandHelper;
import fun.ccmc.dsgraph.config.Config;
import lombok.Getter;
import lombok.Setter;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class DSGraph extends JavaPlugin {
    @Getter
    private static DSGraph instance;
    @Getter
    private Config cfg;
    @Getter
    private TaskManager taskManager;
    @Getter
    private CommandHelper commandHelper;
    @Getter
    @Setter
    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;
        this.cfg = new Config(this);
        this.taskManager = new TaskManager(this);

        this.commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.setDefaultHelpPerPage(5);
        this.commandHelper = new CommandHelper(this);
        commandHelper.register();
        commandManager.registerCommand(new CommandDSGraph(this));

        getServer().getPluginManager().registerEvents(new ShopListener(), this);

        int pluginId = 7828;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        taskManager.stop();
    }
}
