package main.gui;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import main.DeluxeTags;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class GUIOptions {
    final List<String> ITEM_TYPES = Arrays.asList("tag_select_item", "divider_item", "has_tag_item", "no_tag_item", "exit_item");
    private String menuName;
    private DisplayItem tagSelectItem;
    private DisplayItem dividerItem;
    private DisplayItem hasTagItem;
    private DisplayItem noTagItem;
    private DisplayItem exitItem;

    public GUIOptions(DeluxeTags i) {
        FileConfiguration c = i.getConfig();
        this.menuName = c.getString("main.gui.name");
        if (this.menuName == null) {
            this.menuName = "&6Available tags&f: &6%deluxetags_amount%";
        }

        Material mat = null;
        short data = Short.parseShort(null);
        String display = null;
        List<String> lore = null;

        for(Iterator var7 = this.ITEM_TYPES.iterator(); var7.hasNext(); lore = null) {
            String type = (String)var7.next();

            try {
                mat = Material.getMaterial(c.getString("main.gui." + type + ".material").toUpperCase());
            } catch (Exception var13) {
                byte var11 = -1;
                switch(type.hashCode()) {
                    case -1369866860:
                        if (type.equals("exit_item")) {
                            var11 = 4;
                        }
                        break;
                    case -1210242951:
                        if (type.equals("divider_item")) {
                            var11 = 1;
                        }
                        break;
                    case -1107938986:
                        if (type.equals("no_tag_item")) {
                            var11 = 3;
                        }
                        break;
                    case 531239069:
                        if (type.equals("has_tag_item")) {
                            var11 = 2;
                        }
                        break;
                    case 720126801:
                        if (type.equals("tag_select_item")) {
                            var11 = 0;
                        }
                }

                switch(var11) {
                    case 0:
                        mat = Material.NAME_TAG;
                        break;
                    case 1:
                        mat = Material.WHITE_STAINED_GLASS_PANE;
                        break;
                    case 2:
                    case 3:
                        mat = Material.BARRIER;
                        break;
                    case 4:
                        mat = Material.IRON_DOOR;
                }
            }

            try {
                data = Short.parseShort(c.getString("main.gui." + type + ".data"));
            } catch (Exception var12) {
                data = 0;
            }

            display = c.getString("main.gui." + type + ".displayname");
            lore = c.getStringList("main.gui." + type + ".lore");
            byte var10 = -1;
            switch(type.hashCode()) {
                case -1369866860:
                    if (type.equals("exit_item")) {
                        var10 = 4;
                    }
                    break;
                case -1210242951:
                    if (type.equals("divider_item")) {
                        var10 = 1;
                    }
                    break;
                case -1107938986:
                    if (type.equals("no_tag_item")) {
                        var10 = 3;
                    }
                    break;
                case 531239069:
                    if (type.equals("has_tag_item")) {
                        var10 = 2;
                    }
                    break;
                case 720126801:
                    if (type.equals("tag_select_item")) {
                        var10 = 0;
                    }
            }

            switch(var10) {
                case 0:
                    this.tagSelectItem = new DisplayItem(mat, data, display, lore);
                    break;
                case 1:
                    this.dividerItem = new DisplayItem(mat, data, display, lore);
                    break;
                case 2:
                    this.hasTagItem = new DisplayItem(mat, data, display, lore);
                    break;
                case 3:
                    this.noTagItem = new DisplayItem(mat, data, display, lore);
                    break;
                case 4:
                    this.exitItem = new DisplayItem(mat, data, display, lore);
            }

            mat = null;
            data = Short.parseShort(null);
            display = null;
        }

    }

    public DisplayItem getTagSelectItem() {
        return this.tagSelectItem;
    }

    public DisplayItem getDividerItem() {
        return this.dividerItem;
    }

    public DisplayItem getHasTagItem() {
        return this.hasTagItem;
    }

    public DisplayItem getNoTagItem() {
        return this.noTagItem;
    }

    public DisplayItem getExitItem() {
        return this.exitItem;
    }

    public String getMenuName() {
        return this.menuName;
    }
}
