package com.intensitymc.mobproofendcrystals.commands;

import com.intensitymc.mobproofendcrystals.MobproofEndCrystals;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;


import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {


    private final MobproofEndCrystals plugin;

    public Commands(MobproofEndCrystals plugin) {

        this.plugin = plugin;
    }

    private static final String msgNoPermission = ChatColor.RED + "You do not have permission to use this command.";
    private static final String msgMobproofSetRadius = ChatColor.GREEN + "Mobproofing radius now set to " + ChatColor.GOLD + "%radius% " + ChatColor.GREEN + "blocks";
    private static final String msgMobproofOutOfBounds = ChatColor.RED + "The radius cannot be set higher than 64.";
    private static final String msgCurrentRadius = ChatColor.GREEN + "Radius is set at " + ChatColor.GOLD + "%radius% " + ChatColor.GREEN + "blocks";
    private static final String msgCurrentName = ChatColor.GREEN + "The name for the anvil is set to " + ChatColor.GOLD + "%name%";

    private static final String[] mobproofUsage = {
            ChatColor.YELLOW + "MobproofEndCrystals usage:",
            ChatColor.WHITE + "/mobproof info" + ChatColor.DARK_AQUA + " - Show current radius and item name.",
            ChatColor.WHITE + "/mobproof name <name>" + ChatColor.AQUA + "- The name for the anvil.",
            ChatColor.WHITE + "/mobproof radius <radius>" + ChatColor.AQUA + " - Mobproof radius."

    };


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> list = null;
        if (sender.hasPermission(plugin.getAdminPerm())) {
            list = new ArrayList<>();
            if (args.length == 1) {
                list.add("info");
                list.add("name");
                list.add("radius");
            }
        }

        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission(plugin.getAdminPerm())) {
            sender.sendMessage(msgNoPermission);
            return false;
        }

        if (args.length > 2 || args.length == 0) {
            sender.sendMessage(mobproofUsage);

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

        if (args.length == 2 && args[0].equalsIgnoreCase("name")) {
            try {
                String name = args[1];
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
        return false;
    }

}
