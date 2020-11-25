package com.tempustpvp.conquests.conquestplugin.listeners;

import com.tempustpvp.conquests.conquestplugin.ConquestPlugin;
import com.tempustpvp.conquests.conquestplugin.utils.Messages;
import com.tempustpvp.conquests.conquestplugin.utils.WorldGuardUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class BlockListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {

        if (ConquestPlugin.get().getConfig().getString("regenblock.region").equalsIgnoreCase(WorldGuardUtil.getRegionName(event.getBlock().getLocation()))) {
            // start
            Block block = event.getBlock();
            byte data = block.getData();
            Material type = block.getType();
            if (ConquestPlugin.get().getConfig().getString("regenblock.block").equalsIgnoreCase(type.name())) {
                event.setCancelled(true);
                block.setType(type);
                block.setData(data);

                if (!block.hasMetadata("cp_durability")) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(ConquestPlugin.get(), () -> {
                        block.removeMetadata("cp_durability", ConquestPlugin.get());
                        block.setType(type);
                        block.setData((byte) ConquestPlugin.get().getConfig().getInt("regenblock.default-data"));
                    }, 20 * ConquestPlugin.get().getConfig().getInt("regenblock.time"));
                }

                int current = getMetadata(block, "cp_durability", ConquestPlugin.get().getConfig().getInt("regenblock.max"));

                if (ConquestPlugin.get().getConfig().contains("regenblock.colors." + current)) {
                    block.setData((byte) ConquestPlugin.get().getConfig().getInt("regenblock.colors." + current));
                }
                current--;
                if (current == 0) {
                    block.setType(Material.AIR);
//                    block.removeMetadata("cp_durability", ConquestPlugin.get());
                } else {
                    block.setMetadata("cp_durability", new FixedMetadataValue(ConquestPlugin.get(), current));
                    if (ConquestPlugin.get().getConfig().getIntegerList("regenblock.message-interval")
                            .contains(current)) {
                        event.getPlayer().sendMessage(Messages.REGEN_BLOCK_BROKEN.format("durability", current));
                    }
                }
            }
        }
    }

    private int getMetadata(Block block, String m, int defaultAmount) {
        for (MetadataValue metadatum : block.getMetadata(m)) {
            if (metadatum.value() instanceof Integer) {
                return metadatum.asInt();
            }
        }
        return defaultAmount;
    }

}
