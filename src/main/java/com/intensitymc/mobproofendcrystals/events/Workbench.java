package com.intensitymc.mobproofendcrystals.events;

import com.intensitymc.mobproofendcrystals.MobproofEndCrystals;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Workbench implements Listener  {

    MobproofEndCrystals plugin;

    public Workbench(MobproofEndCrystals plugin) {

        this.plugin = plugin;

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCraftTableClick(InventoryClickEvent event){

        String crystalName = ChatColor.translateAlternateColorCodes('&', plugin.getCrystalName());

        // check if the event has been cancelled by another plugin
        if (!event.isCancelled()) {
            HumanEntity ent = event.getWhoClicked();

            // not really necessary
            if (ent instanceof Player) {
                Player player = (Player)ent;
                Inventory inv = event.getInventory();

                // see if the event is about a workbench
                if (inv instanceof CraftingInventory) {
                    InventoryView view = event.getView();
                    int rawSlot = event.getRawSlot();

                    // compare the raw slot with the inventory view to make sure we are talking about the upper inventory
                    if (rawSlot == view.convertSlot(rawSlot)) {
                    /*
                    slot 0 = Result item slot

                    see if the player clicked in the result item slot of the workbench inventory
                    */
                        if (rawSlot == 0) {

                            ItemStack item = event.getCurrentItem();

                            // check if there is an item in the result slot
                            if (item != null) {
                                ItemMeta meta = item.getItemMeta();

                                // it is possible that the item does not have metadata
                                if (meta != null) {

                                    // see if the result matches the crystal
                                    if (meta.hasDisplayName()) {
                                        String displayName = ChatColor.translateAlternateColorCodes('&', meta.getDisplayName());

                                        // deny crafting if the user does not have permission
                                        if (!player.hasPermission(plugin.getRecipePerm())
                                                && displayName.equals(crystalName)
                                                && item.getType() == Material.END_CRYSTAL) {

                                            event.setCancelled(true);
                                            player.playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_SLIME_BLOCK_STEP,1, 1);
                                            player.sendMessage(ChatColor.RED + "You do not have permission to perform this operation.");
                                        } else if (player.hasPermission(plugin.getRecipePerm())
                                                && displayName.equals(crystalName)
                                                && item.getType() == Material.END_CRYSTAL) {

                                            player.playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_SMITHING_TABLE_USE,1, 1);
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
