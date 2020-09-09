package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CleanupTask implements Runnable {
    DeluxeTags plugin;

    public CleanupTask(DeluxeTags instance) {
        this.plugin = instance;
    }

    public void run() {
        if (DeluxeTag.getLoadedPlayers() != null && !DeluxeTag.getLoadedPlayers().isEmpty()) {
            Bukkit.getScheduler().runTask(this.plugin, () -> {
                Iterator<String> it = DeluxeTag.getLoadedPlayers().iterator();
                ArrayList remove = new ArrayList();

                while(it.hasNext()) {
                    String uuid = (String)it.next();
                    Player p = Bukkit.getServer().getPlayer(UUID.fromString(uuid));
                    if (p == null) {
                        remove.add(uuid);
                    }
                }

                if (!remove.isEmpty()) {
                    Iterator var4 = remove.iterator();

                    while(var4.hasNext()) {
                        String id = (String)var4.next();
                        DeluxeTag.removePlayer(id);
                    }

                }
            });
        }
    }
}
