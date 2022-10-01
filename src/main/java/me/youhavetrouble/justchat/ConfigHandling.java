package me.youhavetrouble.justchat;

import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConfigHandling {
    private static final Plugin plugin = JustChat.getInstance();
    private static final HashMap<Message, String> pluginMessages = new HashMap<>();
    public enum Message{
        CHAT_FORMAT, NO_PERMISSION, CONFIG_RELOADED, INVALID_COMMAND
    }

    public static void reloadPluginConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        setPluginMessages();
    }

    public static void setConfigDefaults(){
        plugin.saveDefaultConfig();
        plugin.getConfig().addDefault("format", "<%player_displayname%> %message%");
        setPluginMessages();
    }

    private static void setPluginMessages(){
        pluginMessages.clear();
        pluginMessages.put(Message.CHAT_FORMAT, plugin.getConfig().getString("format"));
        pluginMessages.put(Message.NO_PERMISSION, "You do not have permission to use this command");
        pluginMessages.put(Message.CONFIG_RELOADED, "JustChat config has been reloaded");
        pluginMessages.put(Message.INVALID_COMMAND, "Invalid Command");
    }

    public static Map<Message, String> getPluginMessages() {
        return Collections.unmodifiableMap(pluginMessages);
    }
}
