package com.tempustpvp.conquests.conquestplugin.utils;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class WorldGuardUtil {

    private static WorldGuardPlugin wg;

    static {
        wg = WorldGuardPlugin.inst();
    }

    public static String getRegionName(Location l) {
        try {
            Vector blockVector = BukkitUtil.toVector(l.getBlock());

            List<String> regionSet = wg.getGlobalRegionManager().get(l.getWorld()).getApplicableRegionsIDs(blockVector);
            if (regionSet.size() < 1) {
                return "";
            }

            String return_region = "";
            int return_priority = -1;

            for (String region : regionSet) {
                int region_priority = wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getPriority();

                if (return_region.equalsIgnoreCase("")) {

                    return_region = wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getId();
                    return_priority = region_priority;
                }

                if (region_priority > return_priority) {
                    return_region = wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getId();
                    return_priority = region_priority;
                }
            }
            return return_region;
        } catch (Exception exception) {


            return "";
        }
    }

    public static ProtectedRegion getRegion(Location l) {
        Vector blockVector = BukkitUtil.toVector(l.getBlock());

        List<String> regionSet = Objects.requireNonNull(wg.getGlobalRegionManager().get(l.getWorld())).getApplicableRegionsIDs(blockVector);
        if (regionSet.size() < 1) {
            return null;
        }

        ProtectedRegion return_region = null;
        int return_priority = -1;

        for (String region : regionSet) {
            int region_priority = wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region).getPriority();

            if (return_region == null) {

                return_region = wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region);
                return_priority = region_priority;
            }

            if (region_priority > return_priority) {
                return_region = wg.getGlobalRegionManager().get(l.getWorld()).getRegion(region);
                return_priority = region_priority;
            }
        }

        return return_region;
    }

    public static Map.Entry<String, ProtectedRegion> findRegion(World world, String regionName) {
        if (wg.getGlobalRegionManager().get(world) == null || wg.getGlobalRegionManager().get(world).getRegions().isEmpty())
            return null;
        Optional<Map.Entry<String, ProtectedRegion>> result = wg.getGlobalRegionManager().get(world).getRegions().entrySet().stream().filter(entry -> entry.getKey().equalsIgnoreCase(regionName)).findFirst();
        return result.orElse(null);
    }
}
