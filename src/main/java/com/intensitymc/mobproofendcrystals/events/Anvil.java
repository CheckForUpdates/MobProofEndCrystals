package com.intensitymc.mobproofendcrystals.events;

import com.intensitymc.mobproofendcrystals.MobproofEndCrystals;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class Anvil implements Listener {

    MobproofEndCrystals plugin;

    public Anvil(MobproofEndCrystals plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event){

        // check if the event has been cancelled by another plugin
        if (!event.isCancelled()) {
            HumanEntity ent = event.getWhoClicked();

            if (ent instanceof Player) {
                Player player = (Player)ent;
                Inventory inv = event.getInventory();

                if (inv instanceof AnvilInventory) {
                    InventoryView view = event.getView();
                    int rawSlot = event.getRawSlot();

                    // compare the raw slot with the inventory view to make sure we are talking about the upper inventory
                    if (rawSlot == view.convertSlot(rawSlot)) {
                    /*
                    slot 0 = left item slot
                    slot 1 = right item slot
                    slot 2 = result item slot

                    see if the player clicked in the result item slot of the anvil inventory
                    */
                        if (rawSlot == 2) {

                            ItemStack item = event.getCurrentItem();

                            if (item != null) {
                                ItemMeta meta = item.getItemMeta();

                                if (meta != null) {

                                    // see whether the item is being renamed
                                    if (meta.hasDisplayName()) {
                                        String displayName = meta.getDisplayName();

                                        if (!player.hasPermission(plugin.getAnvilPerm())
                                                && displayName.equals( plugin.getConfig().get("crystal-name"))
                                            && item.getType() == Material.END_CRYSTAL) {

                                            event.setCancelled(true);
                                            player.sendMessage(ChatColor.RED + "You do not have permission to perform this operation.");
                                        }
                                        else if (player.hasPermission(plugin.getAnvilPerm())
                                                    && displayName.equals( plugin.getConfig().get("crystal-name"))
                                                    && item.getType() == Material.END_CRYSTAL) {

                                        player.sendMessage(ChatColor.GREEN + "You have created a new Mobproofer");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
