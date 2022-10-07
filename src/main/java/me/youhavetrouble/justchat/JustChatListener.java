package me.youhavetrouble.justchat;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatCommandDecorateEvent;
import io.papermc.paper.event.player.AsyncChatDecorateEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerChatPreviewEvent;
import org.bukkit.permissions.Permission;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JustChatListener implements Listener {

    private final HashMap<Permission, TagResolver> formattingPerms = new HashMap<>();
    private final PlainTextComponentSerializer plainTextComponentSerializer = PlainTextComponentSerializer.plainText();

    private final Pattern placeholderPattern = PlaceholderAPI.getPlaceholderPattern();

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

        String format = ConfigHandling.Message.CHAT_FORMAT.getMessage();
        Component formatComponent = parseFormat(event.player(), format);

        Component message = parseMessageContent(event.player(), plainTextComponentSerializer.serialize(event.originalMessage()));

        event.result(formatComponent.replaceText(TextReplacementConfig.builder().match("%message%").replacement(message).build()));
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onAsyncChatPreviewEvent(AsyncPlayerChatPreviewEvent event) {
        event.setFormat("%2$s");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
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

    private Component parseFormat(Player player, String format) {

        Component formatComponent = JustChat.getMiniMessage().deserialize(format);

        if (PlaceholderAPI.containsPlaceholders(format)) {
            Matcher matcher = placeholderPattern.matcher(format);
            while (matcher.find()) {
                String string = matcher.group(0);

                String parsedPlaceholder = PlaceholderAPI.setPlaceholders(player, string);
                if (parsedPlaceholder.equals(string)) continue;

                Component placeholderComponent = LegacyComponentSerializer.legacySection().deserialize(parsedPlaceholder);

                TextReplacementConfig replacementConfig = TextReplacementConfig
                        .builder()
                        .match(string)
                        .replacement(placeholderComponent)
                        .build();
                formatComponent = formatComponent.replaceText(replacementConfig);
            }
        }
        return formatComponent;
    }

}
