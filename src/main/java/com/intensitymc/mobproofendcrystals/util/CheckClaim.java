package com.intensitymc.mobproofendcrystals.util;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CheckClaim {

    public boolean isInOwnClaim(Player player) {
        Location playerLocation = player.getLocation();
        DataStore dataStore = GriefPrevention.instance.dataStore;
        Claim claim = dataStore.getClaimAt(playerLocation, false, null);

        if (claim != null && claim.getOwnerID().equals(player.getUniqueId())) {
            // Player is in their own claim
            return true;
        }

        // Player is not in their own claim
        return false;
    }

}
