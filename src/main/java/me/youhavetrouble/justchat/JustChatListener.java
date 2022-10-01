package me.youhavetrouble.justchat;

import io.papermc.paper.event.player.AsyncChatCommandDecorateEvent;
import io.papermc.paper.event.player.AsyncChatDecorateEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerChatPreviewEvent;
import org.bukkit.permissions.Permission;


import java.util.HashMap;

public class JustChatListener implements Listener {

    private final HashMap<Permission, TagResolver> formattingPerms = new HashMap<>();
    private final PlainTextComponentSerializer plainTextComponentSerializer = PlainTextComponentSerializer.plainText();

    public JustChatListener() {
        formattingPerms.put(new Permission("justchat.color"), StandardTags.color());
        formattingPerms.put(new Permission("justchat.bold"), StandardTags.decorations(TextDecoration.BOLD));
        formattingPerms.put(new Permission("justchat.italic"), StandardTags.decorations(TextDecoration.ITALIC));
        formattingPerms.put(new Permission("justchat.underlined"), StandardTags.decorations(TextDecoration.UNDERLINED));
        formattingPerms.put(new Permission("justchat.strikethrough"), StandardTags.decorations(TextDecoration.STRIKETHROUGH));
        formattingPerms.put(new Permission("justchat.obfuscated"), StandardTags.decorations(TextDecoration.OBFUSCATED));
        formattingPerms.put(new Permission("justchat.gradient"), StandardTags.gradient());
        formattingPerms.put(new Permission("justchat.font"), StandardTags.font());
        formattingPerms.put(new Permission("justchat.rainbow"), StandardTags.rainbow());
        formattingPerms.put(new Permission("justchat.hover"), StandardTags.hoverEvent());
        formattingPerms.put(new Permission("justchat.click"), StandardTags.clickEvent());
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onjustChatDecorate(AsyncChatDecorateEvent event) {

        if (event.player() == null) return;

        String format = ConfigHandling.getPluginMessages().get(ConfigHandling.Message.CHAT_FORMAT);

        format = PlaceholderAPI.setPlaceholders(event.player(), format);
        Component formatComponent = JustChat.getMiniMessage().deserialize(format);

        Component message = parseMessageContent(event.player(), plainTextComponentSerializer.serialize(event.originalMessage()));

        event.result(formatComponent.replaceText(TextReplacementConfig.builder().match("%message%").replacement(message).build()));
    }

    @EventHandler
    public void onAsyncChatPreviewEvent(AsyncPlayerChatPreviewEvent event) {
        event.setFormat("%2$s");
    }

    @EventHandler
    public void onAsyncChatEvent(AsyncPlayerChatEvent event) {
        event.setFormat("%2$s");
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onjustChatCommandDecorate(AsyncChatCommandDecorateEvent event) {
        if (event.player() == null) return;
        event.result(parseMessageContent(event.player(), plainTextComponentSerializer.serialize(event.originalMessage())));
    }

    private Component parseMessageContent(Player player, String rawMessage) {
        TagResolver.Builder tagResolver = TagResolver.builder();

        formattingPerms.forEach((perm, resolver) -> {
            if (player.hasPermission(perm)) {
                tagResolver.resolver(resolver);
            }
        });

        MiniMessage messageParser = MiniMessage.builder().tags(tagResolver.build()).build();
        return messageParser.deserialize(rawMessage);

    }

}
