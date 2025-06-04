package com.intensitymc.mobproofendcrystals;

import com.intensitymc.mobproofendcrystals.commands.Commands;
import com.intensitymc.mobproofendcrystals.events.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;


public class MobproofEndCrystals extends JavaPlugin {

    private MobproofEndCrystals plugin;

    private String anvilPerm = "mobproof.anvil";
    private String adminPerm = "mobproof.admin";
    private String recipePerm = "mobproof.recipe";
    private String overridePerm = "mobproof.override";

    private int radius;
    private String crystalName;

    @Override
    public void onEnable() {

        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            Bukkit.getServer().getConsoleSender().sendMessage("[" + ChatColor.GOLD + "MobproofEndCrystals" + ChatColor.RESET + "] " +
                    ChatColor.RED + "No Config Found. Generating a new one...");
            this.saveDefaultConfig();

        } else {
            Bukkit.getServer().getConsoleSender().sendMessage("[" + ChatColor.GOLD + "MobproofEndCrystals" + ChatColor.RESET + "] " +
                    ChatColor.GREEN + "Loading config file.");
            setRadius(getConfig().getInt("radius"));
            setCrystalName(getConfig().getString("crystal-name"));

            Bukkit.getServer().getConsoleSender().sendMessage("[" + ChatColor.GOLD + "MobproofEndCrystals" + ChatColor.RESET + "] " +
                    ChatColor.GREEN + "Config loaded successfully.");
        }

        CrystalLocations.setupLocationsFile();
        CrystalLocations.save();


        getCommand("mobproof").setExecutor(new Commands(this));
        getServer().getPluginManager().registerEvents(new Anvil(this), this);
        getServer().getPluginManager().registerEvents(new Workbench(this), this);
        getServer().getPluginManager().registerEvents(new MobSpawn(this), this);
        getServer().getPluginManager().registerEvents(new PlaceCrystal(this), this);


        Bukkit.getServer().getConsoleSender().sendMessage("[" + ChatColor.GOLD + "MobproofEndCrystals" + ChatColor.RESET + "] " + ChatColor.DARK_AQUA
                + "MobproofEndCrystals has been successfully enabled.");

    }

    @Override
    public void onDisable() {

        Bukkit.getServer().getConsoleSender().sendMessage("[" + ChatColor.GOLD + "MobproofEndCrystals" + ChatColor.RESET + "] " + ChatColor.DARK_AQUA
                + "MobproofEndCrystals has been successfully disabled.");
    }


    public String getAnvilPerm(){

        return anvilPerm;
    }

    public String getAdminPerm(){

        return adminPerm;
    }

    public String getRecipePerm(){

        return recipePerm;
    }

    public int getRadius() {

        return radius;
    }

    public String getOverridePerm() {

        return overridePerm;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setCrystalName(String crystalName) {
        this.crystalName = crystalName;
    }

    public String getCrystalName() {
        return ChatColor.translateAlternateColorCodes('&', crystalName);
    }


}
