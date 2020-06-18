package fun.ccmc.dsgraph.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import fun.ccmc.dsgraph.DSGraph;
import fun.ccmc.jmplib.Chat;
import org.bukkit.command.CommandSender;

@CommandAlias("dsgraph|dsg")
public class CommandDSGraph extends BaseCommand {
    private final DSGraph plugin;

    public CommandDSGraph(DSGraph p) {
        plugin = p;
    }

    @Default
    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        String m = "&f---&a[ &5&l" + plugin.getName() + "&d&l Help &a]&f---";
        Chat.sendMsg(sender, m);
        help.showHelp();
    }

    @Subcommand("about")
    public void onAbout(CommandSender sender) {
        String[] m = new String[]{
                "&a==========================",
                plugin.getName() + " &d&o" + plugin.getDescription().getVersion(),
                "&7By &bjmp",
                "&a=========================="
        };
        Chat.sendCenteredMessage(sender, m);
    }

    @Subcommand("reload")
    public void onReload(CommandSender sender) {
        Chat.sendCenteredMessage(sender, "&a&oReloading plugin...");
        plugin.getCfg().load();
        plugin.getTaskManager().restart();
        Chat.sendCenteredMessage(sender, "&aDone reloading plugin");
    }
}
