package main.listeners;

import main.DeluxeTag;
import main.DeluxeTags;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerListener implements Listener {
    DeluxeTags plugin;

    public PlayerListener(DeluxeTags i) {
        this.plugin = i;
    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = true
    )
    public void onChat(AsyncPlayerChatEvent e) {
        if (!e.getPlayer().isOp() && DeluxeTags.forceTags()) {
            DeluxeTag tag = DeluxeTag.getForcedTag(e.getPlayer());
            if (tag != null) {
                tag.setPlayerTag(e.getPlayer());
            }
        }

        if (!DeluxeTag.hasTagLoaded(e.getPlayer())) {
            String identifier = this.plugin.getSavedTagIdentifier(e.getPlayer().getUniqueId().toString());
            if (identifier != null && DeluxeTag.getLoadedTag(identifier) != null && DeluxeTag.getLoadedTag(identifier).hasTagPermission(e.getPlayer())) {
                DeluxeTag.getLoadedTag(identifier).setPlayerTag(e.getPlayer());
            } else {
                this.plugin.getDummy().setPlayerTag(e.getPlayer());
                this.plugin.removeSavedTag(e.getPlayer().getUniqueId().toString());
            }
        }

    }
}
