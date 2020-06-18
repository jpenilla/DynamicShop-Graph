package fun.ccmc.dsgraph.command;

import co.aikar.commands.BukkitMessageFormatter;
import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import fun.ccmc.dsgraph.DSGraph;
import org.bukkit.ChatColor;

public class CommandHelper {
    private final DSGraph plugin;

    public CommandHelper(DSGraph instance) {
        plugin = instance;
    }

    public void register() {
        PaperCommandManager mgr = plugin.getCommandManager();

        mgr.setFormat(MessageType.ERROR, new BukkitMessageFormatter(ChatColor.RED, ChatColor.WHITE, ChatColor.RED));
        mgr.setFormat(MessageType.SYNTAX, new BukkitMessageFormatter(ChatColor.AQUA, ChatColor.DARK_AQUA, ChatColor.WHITE));
        mgr.setFormat(MessageType.INFO, new BukkitMessageFormatter(ChatColor.AQUA, ChatColor.DARK_AQUA, ChatColor.WHITE));
        mgr.setFormat(MessageType.HELP, new BukkitMessageFormatter(ChatColor.AQUA, ChatColor.DARK_AQUA, ChatColor.WHITE));
    }
}
