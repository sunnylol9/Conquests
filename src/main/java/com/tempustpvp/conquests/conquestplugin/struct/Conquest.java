package com.tempustpvp.conquests.conquestplugin.struct;

import be.maximvdw.featherboard.api.FeatherBoardAPI;
import com.google.common.collect.Lists;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.tempustpvp.conquests.conquestplugin.ConquestPlugin;
import com.tempustpvp.conquests.conquestplugin.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Conquest {

    private MassiveMap<UUID, ConquestPlayer> conquestPlayers;

    private long started;
    private int length;

    private boolean running;

    private MassiveMap<Faction, Integer> points;

    public Conquest() {
        length = ConquestPlugin.get().getConfig().getInt("conquest-time");
        conquestPlayers = new MassiveMap<>();
    }

    public long getStarted() {
        return started;
    }

    public void setStarted(long started) {
        this.started = started;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void startEvent() {
        if (isRunning()) return;
        ConquestPlugin.get().logFile("[StartEvent]");
        setRunning(true);
        setStarted(System.currentTimeMillis());
        setPoints(new MassiveMap<>());
        setConquestPlayers(new MassiveMap<>());
        Bukkit.broadcastMessage(Messages.CONQUEST_START.format());
        Bukkit.getOnlinePlayers().forEach(player -> FeatherBoardAPI.showScoreboard(player, "conquest"));
    }

    public void stopEvent() {
        if (!isRunning()) return;
        if (!points.isEmpty()) {
            List<Faction> sortedList = Lists.newArrayList();
            points.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(entry -> sortedList.add(entry.getKey()));
            Collections.reverse(sortedList);
            Faction winner = sortedList.get(0);
//            File file = new File(ConquestPlugin.get().getDataFolder() + File.separator + "data.yml");
//            FileConfiguration data = YamlConfiguration.loadConfiguration(file);
//            if (data.contains("loot." + winner.toString())) {
//                data.set("loot." + winner.toString(), data.getInt("loot." + winner.toString()) + 1);
//            } else {
//                data.set("loot." + winner.toString(), 1);
//            }
//            try {
//                data.save(file);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            //OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(winner);
            Bukkit.broadcastMessage(Messages.CONQUEST_WIN.format("winner", winner.getName()));
            //if (offlinePlayer.isOnline()) {
            //    Player player = Bukkit.getPlayer(winner);
            //    player.sendMessage(Messages.UNCLAIMED_REWARDS.format("amount", data.get("loot." + winner.toString())));
           // }
            ConquestPlugin.get().logFile("[StopEvent] winner=" + winner.getName());
        } else {
            Bukkit.broadcastMessage(Messages.CONQUEST_END.format());
            ConquestPlugin.get().logFile("[StopEvent] winner=none");
        }
        setRunning(false);
        Bukkit.getOnlinePlayers().forEach(FeatherBoardAPI::resetDefaultScoreboard);

    }

    public MassiveMap<Faction, Integer> getPoints() {
        return points;
    }

    public void setPoints(MassiveMap<Faction, Integer> points) {
        this.points = points;
    }

    public MassiveMap<UUID, ConquestPlayer> getConquestPlayers() {
        return conquestPlayers;
    }

    public void setConquestPlayers(MassiveMap<UUID, ConquestPlayer> conquestPlayers) {
        this.conquestPlayers = conquestPlayers;
    }

    public void addPoint(Player player) {

        MPlayer mPlayer = MPlayer.get(player);
        Faction faction = mPlayer.getFaction();
        points.set(faction, points.getOrDefault(faction, 0) + 1);
        if (points.get(faction) >= ConquestPlugin.get().getConfig().getInt("max-points")) {
            stopEvent();
        }

        ConquestPlugin.get().logFile("[AddPoint] player=" + player.getName() + " faction=" + faction.getName() + " points=" +
                points.get(faction));

//        Bukkit.broadcastMessage(faction.getTag() + " gained 1 point, total = " + points.get(faction));
    }

    public boolean isFinish() {
        return System.currentTimeMillis() >= (getStarted() + (getLength() * 1000));
    }
}
