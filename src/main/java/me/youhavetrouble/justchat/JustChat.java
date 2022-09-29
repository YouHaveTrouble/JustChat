package me.youhavetrouble.justchat;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class JustChat extends JavaPlugin {

    public static Plugin plugin;

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        plugin = this;
        ConfigReload.setConfigDefaults();
        getServer().getPluginManager().registerEvents(new JustChatListener(), this);
        this.getCommand("justchat").setExecutor(new CommandHandler());
    }

    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }

}
