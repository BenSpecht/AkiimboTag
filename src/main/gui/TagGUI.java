package main.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import main.utils.MsgUtils;

public class TagGUI {
    private static HashMap<String, TagGUI> inGUI;
    private Inventory inventory;
    private String displayName;
    private int slots;
    private int page;
    private Map<Integer, String> tags;
    private Map<Integer, ItemStack> items;

    public TagGUI(String displayName, int page) {
        this.displayName = displayName;
        this.items = new HashMap();
        this.page = page;
        this.tags = new HashMap();
    }

    public int getInventorySize() {
        return this.slots;
    }

    public String getInventoryName() {
        return this.displayName;
    }

    public TagGUI clear() {
        this.items.clear();
        this.tags = null;
        return this;
    }

    public boolean contains(ItemStack item) {
        return this.items.containsValue(item);
    }

    public TagGUI setItem(int slot, ItemStack item) {
        this.items.put(slot, item);
        return this;
    }

    public TagGUI setSlots(int slots) {
        this.slots = slots;
        return this;
    }

    public void openInventory(Player player) {
        this.inventory = Bukkit.createInventory((InventoryHolder)null, this.slots, MsgUtils.color(this.displayName));
        Iterator var2 = this.items.keySet().iterator();

        while(var2.hasNext()) {
            Integer slot = (Integer)var2.next();
            this.inventory.setItem(slot, (ItemStack)this.items.get(slot));
        }

        player.openInventory(this.inventory);
        if (inGUI == null) {
            inGUI = new HashMap();
        }

        inGUI.put(player.getName(), this);
    }

    public static ItemStack createItem(Material mat, short data, int amount, String displayName, List<String> lore) {
        if (mat == null) {
            return null;
        } else {
            ItemStack i = new ItemStack(mat, amount);
            if (data > 0) {
                i.setDurability(data);
            }

            ItemMeta im = i.getItemMeta();
            if (displayName != null) {
                im.setDisplayName(MsgUtils.color(displayName));
            }

            if (lore != null && !lore.isEmpty()) {
                List<String> temp = new ArrayList();
                Iterator var8 = lore.iterator();

                while(var8.hasNext()) {
                    String line = (String)var8.next();
                    temp.add(MsgUtils.color(line));
                }

                im.setLore(temp);
            }

            i.setItemMeta(im);
            return i;
        }
    }

    public static boolean hasGUI(Player p) {
        if (inGUI == null) {
            return false;
        } else {
            return inGUI.containsKey(p.getName()) && inGUI.get(p.getName()) != null;
        }
    }

    public static TagGUI getGUI(Player p) {
        return !hasGUI(p) ? null : (TagGUI)inGUI.get(p.getName());
    }

    public static boolean close(Player p) {
        if (!hasGUI(p)) {
            return false;
        } else {
            getGUI(p).clear();
            inGUI.remove(p.getName());
            return true;
        }
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public static void unload() {
        inGUI = null;
    }

    public Map<Integer, String> getTags() {
        return this.tags;
    }

    public void setTags(Map<Integer, String> tags) {
        this.tags = tags;
    }
}
