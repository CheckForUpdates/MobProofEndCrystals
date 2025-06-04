package com.intensitymc.mobproofendcrystals;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private MobproofEndCrystals plugin;

    private static File file;
    private static FileConfiguration configFile;

    public Config(MobproofEndCrystals plugin) {

        this.plugin = plugin;
    }

    public static void setupConfigFile() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("MobproofEndCrystals").getDataFolder(), "config.yml");

        if (!file.exists()) {

            try {

                file.createNewFile();

            }catch (IOException e) {
                System.out.println(e.getMessage());

            }
        }

        configFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration getConfigFile() {

        return configFile;

    }

    public static void save() {
        try {
            configFile.save(file);

        }catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }

    public static void loadConfig() {

        configFile = YamlConfiguration.loadConfiguration(file);
    }


    public static void reload() {

        loadConfig();

    }

    public static String format(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
