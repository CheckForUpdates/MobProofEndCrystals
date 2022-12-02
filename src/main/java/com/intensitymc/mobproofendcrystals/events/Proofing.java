package com.intensitymc.mobproofendcrystals.events;

import com.intensitymc.mobproofendcrystals.CrystalLocations;
import com.intensitymc.mobproofendcrystals.MobproofEndCrystals;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

// THIS CLASS IS NOT BEING USED

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


/*
            if (entity.getUniqueId() == null) {

                System.out.println("This is null too");
            }

            if (entity == null) {
                System.out.println("entity is null");
                continue;
            }

            World world = Bukkit.getServer().getWorld("world");
            List<Entity> entities = entity.getNearbyEntities(radius, radius, radius);
            for (Entity e : world.getEntities()) {
                if (e instanceof Monster) {
                    entities.remove(e);
                }
            }*/



/*    public Entity getEntityByUniqueId(UUID uniqueId){
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (Entity entity : chunk.getEntities()) {
                    if (entity.getUniqueId().equals(uniqueId))
                        return entity;
                }
            }
        }

        return null;
    }

}*/
