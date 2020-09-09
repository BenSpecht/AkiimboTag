package main;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.configuration.file.FileConfiguration;

public class TagConfig {
    DeluxeTags plugin;
    FileConfiguration config;

    public TagConfig(DeluxeTags i) {
        this.plugin = i;
        this.config = this.plugin.getConfig();
    }

    public void loadDefConfig() {
        this.config.options().header("DeluxeTags version: " + this.plugin.getDescription().getVersion() + " Main Configuration\n\nCreate your tags using the following format:\n\ndeluxetags:\n  VIP: \n    order: 1\n    tag: '&7[&eVIP&7]'\n    description: 'This tag is awarded by getting VIP'\n\nPlaceholders for your DeluxeChat formats main.config:\n\n%deluxetags_identifier% - display the players active tag identifier\n%deluxetags_tag% - display the players active tag\n%deluxetags_description% - display the players active tag description\n%deluxetags_amount% - display the amount of tags a player has access to\n\nPlaceholders for your essentials/chat handling formats main.config:\n\n{deluxetags_identifier} - display the players active tag identifier\n{deluxetags_tag} - display the players active tag\n{deluxetags_description} - display the players active tag description\n{deluxetags_amount} - display the amount of tags a player has access to");
        this.config.addDefault("force_tags", false);
        this.config.addDefault("check_updates", true);
        this.config.addDefault("deluxe_chat", true);
        this.config.addDefault("format_chat.enabled", true);
        this.config.addDefault("format_chat.format", "{deluxetags_tag} <%1$s> %2$s");
        if (this.config.contains("force_tag_on_join")) {
            this.config.set("force_tag_on_join", (Object)null);
        }

        this.config.addDefault("load_tag_on_join", true);
        this.config.addDefault("main.gui.name", "&6Available tags&f: &6%deluxetags_amount%");
        this.config.addDefault("main.gui.tag_select_item.material", "NAME_TAG");
        this.config.addDefault("main.gui.tag_select_item.data", 0);
        this.config.addDefault("main.gui.tag_select_item.displayname", "&6Tag&f: &6%deluxetags_identifier%");
        this.config.addDefault("main.gui.tag_select_item.lore", Arrays.asList("%deluxetags_tag%", "%deluxetags_description%"));
        this.config.addDefault("main.gui.divider_item.material", "STAINED_GLASS_PANE");
        this.config.addDefault("main.gui.divider_item.data", 15);
        this.config.addDefault("main.gui.divider_item.displayname", "");
        this.config.addDefault("main.gui.divider_item.lore", Collections.emptyList());
        this.config.addDefault("main.gui.has_tag_item.material", "SKULL_ITEM");
        this.config.addDefault("main.gui.has_tag_item.data", 3);
        this.config.addDefault("main.gui.has_tag_item.displayname", "&eCurrent tag&f: &6%deluxetags_identifier%");
        this.config.addDefault("main.gui.has_tag_item.lore", Arrays.asList("%deluxetags_tag%", "Click to remove your current tag"));
        this.config.addDefault("main.gui.no_tag_item.material", "SKULL_ITEM");
        this.config.addDefault("main.gui.no_tag_item.data", 3);
        this.config.addDefault("main.gui.no_tag_item.displayname", "&cYou don't have a tag set!");
        this.config.addDefault("main.gui.no_tag_item.lore", Collections.singletonList("&7Click a tag above to select one!"));
        this.config.addDefault("main.gui.exit_item.material", "IRON_DOOR");
        this.config.addDefault("main.gui.exit_item.data", 0);
        this.config.addDefault("main.gui.exit_item.displayname", "&cClick to exit");
        this.config.addDefault("main.gui.exit_item.lore", Collections.singletonList("&7Exit the tags menu"));
        if (!this.config.contains("deluxetags")) {
            this.config.set("deluxetags.example.order", 1);
            this.config.set("deluxetags.example.tag", "&8[&bDeluxeTags&8]");
            this.config.set("deluxetags.example.description", "&cAwarded for using DeluxeTags!");
            this.config.set("deluxetags.example.permission", "deluxetags.tag.example");
        }

        this.config.options().copyDefaults(true);
        this.plugin.saveConfig();
        this.plugin.reloadConfig();
    }

    public boolean formatChat() {
        return this.config.getBoolean("format_chat.enabled");
    }

    public String chatFormat() {
        String format = this.config.getString("format_chat.format");
        return format != null ? format : "{deluxetags_tag} <%1$s> %2$s";
    }

    public boolean checkUpdates() {
        return this.config.getBoolean("check_updates");
    }

    public boolean deluxeChat() {
        return this.config.getBoolean("deluxe_chat");
    }

    public boolean loadTagOnJoin() {
        return this.config.getBoolean("load_tag_on_join");
    }

    public boolean forceTags() {
        return this.config.getBoolean("force_tags");
    }

    public int loadTags() {
        FileConfiguration c = this.plugin.getConfig();
        int loaded = 0;
        if (!c.contains("deluxetags")) {
            return loaded;
        } else {
            Set<String> keys = c.getConfigurationSection("deluxetags").getKeys(false);
            if (keys != null && !keys.isEmpty()) {
                Iterator<String> it = keys.iterator();
                int count = 1;

                boolean save;
                for(save = false; it.hasNext(); ++loaded) {
                    String identifier = (String)it.next();
                    String tag = c.getString("deluxetags." + identifier + ".tag");
                    String desc;
                    if (c.isList("deluxetags." + identifier + ".description")) {
                        desc = String.join("\n", c.getStringList("deluxetags." + identifier + ".description"));
                    } else {
                        desc = c.getString("deluxetags." + identifier + ".description", "&f");
                    }

                    int priority = count++;
                    if (!c.contains("deluxetags." + identifier + ".order")) {
                        c.set("deluxetags." + identifier + ".order", priority);
                        save = true;
                    } else {
                        priority = c.getInt("deluxetags." + identifier + ".order");
                    }

                    DeluxeTag t = new DeluxeTag(priority, identifier, tag, desc);
                    t.setPermission(c.getString("deluxetags." + identifier + ".permission", "deluxetags.tag." + identifier));
                    t.load();
                }

                if (save) {
                    this.plugin.saveConfig();
                    this.plugin.reloadConfig();
                }
            }

            return loaded;
        }
    }

    public void saveTag(DeluxeTag tag) {
        this.saveTag(tag.getPriority(), tag.getIdentifier(), tag.getDisplayTag(), tag.getDescription(), tag.getPermission());
    }

    public void saveTag(int priority, String identifier, String tag, String description, String permission) {
        FileConfiguration c = this.plugin.getConfig();
        c.set("deluxetags." + identifier + ".order", priority);
        c.set("deluxetags." + identifier + ".tag", tag);
        if (description == null) {
            description = "&fDescription for tag " + identifier;
        }

        c.set("deluxetags." + identifier + ".description", description);
        c.set("deluxetags." + identifier + ".permission", permission);
        this.plugin.saveConfig();
    }

    public void removeTag(String identifier) {
        FileConfiguration c = this.plugin.getConfig();
        c.set("deluxetags." + identifier, (Object)null);
        this.plugin.saveConfig();
    }
}
