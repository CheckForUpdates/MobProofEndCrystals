package com.intensitymc.mobproofendcrystals.events;

import com.intensitymc.mobproofendcrystals.CrystalLocations;
import com.intensitymc.mobproofendcrystals.MobproofEndCrystals;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CrystalBlock implements Listener {

    private final MobproofEndCrystals plugin;
    private Proofing MobProof;

    public CrystalBlock(MobproofEndCrystals plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void AnEvent(PlayerInteractEvent event) {


        Player player = event.getPlayer();
        Location location;

        String crystalName = plugin.getCrystalName();


        try {
            if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(crystalName)
                    && event.getPlayer().getInventory().getItemInMainHand().getType() == Material.END_CRYSTAL
                    && (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
                return;
            }
            if (!event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(crystalName)
                    && !(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.END_CRYSTAL)) {
                return;
            }

            if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(crystalName)
                    && (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.END_CRYSTAL)) {

                location = event.getClickedBlock().getLocation().add(0.5, 1, 0.5);

                if (location.getBlock().getType() != Material.AIR) {
                    return;

                }

                if (!event.getPlayer().hasPermission(plugin.getAnvilPerm())) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to set that down");
                    //event.getPlayer().damage(999);
                    return;
                }

                EnderCrystal crystal = (EnderCrystal) player.getWorld().spawnEntity(location, EntityType.ENDER_CRYSTAL);
                crystal.setShowingBottom(false);

                if (player.getInventory().getItemInMainHand().getAmount() == 1) {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                } else {
                    player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1);
                }

            }
        } catch (Exception e) {

        }
    }


        @EventHandler
        public void placeCrystal(EntitySpawnEvent event) {

            String crystalName = plugin.getCrystalName();
            Location location = event.getLocation();

            if (event.getEntityType() == EntityType.ENDER_CRYSTAL) {
                UUID uuid = event.getEntity().getUniqueId();
                event.getEntity().setCustomName("MobproofEndCrystals");
                CrystalLocations.getConfigFile().set("Location." + uuid + ".uuid", uuid.toString());
                CrystalLocations.getConfigFile().set("Location." + uuid + ".x", (float) location.getX());
                CrystalLocations.getConfigFile().set("Location." + uuid + ".y", (float) location.getY());
                CrystalLocations.getConfigFile().set("Location." + uuid + ".z", (float) location.getZ());
                CrystalLocations.getConfigFile().set("Location." + uuid + ".world", location.getWorld().getName());
                CrystalLocations.getConfigFile().set("Location." + uuid + ".crystal-name", crystalName);
                //CrystalLocations.getConfigFile().set("Location." + uuid + ".player-Name", player.getDisplayName());
                CrystalLocations.save();
                CrystalLocations.reload();
            }

            this.MobProof = new Proofing(this.plugin);
            this.MobProof.MobProof();

    }

    @EventHandler
    public void removeCrystal(EntityDamageEvent event) {

        if (event.getEntity().getName().equals(plugin.getCrystalName())
                && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK
                && event.getEntity().getType().equals(EntityType.ENDER_CRYSTAL)) {

            event.setCancelled(true);
            event.getEntity().remove();
        }
        UUID uuid = event.getEntity().getUniqueId();
        String uuidString = uuid.toString();

        CrystalLocations.getConfigFile().set("Location." + uuid, null);
        CrystalLocations.save();
        CrystalLocations.reload();

    }



    /*@EventHandler
    public void removeCrystals(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();

        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK
                && event.getEntity().getName() != null //plugin.getCrystalName()
                && event.getEntity().getType().equals(EntityType.ENDER_CRYSTAL)) {

        if (!player.hasPermission("mobproof.anvil")) {

                player.sendMessage(ChatColor.RED + "You do not have permission to remove that.");
                event.setCancelled(true);
                //player.getPlayer().damage(999);
            }

        }

    }*/

                /*CrystalLocations.getConfigFile().set("Location." + uuid + ".x", Float.valueOf((float) location.getX()));
                CrystalLocations.getConfigFile().set("Location." + uuid + ".y", Float.valueOf((float) location.getY()));
                CrystalLocations.getConfigFile().set("Location." + uuid + ".z", Float.valueOf((float) location.getZ()));*/
}
