package com.intensitymc.mobproofendcrystals.events;

import com.intensitymc.mobproofendcrystals.CrystalLocations;
import com.intensitymc.mobproofendcrystals.MobproofEndCrystals;
import com.intensitymc.mobproofendcrystals.util.CheckClaim;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlaceCrystal implements Listener {

    private MobproofEndCrystals plugin;
    private Proofing MobProof;
    private String thePlayerName;
    private String playerIGN;

    public PlaceCrystal(MobproofEndCrystals plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void AnEvent(PlayerInteractEvent event) {

        CheckClaim checkClaim = new CheckClaim();
        Player player = event.getPlayer();
        Location location;

        Location playerLocation = player.getLocation();
        DataStore dataStore = GriefPrevention.instance.dataStore;
        Claim claim = dataStore.getClaimAt(playerLocation, false, null);

        //boolean isInOwnClaim = checkClaim.isInOwnClaim(player);

        this.setThePlayerName(player.getDisplayName());
        this.setPlayerIGN(player.getName());

        String crystalName = ChatColor.translateAlternateColorCodes('&', plugin.getCrystalName());

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
                    && (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.END_CRYSTAL)
                    && event.getPlayer().getWorld().getName().equals("world_the_end")) { //&& !player.hasPermission(plugin.getAdminPerm())) {
                player.sendMessage(ChatColor.RED + "Mob crystals are not allowed in the end world");
                return;
            }
            if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(crystalName)
                    && (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.END_CRYSTAL)) {

                location = event.getClickedBlock().getLocation().add(0.5, 1, 0.5);

                if (location.getBlock().getType() != Material.AIR) {
                    return;

                }


                // Player is outside their own claim
                if (claim != null && !claim.getOwnerID().equals(player.getUniqueId()) && !event.getPlayer().hasPermission(plugin.getOverridePerm())) {
                    player.sendMessage(ChatColor.RED + "These Cyrstals are only allowed in your own claims.");
                    return;
                }

                if (!event.getPlayer().hasPermission(plugin.getOverridePerm()) && event.getPlayer().isOp()) {
                    player.sendMessage(ChatColor.RED + "These Cyrstals are only allowed in your own claims.");
                    return;
                }



                if (!event.getPlayer().hasPermission(plugin.getRecipePerm())) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to use this.");
                    //event.getPlayer().damage(999);
                    return;
                }

                if (event.getPlayer().hasPermission(plugin.getOverridePerm())) {


                    EnderCrystal crystal = (EnderCrystal) player.getWorld().spawnEntity(location, EntityType.ENDER_CRYSTAL);
                    crystal.setCustomName(this.getThePlayerName());
                    crystal.setShowingBottom(false);
                    crystal.setCustomNameVisible(true);

                    if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {

                        if (player.getInventory().getItemInMainHand().getAmount() == 1) {
                            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                        } else {
                            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                        }
                    }

                    this.setThePlayerName(player.getDisplayName());
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
            CrystalLocations.getConfigFile().set("Location." + uuid + ".player-Name", this.getPlayerIGN());

            CrystalLocations.save();
            CrystalLocations.reload();
        }

        this.MobProof = new Proofing(this.plugin);
        this.MobProof.MobProof();

    }

    @EventHandler
    public void removeCrystals(EntityDamageByEntityEvent event) {

        if (event.getEntity().getType().equals(EntityType.ENDER_CRYSTAL)
                //&& event.getEntity().getCustomName().equalsIgnoreCase(this.getThePlayerName())//.contains("§")
                && event.getEntity().getCustomName().contains("§")
                && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {

            Player player = (Player) event.getDamager();


            if (!player.hasPermission(plugin.getRecipePerm())) {
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
    public void setThePlayerName(String thePlayerName) {
        this.thePlayerName = thePlayerName;
    }

    public String getThePlayerName() {
        return thePlayerName;
    }

    public void setPlayerIGN(String playerIGN) {
        this.playerIGN = playerIGN;
    }

    public String getPlayerIGN() {
        return playerIGN;
    }

    public boolean isInOwnClaim(Player player) {

        // Player is not in their own claim
        return false;
    }
}