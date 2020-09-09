package main.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import main.DeluxeTag;
import main.DeluxeTags;
import main.Lang;
import main.utils.MsgUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagCommand implements CommandExecutor {
    private DeluxeTags plugin;

    public TagCommand(DeluxeTags i) {
        this.plugin = i;
    }

    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        Player target;
        if (args.length == 0) {
            if (!(s instanceof Player)) {
                MsgUtils.msg(s, "&8&m+----------------+");
                MsgUtils.msg(s, "&5&lDeluxeTags &f&o" + this.plugin.getDescription().getVersion());
                MsgUtils.msg(s, "&7Created by &f&oextended_clip");
                MsgUtils.msg(s, "Use /tags help for console main.commands");
                MsgUtils.msg(s, "&8&m+----------------+");
                return true;
            } else if (!s.hasPermission("deluxetags.main.gui")) {
                MsgUtils.msg(s, Lang.CMD_NO_PERMS.getConfigValue(new String[]{"deluxetags.main.gui"}));
                return true;
            } else if (DeluxeTag.getLoadedTags() != null && !DeluxeTag.getLoadedTags().isEmpty()) {
                target = (Player)s;
                if (!this.plugin.getGUIHandler().openMenu(target, 1)) {
                    MsgUtils.msg(s, Lang.CMD_NO_TAGS_AVAILABLE.getConfigValue((String[])null));
                }

                return true;
            } else {
                MsgUtils.msg(s, Lang.CMD_NO_TAGS_LOADED.getConfigValue((String[])null));
                return true;
            }
        } else {
            String id;
            if (args[0].equalsIgnoreCase("help")) {
                id = Lang.CMD_HELP_COLOR.getConfigValue((String[])null);
                MsgUtils.msg(s, "&8&m+----------------+");
                MsgUtils.msg(s, Lang.CMD_HELP_TITLE.getConfigValue((String[])null));
                MsgUtils.msg(s, " ");
                if (s.hasPermission("deluxetags.main.gui")) {
                    MsgUtils.msg(s, id + "/tags");
                    MsgUtils.msg(s, Lang.CMD_HELP_TAGS.getConfigValue((String[])null));
                }

                if (s.hasPermission("deluxetags.list")) {
                    MsgUtils.msg(s, id + "/tags list (all/<playername>)");
                    MsgUtils.msg(s, Lang.CMD_HELP_LIST.getConfigValue((String[])null));
                }

                if (s.hasPermission("deluxetags.select")) {
                    MsgUtils.msg(s, id + "/tags select <tag>");
                    MsgUtils.msg(s, Lang.CMD_HELP_SELECT.getConfigValue((String[])null));
                }

                if (s.hasPermission("deluxetags.set")) {
                    MsgUtils.msg(s, id + "/tags set <player> <tag>");
                    MsgUtils.msg(s, Lang.CMD_HELP_ADMIN_SET.getConfigValue((String[])null));
                }

                if (s.hasPermission("deluxetags.clear")) {
                    MsgUtils.msg(s, id + "/tags clear <player>");
                    MsgUtils.msg(s, Lang.CMD_HELP_ADMIN_CLEAR.getConfigValue((String[])null));
                }

                if (s.hasPermission("deluxetags.create")) {
                    MsgUtils.msg(s, id + "/tags create <identifier> <tag>");
                    MsgUtils.msg(s, Lang.CMD_HELP_ADMIN_CREATE.getConfigValue((String[])null));
                }

                if (s.hasPermission("deluxetags.delete")) {
                    MsgUtils.msg(s, id + "/tags delete <identifier>");
                    MsgUtils.msg(s, Lang.CMD_HELP_ADMIN_DELETE.getConfigValue((String[])null));
                }

                if (s.hasPermission("deluxetags.setdescription")) {
                    MsgUtils.msg(s, id + "/tags setdesc <identifier> <tag description>");
                    MsgUtils.msg(s, Lang.CMD_HELP_ADMIN_SET_DESC.getConfigValue((String[])null));
                }

                if (s.hasPermission("deluxetags.reload")) {
                    MsgUtils.msg(s, id + "/tags reload");
                    MsgUtils.msg(s, Lang.CMD_HELP_RELOAD.getConfigValue((String[])null));
                }

                MsgUtils.msg(s, id + "/tags version");
                MsgUtils.msg(s, Lang.CMD_HELP_VERSION.getConfigValue((String[])null));
                MsgUtils.msg(s, "&8&m+----------------+");
                return true;
            } else if (args[0].equalsIgnoreCase("version")) {
                MsgUtils.msg(s, "&8&m+----------------+");
                MsgUtils.msg(s, "&5&lDeluxeTags &f&o" + this.plugin.getDescription().getVersion());
                MsgUtils.msg(s, "&7Created by &f&oextended_clip");
                MsgUtils.msg(s, "&8&m+----------------+");
                return true;
            } else {
                String tag;
                String identifier;
                String t;
                List r;
                DeluxeTag dTag;
                if (args[0].equalsIgnoreCase("list")) {
                    if (args.length == 1) {
                        if (!s.hasPermission("deluxetags.list")) {
                            MsgUtils.msg(s, Lang.CMD_NO_PERMS.getConfigValue(new String[]{"deluxetags.list"}));
                            return true;
                        }

                        if (DeluxeTag.getLoadedTags() == null || DeluxeTag.getLoadedTags().isEmpty()) {
                            MsgUtils.msg(s, Lang.CMD_NO_TAGS_LOADED.getConfigValue((String[])null));
                            return true;
                        }

                        List listT;
                        if (!(s instanceof Player)) {
                            listT = DeluxeTag.getAllTagIdentifiers();
                        } else {
                            listT = DeluxeTag.getAvailableTagIdentifiers((Player)s);
                        }

                        if (listT != null) {
                            tag = listT.toString().replace("[", "&7").replace(",", "&a,&7").replace("]", "");
                        }
                        tag = String.valueOf(listT.size());
                        MsgUtils.msg(s, Lang.CMD_TAG_LIST.getConfigValue(new String[]{tag, tag}));
                    } else {
                        if (args[1].equalsIgnoreCase("all")) {
                            if (!s.hasPermission("deluxetags.list.all")) {
                                MsgUtils.msg(s, Lang.CMD_NO_PERMS.getConfigValue(new String[]{"deluxetags.list.all"}));
                                return true;
                            }

                            if (DeluxeTag.getLoadedTags() != null && !DeluxeTag.getLoadedTags().isEmpty()) {
                                Collection<DeluxeTag> tagCollection = DeluxeTag.getLoadedTags();
                                StringBuilder sb = new StringBuilder();
                                Iterator var25 = tagCollection.iterator();

                                while(var25.hasNext()) {
                                    dTag = (DeluxeTag)var25.next();
                                    sb.append("&f").append(dTag.getIdentifier()).append("&7:&f").append(dTag.getDisplayTag()).append("&a, ");
                                }

                                tag = sb.toString().trim();
                                identifier = String.valueOf(tagCollection.size());
                                MsgUtils.msg(s, Lang.CMD_TAG_LIST_ALL.getConfigValue(new String[]{identifier, tag}));
                                return true;
                            }

                            MsgUtils.msg(s, Lang.CMD_NO_TAGS_LOADED.getConfigValue((String[])null));
                            return true;
                        }

                        if (!s.hasPermission("deluxetags.list.player")) {
                            MsgUtils.msg(s, Lang.CMD_NO_PERMS.getConfigValue(new String[]{"deluxetags.list.player"}));
                            return true;
                        }

                        if (DeluxeTag.getLoadedTags() == null || DeluxeTag.getLoadedTags().isEmpty()) {
                            MsgUtils.msg(s, Lang.CMD_NO_TAGS_LOADED.getConfigValue((String[])null));
                            return true;
                        }

                        id = args[1];
                        Player targetPlayer = Bukkit.getPlayer(id);
                        if (targetPlayer == null) {
                            MsgUtils.msg(s, Lang.CMD_TARGET_NOT_ONLINE.getConfigValue(new String[]{id}));
                            return true;
                        }

                        r = DeluxeTag.getAvailableTagIdentifiers(targetPlayer);
                        identifier = r.toString().replace("[", "&7").replace(",", "&a,&7").replace("]", "");
                        t = String.valueOf(r.size());
                        MsgUtils.msg(s, Lang.CMD_TAG_LIST_TARGET.getConfigValue(new String[]{targetPlayer.getName(), t, identifier}));
                    }

                    return true;
                } else {
                    DeluxeTag set;
                    List avail;
                    Iterator var18;
                    if (args[0].equalsIgnoreCase("select")) {
                        if (!(s instanceof Player)) {
                            MsgUtils.msg(s, "&4This command can only be used in game!");
                            return true;
                        } else {
                            target = (Player)s;
                            if (!s.hasPermission("deluxetags.select")) {
                                MsgUtils.msg(s, Lang.CMD_NO_PERMS.getConfigValue(new String[]{"deluxetags.select"}));
                                return true;
                            } else if (args.length != 2) {
                                MsgUtils.msg(s, Lang.CMD_TAG_SEL_INCORRECT.getConfigValue((String[])null));
                                return true;
                            } else if (DeluxeTag.getLoadedTags() != null && !DeluxeTag.getLoadedTags().isEmpty()) {
                                avail = DeluxeTag.getAvailableTagIdentifiers(target);
                                if (avail != null && !avail.isEmpty()) {
                                    tag = args[1];
                                    var18 = avail.iterator();

                                    while(var18.hasNext()) {
                                        t = (String)var18.next();
                                        if (t.equalsIgnoreCase(tag)) {
                                            set = DeluxeTag.getLoadedTag(t);
                                            if (set != null) {
                                                if (set.setPlayerTag(target)) {
                                                    this.plugin.saveTagIdentifier(target.getUniqueId().toString(), set.getIdentifier());
                                                    MsgUtils.msg(s, Lang.CMD_TAG_SEL_SUCCESS.getConfigValue(new String[]{set.getIdentifier(), set.getDisplayTag()}));
                                                } else {
                                                    MsgUtils.msg(s, Lang.CMD_TAG_SEL_FAIL_SAMETAG.getConfigValue(new String[]{set.getIdentifier(), set.getDisplayTag()}));
                                                }

                                                return true;
                                            }
                                        }
                                    }

                                    MsgUtils.msg(s, Lang.CMD_TAG_SEL_FAIL_INVALID.getConfigValue(new String[]{tag}));
                                    return true;
                                } else {
                                    MsgUtils.msg(s, Lang.CMD_NO_TAGS_AVAILABLE.getConfigValue((String[])null));
                                    return true;
                                }
                            } else {
                                MsgUtils.msg(s, Lang.CMD_NO_TAGS_LOADED.getConfigValue((String[])null));
                                return true;
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("create")) {
                        if (!s.hasPermission("deluxetags.create")) {
                            MsgUtils.msg(s, Lang.CMD_NO_PERMS.getConfigValue(new String[]{"deluxetags.create"}));
                            return true;
                        } else if (args.length < 3) {
                            MsgUtils.msg(s, Lang.CMD_ADMIN_CREATE_TAG_INCORRECT.getConfigValue((String[])null));
                            return true;
                        } else {
                            id = args[1];
                            if (DeluxeTag.getLoadedTag(id) != null) {
                                MsgUtils.msg(s, Lang.CMD_ADMIN_CREATE_TAG_FAIL.getConfigValue(new String[]{id}));
                                return true;
                            } else {
                                tag = StringUtils.join(Arrays.copyOfRange(args, 2, args.length), " ");
                                if (!tag.isEmpty()) {
                                    if (tag.endsWith("_")) {
                                        tag = tag.substring(0, tag.length() - 1) + " ";
                                    }

                                    int priority = DeluxeTag.getLoadedTagsAmount() + 1;
                                    dTag = new DeluxeTag(priority, id, tag, "");
                                    dTag.load();
                                    this.plugin.getCfg().saveTag(priority, id, tag, "&f", "deluxetags.tag." + id);
                                    MsgUtils.msg(s, Lang.CMD_ADMIN_CREATE_TAG_SUCCESS.getConfigValue(new String[]{id, tag}));
                                }

                                return true;
                            }
                        }
                    } else {
//                        DeluxeTag tag;
                        if (args[0].equalsIgnoreCase("delete")) {
                            if (!s.hasPermission("deluxetags.delete")) {
                                MsgUtils.msg(s, Lang.CMD_NO_PERMS.getConfigValue(new String[]{"deluxetags.delete"}));
                                return true;
                            } else if (args.length != 2) {
                                MsgUtils.msg(s, Lang.CMD_ADMIN_DELETE_TAG_INCORRECT.getConfigValue((String[])null));
                                return true;
                            } else {
                                id = args[1];
                                dTag = DeluxeTag.getLoadedTag(id);
                                if (dTag != null) {
                                    r = dTag.removeActivePlayers();
                                    if (r != null && !r.isEmpty()) {
                                        this.plugin.removeSavedTags(r);
                                    }

                                    if (dTag.unload()) {
                                        this.plugin.getCfg().removeTag(id);
                                        tag = null;
                                        MsgUtils.msg(s, Lang.CMD_ADMIN_DELETE_TAG_SUCCESS.getConfigValue(new String[]{id}));
                                        return true;
                                    }
                                }

                                MsgUtils.msg(s, Lang.CMD_ADMIN_DELETE_TAG_FAIL.getConfigValue(new String[]{id}));
                                return true;
                            }
                        } else if (!args[0].equalsIgnoreCase("setdesc") && !args[0].equalsIgnoreCase("setdescription")) {
                            if (args[0].equalsIgnoreCase("set")) {
                                if (!s.hasPermission("deluxetags.set")) {
                                    MsgUtils.msg(s, Lang.CMD_NO_PERMS.getConfigValue(new String[]{"deluxetags.set"}));
                                    return true;
                                } else if (args.length != 3) {
                                    MsgUtils.msg(s, Lang.CMD_ADMIN_SET_INCORRECT_ARGS.getConfigValue((String[])null));
                                    return true;
                                } else if (DeluxeTag.getLoadedTags() != null && !DeluxeTag.getLoadedTags().isEmpty()) {
                                    target = Bukkit.getPlayer(args[1]);
                                    if (target == null) {
                                        MsgUtils.msg(s, Lang.CMD_TARGET_NOT_ONLINE.getConfigValue(new String[]{args[1]}));
                                        return true;
                                    } else {
                                        avail = DeluxeTag.getAvailableTagIdentifiers(target);
                                        if (avail != null && !avail.isEmpty()) {
                                            tag = args[2];
                                            var18 = avail.iterator();

                                            while(var18.hasNext()) {
                                                t = (String)var18.next();
                                                if (t.equalsIgnoreCase(tag)) {
                                                    set = DeluxeTag.getLoadedTag(t);
                                                    if (set != null) {
                                                        set.setPlayerTag(target);
                                                        this.plugin.saveTagIdentifier(target.getUniqueId().toString(), set.getIdentifier());
                                                        MsgUtils.msg(s, Lang.CMD_ADMIN_SET_SUCCESS.getConfigValue(new String[]{target.getName(), set.getIdentifier(), set.getDisplayTag()}));
                                                        if (target != s) {
                                                            MsgUtils.msg(target, Lang.CMD_ADMIN_SET_SUCCESS_TARGET.getConfigValue(new String[]{set.getIdentifier(), set.getDisplayTag(), s.getName()}));
                                                        }

                                                        return true;
                                                    }
                                                }
                                            }

                                            MsgUtils.msg(s, Lang.CMD_ADMIN_SET_FAIL.getConfigValue(new String[]{tag, target.getName()}));
                                            return true;
                                        } else {
                                            MsgUtils.msg(s, Lang.CMD_ADMIN_SET_NO_TAGS.getConfigValue(new String[]{target.getName()}));
                                            return true;
                                        }
                                    }
                                } else {
                                    MsgUtils.msg(s, Lang.CMD_NO_TAGS_LOADED.getConfigValue((String[])null));
                                    return true;
                                }
                            } else if (args[0].equalsIgnoreCase("clear")) {
                                if (!s.hasPermission("deluxetags.clear")) {
                                    MsgUtils.msg(s, Lang.CMD_NO_PERMS.getConfigValue(new String[]{"deluxetags.clear"}));
                                    return true;
                                } else if (args.length != 2) {
                                    MsgUtils.msg(s, Lang.CMD_ADMIN_CLEAR_INCORRECT_ARGS.getConfigValue((String[])null));
                                    return true;
                                } else if (DeluxeTag.getLoadedTags() != null && !DeluxeTag.getLoadedTags().isEmpty()) {
                                    target = Bukkit.getPlayer(args[1]);
                                    if (target == null) {
                                        MsgUtils.msg(s, Lang.CMD_TARGET_NOT_ONLINE.getConfigValue(new String[]{args[1]}));
                                        return true;
                                    } else {
                                        tag = DeluxeTag.getPlayerTagIdentifier(target);
                                        if (tag != null && !DeluxeTag.getPlayerDisplayTag(target).isEmpty()) {
                                            this.plugin.getDummy().setPlayerTag(target);
                                            this.plugin.removeSavedTag(target.getUniqueId().toString());
                                            MsgUtils.msg(s, Lang.CMD_ADMIN_CLEAR_SUCCESS.getConfigValue(new String[]{target.getName()}));
                                            if (target != s) {
                                                MsgUtils.msg(target, Lang.CMD_ADMIN_CLEAR_SUCCESS_TARGET.getConfigValue(new String[]{s.getName()}));
                                            }

                                            return true;
                                        } else {
                                            MsgUtils.msg(s, Lang.CMD_ADMIN_CLEAR_NO_TAG_SET.getConfigValue(new String[]{target.getName()}));
                                            return true;
                                        }
                                    }
                                } else {
                                    MsgUtils.msg(s, Lang.CMD_NO_TAGS_LOADED.getConfigValue((String[])null));
                                    return true;
                                }
                            } else if (!args[0].equalsIgnoreCase("reload")) {
                                MsgUtils.msg(s, Lang.CMD_INCORRECT_USAGE.getConfigValue((String[])null));
                                return true;
                            } else if (!s.hasPermission("deluxetags.reload")) {
                                MsgUtils.msg(s, Lang.CMD_NO_PERMS.getConfigValue(new String[]{"deluxetags.reload"}));
                                return true;
                            } else {
                                this.plugin.reloadConfig();
                                this.plugin.saveConfig();
                                DeluxeTag.unloadData();
                                int loaded = this.plugin.getCfg().loadTags();
                                DeluxeTags.setForceTags(this.plugin.getCfg().forceTags());
                                this.plugin.getPlayerFile().reloadConfig();
                                this.plugin.getPlayerFile().saveConfig();
                                this.plugin.getLangFile().reloadConfig();
                                this.plugin.getLangFile().saveConfig();
                                this.plugin.loadMessages();
                                this.plugin.reloadGUIOptions();
                                Iterator var13 = Bukkit.getServer().getOnlinePlayers().iterator();

                                while(true) {
                                    while(true) {
                                        Player online;
                                        do {
                                            if (!var13.hasNext()) {
                                                MsgUtils.msg(s, Lang.CMD_ADMIN_RELOAD.getConfigValue(new String[]{String.valueOf(loaded)}));
                                                return true;
                                            }

                                            online = (Player)var13.next();
                                        } while(DeluxeTag.hasTagLoaded(online));

                                        identifier = this.plugin.getSavedTagIdentifier(online.getUniqueId().toString());
                                        if (identifier != null && DeluxeTag.getLoadedTag(identifier) != null && DeluxeTag.getLoadedTag(identifier).hasTagPermission(online)) {
                                            DeluxeTag.getLoadedTag(identifier).setPlayerTag(online);
                                        } else {
                                            this.plugin.getDummy().setPlayerTag(online);
                                        }
                                    }
                                }
                            }
                        } else if (!s.hasPermission("deluxetags.setdescription")) {
                            MsgUtils.msg(s, Lang.CMD_NO_PERMS.getConfigValue(new String[]{"deluxetags.setdescription"}));
                            return true;
                        } else if (args.length < 3) {
                            MsgUtils.msg(s, Lang.CMD_ADMIN_SET_DESCRIPTION_INCORRECT.getConfigValue((String[])null));
                            return true;
                        } else {
                            id = args[1];
                            if (DeluxeTag.getLoadedTag(id) == null) {
                                MsgUtils.msg(s, Lang.CMD_ADMIN_SET_DESCRIPTION_FAIL.getConfigValue(new String[]{id}));
                                return true;
                            } else {
                                dTag = DeluxeTag.getLoadedTag(id);
                                tag = StringUtils.join(Arrays.copyOfRange(args, 2, args.length), " ");
                                if (tag.endsWith("_")) {
                                    tag = tag.substring(0, tag.length() - 1) + " ";
                                }

                                dTag.setDescription(tag);
                                this.plugin.getCfg().saveTag(dTag.getPriority(), dTag.getIdentifier(), dTag.getDisplayTag(), dTag.getDescription(), dTag.getPermission());
                                MsgUtils.msg(s, Lang.CMD_ADMIN_SET_DESCRIPTION_SUCCESS.getConfigValue(new String[]{id, dTag.getDisplayTag(), tag}));
                                return true;
                            }
                        }
                    }
                }
            }
        }
    }
}
