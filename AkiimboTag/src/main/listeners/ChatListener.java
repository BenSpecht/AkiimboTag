package main.listeners;

import main.DeluxeTag;
import main.DeluxeTags;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    DeluxeTags plugin;

    public ChatListener(DeluxeTags i) {
        this.plugin = i;
    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onChat(AsyncPlayerChatEvent e) {
        String format = e.getFormat();
        format = DeluxeTags.setPlaceholders(e.getPlayer(), format, (DeluxeTag)null);
        e.setFormat(format);
    }
}
