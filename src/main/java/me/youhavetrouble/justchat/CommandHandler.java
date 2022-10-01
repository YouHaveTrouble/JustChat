package me.youhavetrouble.justchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandHandler implements CommandExecutor, TabCompleter {
    private static final String reloadPermission = "justchat.reload";
    private static final String reloadSubCommand = "reload";
    private static final Map<ConfigHandling.Message, String> pluginMessages = ConfigHandling.getPluginMessages();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(pluginMessages.get(ConfigHandling.Message.INVALID_COMMAND));
            return true;
        }
        String subCommand = args[0].toLowerCase();
        if (subCommand.equals(reloadSubCommand)) {
            if (!(sender instanceof Player) || sender.hasPermission(reloadPermission)) {
                ConfigHandling.reloadPluginConfig();
                sender.sendMessage(pluginMessages.get(ConfigHandling.Message.CONFIG_RELOADED));
                return true;
            }
        } else {
            sender.sendMessage(pluginMessages.get(ConfigHandling.Message.INVALID_COMMAND));
            return true;
        }
        sender.sendMessage(pluginMessages.get(ConfigHandling.Message.NO_PERMISSION));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> tabbableCommands = new ArrayList<String>(List.of(reloadSubCommand));
        if (sender.hasPermission(reloadPermission)) {
            return tabbableCommands;
        }
        return null;
    }

}
