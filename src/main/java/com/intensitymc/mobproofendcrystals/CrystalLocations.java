package com.intensitymc.mobproofendcrystals;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CrystalLocations {

	private MobproofEndCrystals plugin;

	private static File file;
	private static FileConfiguration locationsFile;

	public CrystalLocations(MobproofEndCrystals plugin) {

		this.plugin = plugin;
	}
	
	public static void setupLocationsFile() {
		file = new File(Bukkit.getServer().getPluginManager().getPlugin("MobproofEndCrystals").getDataFolder(), "locations.yml");

		if (!file.exists()) {
			
			try {
				
				file.createNewFile();
				
			}catch (IOException e) {
				System.out.println(e.getMessage());
				
			}
		}

		locationsFile = YamlConfiguration.loadConfiguration(file);
	}
	
	public static FileConfiguration getConfigFile() {

		return locationsFile;
		
	}
	
	public static void save() {
		try {
			locationsFile.save(file);
			
		}catch (IOException e) {
			System.out.println(e.getMessage());
			
		}
	}
		
	public static void loadConfig() {

		locationsFile = YamlConfiguration.loadConfiguration(file);
	}
	
	
	public static void reload() {

		loadConfig();

	}

}
