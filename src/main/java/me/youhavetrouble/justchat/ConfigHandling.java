package me.youhavetrouble.justchat;

import org.bukkit.plugin.Plugin;

public class ConfigHandling {
    private static final Plugin plugin = JustChat.getInstance();

    public static void reloadPluginConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        Message.CHAT_FORMAT.setMessage(plugin.getConfig().getString("format", "<%player_displayname%> %message%"));
    }

    public static void setConfigDefaults(){
        plugin.saveDefaultConfig();
        plugin.getConfig().addDefault("format", "<%player_displayname%> %message%");
    }

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
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
