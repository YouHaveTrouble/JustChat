package me.youhavetrouble.justchat;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class JustChat extends JavaPlugin {

    private static String chatFormat;

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        reloadPluginConfig();
        getServer().getPluginManager().registerEvents(new JustChatListener(), this);
    }

    public void reloadPluginConfig() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        chatFormat = config.getString("format", "<%player_displayname%> %message%");
    }

    public static String getChatFormat() {
        return chatFormat;
    }

    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }
}
