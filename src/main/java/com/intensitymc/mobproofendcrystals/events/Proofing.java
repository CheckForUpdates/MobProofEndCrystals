package com.intensitymc.mobproofendcrystals.events;

import com.intensitymc.mobproofendcrystals.CrystalLocations;
import com.intensitymc.mobproofendcrystals.MobproofEndCrystals;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

// This class may be redundant.

public class Proofing implements Listener {

    MobproofEndCrystals plugin;
    CrystalLocations crystalLocations;
    Location location;

    public Proofing(MobproofEndCrystals plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void MobProof() {

        int radius = plugin.getRadius();


        // -- need a function for this
        for (String key : crystalLocations.getConfigFile().getConfigurationSection("Location").getKeys(false)) {
            ConfigurationSection locations = crystalLocations.getConfigFile().getConfigurationSection("Location." + key);


            String uuidString = locations.getString("uuid");
            UUID uuid = UUID.fromString(uuidString);


            Double x = locations.getDouble("x");
            Double y = locations.getDouble("y");
            Double z = locations.getDouble("z");


            location = new Location(Bukkit.getWorld("world"), radius, radius, radius);

        }
    }
}