package me.youhavetrouble.justchat;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

public final class JustChat extends JavaPlugin {

    private static JustChat instance;

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        instance = this;
        ConfigHandling.setConfigDefaults();
        getServer().getPluginManager().registerEvents(new JustChatListener(), this);
        this.getCommand("justchat").setExecutor(new CommandHandler());
    }

    public static JustChat getInstance(){
        return instance;
    }

    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }

}
