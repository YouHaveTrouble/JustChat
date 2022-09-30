package me.youhavetrouble.justchat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConfigReload {
    private static final Plugin plugin = JustChat.plugin;
    private static final FileConfiguration config = plugin.getConfig();
    private static final HashMap<messages, String> pluginMessages = new HashMap<>();
    public enum messages{
        CHAT_FORMAT, NO_PERMISSION, CONFIG_RELOADED, INVALID_COMMAND
    }

    public static void reloadPluginConfig() {
        plugin.reloadConfig();
        setPluginMessages();
    }

    public static void setConfigDefaults(){
        plugin.saveDefaultConfig();
        config.addDefault("format", "<%player_displayname%> %message%");
        setPluginMessages();
    }

    private static void setPluginMessages(){
        pluginMessages.clear();
        pluginMessages.put(messages.CHAT_FORMAT, config.getString("format", "<%player_displayname%> %message%"));
        pluginMessages.put(messages.NO_PERMISSION, "You do not have permission to use this command");
        pluginMessages.put(messages.CONFIG_RELOADED, "JustChat config has been reloaded");
        pluginMessages.put(messages.INVALID_COMMAND, "Invalid Command");
    }

    public static Map<messages, String> getPluginMessages() {
        return Collections.unmodifiableMap(pluginMessages);
    }
}
