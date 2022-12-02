package com.intensitymc.mobproofendcrystals.events;

import com.intensitymc.mobproofendcrystals.CrystalLocations;
import com.intensitymc.mobproofendcrystals.MobproofEndCrystals;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

public class RemoveCrystal implements Listener {

    private MobproofEndCrystals plugin;
    private Proofing MobProof;
    Location location;

    public RemoveCrystal(MobproofEndCrystals plugin) {

        this.plugin = plugin;
    }




    /*@EventHandler
    public void removeCrystal(EntityDamageEvent event) {

        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK
                && event.getEntity().getName() != null //.equals(plugin.getCrystalName()) //.getCustomName() != null
                && event.getEntity().getType().equals(EntityType.ENDER_CRYSTAL)) {

            HumanEntity humanEntity = (HumanEntity) event.getEntity();

            Player player = humanEntity.getKiller().getPlayer();

            player.sendMessage("Is this working?");

            if (player.getPlayer().equals(event.getCause())) {

                player.sendMessage("This is working now.");
                return;
            }

            event.setCancelled(true);
            event.getEntity().remove();
        }


        UUID uuid = event.getEntity().getUniqueId();
        String uuidString = uuid.toString();

        CrystalLocations.getConfigFile().set("Location." + uuid, null);
        CrystalLocations.save();
        CrystalLocations.reload();


    }*/

}
