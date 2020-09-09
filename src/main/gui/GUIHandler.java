package main.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import main.DeluxeTag;
import main.DeluxeTags;
import main.Lang;
import main.utils.MsgUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIHandler implements Listener {
    private DeluxeTags plugin;

    public GUIHandler(DeluxeTags i) {
        this.plugin = i;
    }

    private void sms(Player p, String msg) {
        p.sendMessage(MsgUtils.color(msg));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        if (TagGUI.hasGUI(p)) {
            TagGUI gui = TagGUI.getGUI(p);
            e.setCancelled(true);
            ItemStack clicked = e.getCurrentItem();
            if (clicked != null && !clicked.getType().equals(Material.AIR)) {
                int slot = e.getRawSlot();
                if (slot < 36) {
                    if (clicked.getType() != null && !clicked.getType().equals(Material.AIR)) {
                        Map tags;
                        try {
                            tags = gui.getTags();
                        } catch (NullPointerException var10) {
                            TagGUI.close(p);
                            p.closeInventory();
                            return;
                        }

                        if (tags.isEmpty()) {
                            TagGUI.close(p);
                            p.closeInventory();
                            return;
                        }

                        String id = (String)tags.get(slot);
                        if (id == null || id.isEmpty()) {
                            TagGUI.close(p);
                            p.closeInventory();
                            return;
                        }

                        if (DeluxeTag.getLoadedTag(id) != null && DeluxeTag.getLoadedTag(id).setPlayerTag(p)) {
                            TagGUI.close(p);
                            p.closeInventory();
                            this.sms(p, Lang.GUI_TAG_SELECTED.getConfigValue(new String[]{id, DeluxeTag.getPlayerDisplayTag(p)}));
                            this.plugin.saveTagIdentifier(p.getUniqueId().toString(), id);
                        }
                    }
                } else if (slot != 48 && slot != 50) {
                    if (slot == 49) {
                        if (!DeluxeTag.getPlayerDisplayTag(p).isEmpty()) {
                            TagGUI.close(p);
                            p.closeInventory();
                            this.plugin.getDummy().setPlayerTag(p);
                            this.plugin.removeSavedTag(p.getUniqueId().toString());
                            this.sms(p, Lang.GUI_TAG_DISABLED.getConfigValue((String[])null));
                        }

                        p.updateInventory();
                    } else if ((slot == 45 || slot == 53) && clicked.hasItemMeta() && clicked.getItemMeta().hasDisplayName()) {
                        String name;
                        if (slot == 45) {
                            name = clicked.getItemMeta().getDisplayName().replace("Back to page ", "");
                        } else {
                            name = clicked.getItemMeta().getDisplayName().replace("Forward to page ", "");
                        }

                        int page;
                        try {
                            page = Integer.parseInt(name);
                        } catch (Exception var9) {
                            TagGUI.close(p);
                            p.closeInventory();
                            this.sms(p, Lang.GUI_PAGE_ERROR.getConfigValue((String[])null));
                            return;
                        }

                        this.openMenu(p, page);
                    }
                } else {
                    TagGUI.close(p);
                    p.closeInventory();
                }

            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player) {
            Player p = (Player)e.getPlayer();
            if (TagGUI.hasGUI(p)) {
                TagGUI.close(p);
            }
        }

    }

    public boolean openMenu(Player p, int page) {
        List<String> ids = DeluxeTag.getAvailableTagIdentifiers(p);
        if (ids != null && !ids.isEmpty()) {
            GUIOptions options = this.plugin.getGuiOptions();
            String title = options.getMenuName();
            title = DeluxeTags.setPlaceholders(p, title, (DeluxeTag)null);
            if (title.length() > 32) {
                title = title.substring(0, 31);
            }

            TagGUI gui = (new TagGUI(title, page)).setSlots(54);
            int pages = 1;
            if (ids.size() > 36) {
                pages = ids.size() / 36;
                if (ids.size() % 36 > 0) {
                    ++pages;
                }
            }

            int count;
            if (page > 1 && page <= pages) {
                count = 36 * page - 36;
                ids = ids.subList(count, ids.size());
            }

            count = 0;
            Map<Integer, String> tags = new HashMap();

            for(Iterator var10 = ids.iterator(); var10.hasNext(); ++count) {
                String id = (String)var10.next();
                if (count >= 36) {
                    break;
                }

                tags.put(count, id);
                DeluxeTag tag = DeluxeTag.getLoadedTag(id);
                if (tag == null) {
                    tag = new DeluxeTag(1, "", "", "");
                }

                String display = options.getTagSelectItem().getName();
                display = DeluxeTags.setPlaceholders(p, display, tag);
                List<String> tmp = null;
                List<String> orig = options.getTagSelectItem().getLore();
                if (orig != null && !orig.isEmpty()) {
                    tmp = new ArrayList();
                    Iterator var16 = orig.iterator();

                    while(var16.hasNext()) {
                        String line = (String)var16.next();
                        line = DeluxeTags.setPlaceholders(p, line, tag);
                        if (line.contains("\n")) {
                            tmp.addAll(Arrays.asList(line.split("\n")));
                        } else {
                            tmp.add(line);
                        }
                    }
                }

                gui.setItem(count, TagGUI.createItem(options.getTagSelectItem().getMaterial(), options.getTagSelectItem().getData(), 1, display, tmp));
            }

            gui.setTags(tags);
            String display = options.getDividerItem().getName();
            display = MsgUtils.color(DeluxeTags.setPlaceholders(p, display, (DeluxeTag)null));
            List<String> tmp = null;
            List<String> orig = options.getDividerItem().getLore();
            String line;
            if (orig != null && !orig.isEmpty()) {
                tmp = new ArrayList();

                for(Iterator var26 = orig.iterator(); var26.hasNext(); tmp.add(line)) {
                    line = (String)var26.next();
                    line = DeluxeTags.setPlaceholders(p, line, (DeluxeTag)null);
                    if (line.contains("\n")) {
                        tmp.addAll(Arrays.asList(line.split("\n")));
                    } else {
                        tmp.add(line);
                    }
                }
            }

            ItemStack divider = TagGUI.createItem(options.getDividerItem().getMaterial(), options.getDividerItem().getData(), 1, display, tmp);

            for(int b = 36; b < 45; ++b) {
                gui.setItem(b, divider);
            }

            line = DeluxeTag.getPlayerTagIdentifier(p);
            DisplayItem item;
            if (line != null && !line.isEmpty()) {
                item = options.getHasTagItem();
            } else {
                item = options.getNoTagItem();
            }

            ItemStack info = new ItemStack(item.getMaterial(), 1, item.getData());
            ItemMeta meta = info.getItemMeta();
            meta.setDisplayName(DeluxeTags.setPlaceholders(p, item.getName(), (DeluxeTag)null));
            List exitLore;
            if (item.getLore() != null) {
                exitLore = item.getLore();
                if (exitLore != null && !exitLore.isEmpty()) {
                    List<String> infoTmp = new ArrayList();
                    Iterator var20 = exitLore.iterator();

                    while(var20.hasNext()) {
                        String line2 = (String)var20.next();
                        line2 = DeluxeTags.setPlaceholders(p, line2, (DeluxeTag)null);
                        if (line2.contains("\n")) {
                            infoTmp.addAll(Arrays.asList(line2.split("\n")));
                        } else {
                            infoTmp.add(line2);
                        }
                    }

                    meta.setLore(infoTmp);
                }
            }

            info.setItemMeta(meta);
            gui.setItem(49, info);
            String exitDisplay = DeluxeTags.setPlaceholders(p, options.getExitItem().getName(), (DeluxeTag)null);
            exitLore = options.getExitItem().getLore();
            List<String> exitTmp = null;
            if (exitLore != null && !exitLore.isEmpty()) {
                exitTmp = new ArrayList();
                Iterator var35 = exitLore.iterator();

                while(var35.hasNext()) {
                    String line3 = (String)var35.next();
                    line3 = DeluxeTags.setPlaceholders(p, line3, (DeluxeTag)null);
                    exitTmp.add(line3);
                }
            }

            ItemStack exit = TagGUI.createItem(options.getExitItem().getMaterial(), options.getExitItem().getData(), 1, exitDisplay, exitTmp);
            gui.setItem(48, exit);
            gui.setItem(50, exit);
            if (page > 1) {
                gui.setItem(45, TagGUI.createItem(Material.PAPER, (short)0, 1, "Back to page " + (page - 1), (List)null));
            }

            if (page < pages) {
                gui.setItem(53, TagGUI.createItem(Material.PAPER, (short)0, 1, "Forward to page " + (page + 1), (List)null));
            }

            gui.openInventory(p);
            return true;
        } else {
            return false;
        }
    }
}
