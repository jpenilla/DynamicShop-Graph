package fun.ccmc.dsgraph.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import fun.ccmc.dsgraph.DSGraph;
import fun.ccmc.jmplib.Chat;
import org.bukkit.ChatColor;
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
        String m = ChatColor.AQUA + plugin.getName() + ChatColor.DARK_AQUA + " Help";
        Chat.sendMsg(sender, m);
        help.showHelp();
    }

    @Subcommand("about")
    public void onAbout(CommandSender sender) {
        String[] m = new String[]{
                ChatColor.AQUA + "==========================",
                plugin.getName() + " &1&o" + plugin.getDescription().getVersion(),
                "&7By &bjmp",
                ChatColor.AQUA + "=========================="
        };
        Chat.sendCenteredMessage(sender, m);
    }

    @Subcommand("reload")
    @CommandPermission("dsgraph.reload")
    public void onReload(CommandSender sender) {
        Chat.sendCenteredMessage(sender, "&a&oReloading plugin...");
        plugin.getCfg().load();
        plugin.getTaskManager().restart();
        Chat.sendCenteredMessage(sender, "&aDone reloading plugin");
    }
}
