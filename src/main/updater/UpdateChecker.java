package main.updater;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import javax.net.ssl.HttpsURLConnection;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import main.utils.MsgUtils;

public class UpdateChecker implements Listener {
    private final int RESOURCE_ID = 4390;
    private Plugin plugin;
    private String spigotVersion;
    private String pluginVersion;
    private boolean updateAvailable;

    public UpdateChecker(Plugin i) {
        this.plugin = i;
        this.pluginVersion = i.getDescription().getVersion();
    }

    public boolean hasUpdateAvailable() {
        return this.updateAvailable;
    }

    public String getSpigotVersion() {
        return this.spigotVersion;
    }

    public void fetch() {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try {
                HttpsURLConnection con = (HttpsURLConnection)(new URL("https://api.spigotmc.org/legacy/update.php?resource=4390")).openConnection();
                con.setRequestMethod("GET");
                this.spigotVersion = (new BufferedReader(new InputStreamReader(con.getInputStream()))).readLine();
            } catch (Exception var2) {
                this.plugin.getLogger().info("Failed to check for updates on spigot.");
                return;
            }

            if (this.spigotVersion != null && !this.spigotVersion.isEmpty()) {
                this.updateAvailable = this.spigotIsNewer();
                if (this.updateAvailable) {
                    Bukkit.getScheduler().runTask(this.plugin, () -> {
                        this.plugin.getLogger().info("An update for DeluxeTags (v" + this.getSpigotVersion() + ") is available at:");
                        this.plugin.getLogger().info("https://www.spigotmc.org/resources/deluxetags.4390/");
                        Bukkit.getPluginManager().registerEvents(this, this.plugin);
                    });
                }
            }
        });
    }

    private boolean spigotIsNewer() {
        if (this.spigotVersion != null && !this.spigotVersion.isEmpty()) {
            String plV = this.toReadable(this.pluginVersion);
            String spV = this.toReadable(this.spigotVersion);
            return plV.compareTo(spV) < 0;
        } else {
            return false;
        }
    }

    private String toReadable(String version) {
        if (version.contains("-DEV-")) {
            version = version.split("-DEV-")[0];
        }

        return version.replaceAll("\\.", "");
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("deluxetags.updates")) {
            MsgUtils.msg(e.getPlayer(), Arrays.toString(new String[]{"&bAn update for &5&lDeluxeTags &e(&5&lDeluxeTags &fv" + this.getSpigotVersion() + "&e)", "&bis available at &ehttps://www.spigotmc.org/resources/deluxetags.4390/"}));
        }

    }
}
