package me.youhavetrouble.justchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandHandler implements CommandExecutor, TabCompleter {
    private static final String reloadPermission = "justchat.reload";
    private static final String reloadSubCommand = "reload";
    private static final Map<ConfigReload.messages, String> pluginMessages = ConfigReload.getPluginMessages();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(pluginMessages.get(ConfigReload.messages.INVALID_COMMAND));
            return true;
        }
        String subCommand = args[0].toLowerCase();
        if (subCommand.equals(reloadSubCommand)) {
            if (!(sender instanceof Player) || sender.hasPermission(reloadPermission)) {
                ConfigReload.reloadPluginConfig();
                sender.sendMessage(pluginMessages.get(ConfigReload.messages.CONFIG_RELOADED));
                return true;
            }
        }
        sender.sendMessage(pluginMessages.get(ConfigReload.messages.NO_PERMISSION));
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
