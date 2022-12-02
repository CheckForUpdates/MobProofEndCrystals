package com.intensitymc.mobproofendcrystals.events;

import com.intensitymc.mobproofendcrystals.CrystalLocations;
import com.intensitymc.mobproofendcrystals.MobproofEndCrystals;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import java.util.List;
import java.util.UUID;

public class MobSpawn implements Listener {

    MobproofEndCrystals plugin;
    CrystalLocations crystalLocations;
    Location location;

    public MobSpawn(MobproofEndCrystals plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {

        int radius = plugin.getRadius();
        for (String key : crystalLocations.getConfigFile().getConfigurationSection("Location").getKeys(false)) {
            ConfigurationSection locations = crystalLocations.getConfigFile().getConfigurationSection("Location." + key);

            //CrystalLocations.getConfigFile().getString("Location");

            String playerWorld = event.getLocation().getWorld().getName();

            String uuidString = locations.getString("uuid");
            UUID uuid = UUID.fromString(uuidString);


            Double x = locations.getDouble("x");
            Double y = locations.getDouble("y");
            Double z = locations.getDouble("z");

            location = new Location(Bukkit.getWorld(playerWorld), x, y, z);

            World world = Bukkit.getServer().getWorld(playerWorld);

            List<Entity> nearbyEntities = (List<Entity>) location.getWorld().getNearbyEntities(location, radius, radius, radius);

            for (Entity e : nearbyEntities) {
                if (e instanceof Monster) {
                    //System.out.println(e.getName());
                    //((Monster) e).damage(999);
                    ((Monster) e).remove();
                }
                if (e.getType() == EntityType.PHANTOM || e.getType() == EntityType.HOGLIN) {
                    e.remove();
                }
            }
        }
    }
}

