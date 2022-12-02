package com.intensitymc.mobproofendcrystals.events;

import com.intensitymc.mobproofendcrystals.CrystalLocations;
import com.intensitymc.mobproofendcrystals.MobproofEndCrystals;
import net.minecraft.network.chat.ChatDecoration;
import net.minecraft.network.chat.ChatDecorator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlaceCrystal implements Listener {

    private MobproofEndCrystals plugin;
    private Proofing MobProof;

    public PlaceCrystal(MobproofEndCrystals plugin) {
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
                crystal.setCustomName(plugin.getCrystalName());
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
            CrystalLocations.getConfigFile().set("Location." + uuid + ".x", Float.valueOf((float) location.getX()));
            CrystalLocations.getConfigFile().set("Location." + uuid + ".y", Float.valueOf((float) location.getY()));
            CrystalLocations.getConfigFile().set("Location." + uuid + ".z", Float.valueOf((float) location.getZ()));
            CrystalLocations.getConfigFile().set("Location." + uuid + ".world", location.getWorld().getName());
            CrystalLocations.getConfigFile().set("Location." + uuid + ".crystal-name", crystalName);
            //CrystalLocations.getConfigFile().set("Location." + uuid + ".player-Name", player.getDisplayName());
            CrystalLocations.save();
            CrystalLocations.reload();
        }

        this.MobProof = new Proofing(this.plugin);
        this.MobProof.MobProof();

    }

    /*@EventHandler
    public void removeCrystal(EntityDamageEvent event) {

        *//*if (event.getEntity() instanceof Player && !event.getEntity().hasPermission("mobproof.anvil")) {
            Player player = (Player) event.getEntity();
            event.setCancelled(true);

            player.sendMessage("You do not have permission to remove that");
            player.damage(999);
            return;
        }*//*

        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK
                && event.getEntity().getName().equals(plugin.getCrystalName())
                && event.getEntity().getType().equals(EntityType.ENDER_CRYSTAL)) {

            event.setCancelled(true);
            event.getEntity().remove();
        }


        UUID uuid = event.getEntity().getUniqueId();
        String uuidString = uuid.toString();

        CrystalLocations.getConfigFile().set("Location." + uuid, null);
        CrystalLocations.save();
        CrystalLocations.reload();

    }*/

    @EventHandler
    public void removeCrystals(EntityDamageByEntityEvent event) {

        if (event.getEntity().getType().equals(EntityType.ENDER_CRYSTAL)
                && event.getEntity().getName().equals(plugin.getCrystalName())//!= null //plugin.getCrystalName()
                && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            Player player = (Player) event.getDamager();

            if (!player.hasPermission(plugin.getAnvilPerm())) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "YOU DON'T HAVE PERMISSION TO MOVE THAT.");
                player.getPlayer().damage(999);
                return;
            }


                event.setCancelled(true);
                event.getEntity().remove();
                UUID uuid = event.getEntity().getUniqueId();
                String uuidString = uuid.toString();

                CrystalLocations.getConfigFile().set("Location." + uuid, null);
                CrystalLocations.save();
                CrystalLocations.reload();

        }

    }

}