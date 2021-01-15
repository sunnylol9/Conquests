package com.tempustpvp.conquests.conquestplugin.placeholders;

import com.google.common.collect.Lists;
import com.tempustpvp.conquests.conquestplugin.ConquestPlugin;
import com.tempustpvp.conquests.conquestplugin.struct.CapColor;
import com.tempustpvp.conquests.conquestplugin.struct.ConquestPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.*;

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
                ConquestPlugin.get().getEvent().getPoints().entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(entry -> sortedList.add(entry.getKey().getName()));
                Collections.reverse(sortedList);
                try {
                    return sortedList.get(placement);
                } catch (Exception ex) {
                    return ConquestPlugin.get().getConfig().getString("empty-placement");
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
                    return ConquestPlugin.get().getConfig().getString("empty-points");
                }
            } else if (params.startsWith("cap_")) {
                String[] args = params.split("_");
                CapColor capColor = CapColor.get(args[1]);
                if (capColor != null) {

                    Optional<Map.Entry<UUID, ConquestPlayer>> capper = ConquestPlugin.get().getEvent().getConquestPlayers().entrySet()
                            .stream().filter(entry -> {
                                if (entry.getValue().getCapColor() == capColor) {
                                    return true;
                                } else {
                                    return false;
                                }
                            }).findFirst();

                    if (capper.isPresent()) {
                        return capper.get().getValue().getPlayer().getName();
                    } else {
                        return ConquestPlugin.get().getConfig().getString("empty-capper");
                    }
                }
            } else if (params.startsWith("capamt_")) {
                String[] args = params.split("_");
                CapColor capColor = CapColor.get(args[1]);
                if (capColor != null) {


                    Optional<Map.Entry<UUID, ConquestPlayer>> capper = ConquestPlugin.get().getEvent().getConquestPlayers().entrySet()
                            .stream().filter(entry -> {
                                if (entry.getValue().getCapColor() == capColor) {
                                    return true;
                                } else {
                                    return false;
                                }
                            }).findFirst();

                    if (capper.isPresent()) {
                        double count = capper.get().getValue().getCappingTime();

                        double percent = count / 10D;
                        percent *= 100;

                        return ((int) percent) + "%";

                    } else {
                        return ConquestPlugin.get().getConfig().getString("empty-capamt");
                    }
                }
            }
        } else {
            return ConquestPlugin.get().getConfig().getString("event-not-running");
        }

        return ConquestPlugin.get().getConfig().getString("event-not-running");
    }
}
