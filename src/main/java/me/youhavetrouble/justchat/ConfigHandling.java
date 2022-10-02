package me.youhavetrouble.justchat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConfigHandling {
    private static final Plugin plugin = JustChat.getInstance();
    public enum Message{
        CHAT_FORMAT(plugin.getConfig().getString("format", "<%player_displayname%> %message%")),
        NO_PERMISSION("<red>You do not have permission to use this command"),
        CONFIG_RELOADED("<gold>JustChat config has been reloaded"),
        INVALID_COMMAND("<red>Invalid Command");

        private String message;
        Message(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    public static void reloadPluginConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
    }

    public static void setConfigDefaults(){
        plugin.saveDefaultConfig();
        plugin.getConfig().addDefault("format", "<%player_displayname%> %message%");
    }
}
