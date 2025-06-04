package com.intensitymc.mobproofendcrystals.commands;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import com.Zrips.CMI.PlayerManager;
import com.intensitymc.mobproofendcrystals.CrystalLocations;
import com.intensitymc.mobproofendcrystals.MobproofEndCrystals;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Commands implements CommandExecutor, TabCompleter {


    private final MobproofEndCrystals plugin;

    public Commands(MobproofEndCrystals plugin) {

        this.plugin = plugin;
    }

    private static final String msgNoPermission = ChatColor.RED + "You do not have permission to use this command.";
    private static final String msgMobproofSetRadius = ChatColor.GREEN + "Mobproofing radius now set to " + ChatColor.GOLD + "%radius% " + ChatColor.GREEN + "blocks";
    private static final String msgMobproofOutOfBounds = ChatColor.RED + "The radius cannot be set higher than 64.";
    private static final String msgCurrentRadius = ChatColor.GREEN + "Radius is set at " + ChatColor.GOLD + "%radius% " + ChatColor.GREEN + "blocks";
    private static final String msgCurrentName = ChatColor.GREEN + "The name for the anvil is set to " + ChatColor.RESET + "%name%";

    private static final String[] mobproofUsage = {
            ChatColor.YELLOW + "MobproofEndCrystals usage:",
            ChatColor.WHITE + "/mobproof info" + ChatColor.DARK_AQUA + " - Show current protection radius & crystal name.",
            ChatColor.WHITE + "/mobproof name <name>" + ChatColor.AQUA + "- The set the crystal name to match the recipe name.",
            ChatColor.WHITE + "/mobproof radius <radius>" + ChatColor.AQUA + " - Set the protection radius."

    };


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> list = null;
        if (sender.hasPermission(plugin.getAdminPerm())) {
            list = new ArrayList<>();
            if (args.length == 1) {
                list.add("clean");
                list.add("info");
                list.add("list");
                list.add("name");
                list.add("radius");
                list.add("reload");
            }
        }

        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        try {


            if (!sender.hasPermission(plugin.getAdminPerm())) {
                sender.sendMessage(msgNoPermission);
                return false;
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    CrystalLocations.reload();
                    sender.sendMessage(ChatColor.YELLOW + "Configuration for MobproofEndCrystals reloaded");

                }

            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("info")) {
                    sender.sendMessage(ChatColor.YELLOW
                            + msgCurrentRadius.replace("%radius%", String.valueOf(plugin.getRadius()))
                            + "\n"
                            + msgCurrentName.replace("%name%", String.valueOf(plugin.getCrystalName())));

                }

            }

            if (args.length == 2 && args[0].equalsIgnoreCase("radius")) {
                try {
                    int radius = Integer.parseInt(args[1]);
                    if (radius > 64) {
                        sender.sendMessage(msgMobproofOutOfBounds);
                        return false;
                    }
                    plugin.setRadius(radius);
                    plugin.getConfig().set("radius", radius);
                    plugin.saveConfig();
                    plugin.reloadConfig();
                    sender.sendMessage(msgMobproofSetRadius.replace("%radius%", String.valueOf(plugin.getRadius())));
                    return true;
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Something went wrong. Please try that command again.");
                    return true;
                }
            }

            if (args.length >= 2 && args[0].equalsIgnoreCase("name")) {
                try {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i]);
                        sb.append(" ");
                    }

                    String name = sb.toString().trim();

                    //String name = args[1];
                    plugin.setCrystalName(name);
                    plugin.getConfig().set("crystal-name", name);
                    plugin.saveConfig();
                    plugin.reloadConfig();
                    sender.sendMessage(msgCurrentName.replace("%name%", String.valueOf(plugin.getCrystalName())));
                    return true;
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Something went wrong. Please try that command again.");
                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("clean")) {

                if (!sender.hasPermission(plugin.getAdminPerm())) {
                    sender.sendMessage(msgNoPermission);
                    return false;
                }


                try {

                    //location = get the location from the location.yml file

                    for (String key : CrystalLocations.getConfigFile().getConfigurationSection("Location").getKeys(false)) {
                        ConfigurationSection locations = CrystalLocations.getConfigFile().getConfigurationSection("Location." + key);

                        //CrystalLocations.getConfigFile().getString("Location");

                        Double x = locations.getDouble("x");
                        Double y = locations.getDouble("y");
                        Double z = locations.getDouble("z");

                        String world = locations.getString("world");
                        String uuidString = locations.getString("uuid");
                        UUID uuid = UUID.fromString(uuidString);

                        Player player = (Player) sender;
                        Location location = new Location(Bukkit.getWorld(world), x, y, z);

                        if (!args[0].equalsIgnoreCase(player.getWorld().getName())) {
                            sender.sendMessage(ChatColor.RED + "You must be in " + args[0] + " to run this.");
                            break;
                        }
                        else {
                            if (world.equals(player.getWorld().getName())) {
                                if (Bukkit.getEntity(uuid) != null) {
                                    Bukkit.getEntity(uuid).remove();
                                    location.getBlock().setType(Material.AIR);
                                    CrystalLocations.getConfigFile().set("Location." + uuid, null);
                                } else {
                                    CrystalLocations.getConfigFile().set("Location." + uuid, null);
                                }
                            }

                            CrystalLocations.save();
                            CrystalLocations.reload();
                        }
                    }
                } catch (Exception e) {

                }
                finally {

                    sender.sendMessage("Mobproof world_the_end locations cleaned.");
                    Bukkit.getServer().getConsoleSender().sendMessage("Mobproof world_the_end locations cleaned.");
                }
            }

            if (args[0].equalsIgnoreCase("list")) {

                sender.sendMessage("\n\nCrystals: " + ChatColor.GRAY + "(Click the coordinates to teleport)\n");
                for (String key : Objects.requireNonNull(CrystalLocations.getConfigFile().getConfigurationSection("Location")).getKeys(false)) {
                    ConfigurationSection locations = CrystalLocations.getConfigFile().getConfigurationSection("Location." + key);
                    assert locations != null;
                    String playerName = locations.getString("player-Name");
                    String gameWorld = locations.getString("world");

                    // Assuming you already have CMI initialized
                    PlayerManager playerManager = CMI.getInstance().getPlayerManager();

                    String username = playerName;
                    String displayName;

                    CMIUser cmiUser = playerManager.getUser(username);
                    if (cmiUser != null) {
                        displayName = cmiUser.getDisplayName();
                    } else {
                        displayName = playerName;
                    }


                    // Create the clickable component for the coordinates
                    TextComponent coordinatesComponent = new TextComponent("          " + ChatColor.AQUA + locations.getString("x") + ", "
                            + locations.getString("y") + ", " + locations.getString("z") + ChatColor.WHITE + " ("+gameWorld+")");

                    String teleportCommand = "/teleport " + locations.getString("x") + " " + locations.getString("y") + " " + locations.getString("z");
                    coordinatesComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, teleportCommand));

                    String hoverText = "Click to teleport to coordinates";
                    coordinatesComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverText)));

                    // Append the coordinates component to the chat
                    sender.sendMessage(displayName + "");
                    sender.spigot().sendMessage(coordinatesComponent);

                }
                return true;
            }

        }catch (Exception e) {
            sender.sendMessage(mobproofUsage);
            sender.sendMessage(ChatColor.RED + "Something went wrong. Please try that command again.");
        }
        return false;
    }
}
