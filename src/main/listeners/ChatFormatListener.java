package main.listeners;

import main.DeluxeTags;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormatListener implements Listener {
    DeluxeTags plugin;

    public ChatFormatListener(DeluxeTags i) {
        this.plugin = i;
    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = true
    )
    public void onChat(AsyncPlayerChatEvent e) {
        e.setFormat(this.plugin.getCfg().chatFormat());
    }
}
