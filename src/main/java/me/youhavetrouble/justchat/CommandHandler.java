package me.youhavetrouble.justchat;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {
    private static final String reloadPermission = "justchat.reload";
    private static final String reloadSubCommand = "reload";
    private static final MiniMessage miniMessage = JustChat.getMiniMessage();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(miniMessage.deserialize(ConfigHandling.Message.INVALID_COMMAND.getMessage()));
            return true;
        }
        String subCommand = args[0].toLowerCase();
        if (subCommand.equals(reloadSubCommand)) {
            if (!(sender instanceof Player) || sender.hasPermission(reloadPermission)) {
                ConfigHandling.reloadPluginConfig();
                sender.sendMessage(miniMessage.deserialize(ConfigHandling.Message.CONFIG_RELOADED.getMessage()));
                return true;
            }
        } else {
            sender.sendMessage(miniMessage.deserialize(ConfigHandling.Message.INVALID_COMMAND.getMessage()));
            return true;
        }
        sender.sendMessage(miniMessage.deserialize(ConfigHandling.Message.NO_PERMISSION.getMessage()));
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
