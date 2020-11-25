package com.tempustpvp.conquests.conquestplugin.placeholders;

import com.google.common.collect.Lists;
import com.tempustpvp.conquests.conquestplugin.ConquestPlugin;
import com.tempustpvp.conquests.conquestplugin.struct.CapColor;
import com.tempustpvp.conquests.conquestplugin.struct.ConquestPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PluginPlaceholder extends PlaceholderExpansion {


    @Override
    public String getIdentifier() {
        return "cp";
    }

    @Override
    public String getAuthor() {
        return "Sunny#4704";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {

        if (ConquestPlugin.get().getEvent().isRunning()) {

            if (params.startsWith("lb_")) {
                String[] args = params.split("_");
                int placement = Integer.parseInt(args[1]);
                List<String> sortedList = Lists.newArrayList();
                ConquestPlugin.get().getEvent().getPoints().entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(entry -> sortedList.add(entry.getKey().getTag()));
                Collections.reverse(sortedList);
                try {
                    return sortedList.get(placement);
                } catch (Exception ex) {
                    return ConquestPlugin.get().getConfig().getString("empty-points");
                }
            } else if (params.startsWith("lbv_")) {
                String[] args = params.split("_");
                int placement = Integer.parseInt(args[1]);
                List<Integer> sortedList = Lists.newArrayList();
                ConquestPlugin.get().getEvent().getPoints().entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(entry -> sortedList.add(entry.getValue()));
                Collections.reverse(sortedList);
                try {
                    return String.valueOf(sortedList.get(placement));
                } catch (Exception ex) {
                    return "0";
                }
            } else if (params.startsWith("cap_")) {
                String[] args = params.split("_");
                CapColor capColor = CapColor.get(args[1]);
                if (capColor != null) {
                    Optional<ConquestPlayer> capper = ConquestPlugin.get().getEvent().getConquestPlayers().stream().filter(cp -> cp.getCapColor() == capColor).findFirst();
                    if (capper.isPresent()) {
                        return capper.get().getPlayer().getName();
                    } else {
                        return ConquestPlugin.get().getConfig().getString("empty-cap");
                    }
                }
            }
        } else {
            return ConquestPlugin.get().getConfig().getString("event-not-running");
        }

        return null;
    }
}
