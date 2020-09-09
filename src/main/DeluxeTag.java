package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;

public class DeluxeTag {
    private static TreeMap<Integer, DeluxeTag> configTags;
    private static Map<String, DeluxeTag> playerTags;
    private String identifier;
    private String displayTag;
    private String description;
    private String permission;
    private int priority;

    public DeluxeTag(int priority, String identifier, String displayTag, String description) {
        this.priority = priority;
        this.identifier = identifier;
        this.displayTag = displayTag;
        this.description = description;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getDisplayTag() {
        return this.displayTag;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDisplayTag(String newDisplayTag) {
        this.displayTag = newDisplayTag;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getPermission() {
        return this.permission == null ? "deluxetags.tag." + this.identifier : this.permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void load() {
        if (configTags == null) {
            configTags = new TreeMap();
        }

        configTags.put(this.priority, this);
    }

    public boolean unload() {
        if (configTags != null && !configTags.isEmpty()) {
            if (configTags.containsKey(this.priority)) {
                configTags.remove(this.priority);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean hasTagPermission(Player p) {
        return p.hasPermission(this.getPermission());
    }

    public boolean hasForceTagPermission(Player p) {
        return p.hasPermission("deluxetags.forcetag." + this.identifier);
    }

    public boolean setPlayerTag(Player p) {
        return this.setPlayerTag(p.getUniqueId().toString());
    }

    public boolean setPlayerTag(String uuid) {
        if (playerTags == null) {
            playerTags = new HashMap();
        }

        if (playerTags.isEmpty()) {
            playerTags.put(uuid, this);
            return true;
        } else if (playerTags.containsKey(uuid) && playerTags.get(uuid) == this) {
            return false;
        } else {
            playerTags.put(uuid, this);
            return true;
        }
    }

    public List<String> removeActivePlayers() {
        if (playerTags != null && !playerTags.isEmpty()) {
            List<String> remove = new ArrayList();
            Iterator var2 = playerTags.keySet().iterator();

            String uuid;
            while(var2.hasNext()) {
                uuid = (String)var2.next();
                if (getPlayerDisplayTag(uuid).equals(this.displayTag)) {
                    remove.add(uuid);
                }
            }

            if (!remove.isEmpty()) {
                var2 = remove.iterator();

                while(var2.hasNext()) {
                    uuid = (String)var2.next();
                    removePlayer(uuid);
                }
            }

            return remove;
        } else {
            return null;
        }
    }

    public static Collection<DeluxeTag> getLoadedTags() {
        return configTags == null ? null : configTags.values();
    }

    public static boolean hasTagLoaded(String uuid) {
        return playerTags != null && !playerTags.isEmpty() ? playerTags.containsKey(uuid) : false;
    }

    public static boolean hasTagLoaded(Player player) {
        return hasTagLoaded(player.getUniqueId().toString());
    }

    public static String getPlayerTagIdentifier(Player p) {
        return getPlayerTagIdentifier(p.getUniqueId().toString());
    }

    public static DeluxeTag getTag(String uuid) {
        if (playerTags == null) {
            playerTags = new HashMap();
        }

        if (playerTags.isEmpty()) {
            return null;
        } else {
            return playerTags.containsKey(uuid) && playerTags.get(uuid) != null ? (DeluxeTag)playerTags.get(uuid) : null;
        }
    }

    public static String getPlayerTagIdentifier(String uuid) {
        if (playerTags == null) {
            playerTags = new HashMap();
        }

        if (playerTags.isEmpty()) {
            return null;
        } else {
            return playerTags.containsKey(uuid) && playerTags.get(uuid) != null && ((DeluxeTag)playerTags.get(uuid)).getIdentifier() != null ? ((DeluxeTag)playerTags.get(uuid)).getIdentifier() : null;
        }
    }

    public static String getPlayerDisplayTag(Player player) {
        String d = getPlayerDisplayTag(player.getUniqueId().toString());
//        return DeluxeTags.papi() ? PlaceholderAPI.setPlaceholders(player, d) : d;
        return d;
    }

    public static String getPlayerDisplayTag(String uuid) {
        if (playerTags == null) {
            playerTags = new HashMap();
        }

        if (playerTags.isEmpty()) {
            return "";
        } else {
            return playerTags.containsKey(uuid) && playerTags.get(uuid) != null && ((DeluxeTag)playerTags.get(uuid)).getDisplayTag() != null ? ((DeluxeTag)playerTags.get(uuid)).getDisplayTag() : "";
        }
    }

    public static String getPlayerTagDescription(Player p) {
        String d = getPlayerTagDescription(p.getUniqueId().toString());
//        return DeluxeTags.papi() ? PlaceholderAPI.setPlaceholders(p, d) : d;
        return d;
    }

    public static String getPlayerTagDescription(String uuid) {
        if (playerTags == null) {
            playerTags = new HashMap();
        }

        if (playerTags.isEmpty()) {
            return "";
        } else {
            return playerTags.containsKey(uuid) && playerTags.get(uuid) != null && ((DeluxeTag)playerTags.get(uuid)).getDescription() != null ? ((DeluxeTag)playerTags.get(uuid)).getDescription() : "";
        }
    }

    public static DeluxeTag getLoadedTag(String identifier) {
        if (configTags != null && !configTags.isEmpty()) {
            Iterator var1 = getLoadedTags().iterator();

            while(var1.hasNext()) {
                DeluxeTag t = (DeluxeTag)var1.next();
                if (t.getIdentifier().equals(identifier)) {
                    return t;
                }
            }
        }

        return null;
    }

    public static DeluxeTag getForcedTag(Player p) {
        if (getLoadedTags() != null && !getLoadedTags().isEmpty()) {
            Iterator it = configTags.keySet().iterator();

            DeluxeTag t;
            do {
                if (!it.hasNext()) {
                    return null;
                }

                t = (DeluxeTag)configTags.get(it.next());
            } while(t == null || !t.hasForceTagPermission(p));

            return t;
        } else {
            return null;
        }
    }

    public static List<String> getAvailableTagIdentifiers(Player p) {
        return getLoadedTags() != null && !getLoadedTags().isEmpty() ? (List)getLoadedTags().stream().filter((t) -> {
            return t.hasTagPermission(p);
        }).map(DeluxeTag::getIdentifier).collect(Collectors.toList()) : null;
    }

    public static List<String> getAllTagIdentifiers() {
        return getLoadedTags() != null && !getLoadedTags().isEmpty() ? (List)getLoadedTags().stream().map(DeluxeTag::getIdentifier).collect(Collectors.toList()) : null;
    }

    public static int getLoadedTagsAmount() {
        return configTags != null && !configTags.isEmpty() ? configTags.size() : 0;
    }

    public static Set<String> getLoadedPlayers() {
        return playerTags != null && !playerTags.isEmpty() ? playerTags.keySet() : null;
    }

    public static void removePlayer(String uuid) {
        if (hasTagLoaded(uuid)) {
            playerTags.remove(uuid);
        }

    }

    public static void unloadData() {
        configTags = null;
        playerTags = null;
    }
}
