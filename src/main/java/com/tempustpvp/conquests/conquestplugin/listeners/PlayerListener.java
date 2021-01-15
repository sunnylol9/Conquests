package com.tempustpvp.conquests.conquestplugin.listeners;

import be.maximvdw.featherboard.api.FeatherBoardAPI;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.tempustpvp.conquests.conquestplugin.ConquestPlugin;
import com.tempustpvp.conquests.conquestplugin.struct.Conquest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private Conquest conquest = ConquestPlugin.get().getEvent();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (ConquestPlugin.get().getEvent().isRunning()) {
            FeatherBoardAPI.showScoreboard(event.getPlayer(), "conquest");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (ConquestPlugin.get().getEvent().isRunning()) {
            ConquestPlugin.get().getEvent().getConquestPlayers().remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (ConquestPlugin.get().getEvent().isRunning()) {
            MPlayer mPlayer = MPlayer.get(event.getEntity());
            if (mPlayer.hasFaction()) {
                Conquest conquest = ConquestPlugin.get().getEvent();
                if (conquest.getPoints().containsKey(mPlayer.getFaction())) {
                    Faction faction = mPlayer.getFaction();
                    conquest.getPoints().put(faction,
                            Math.max(conquest.getPoints().get(faction) - ConquestPlugin.get().getConfig().getInt("death"), 0));
                }
            }

            Player killer = event.getEntity().getKiller();
            if (killer != null) {
                MPlayer kPlayer = MPlayer.get(killer);
                if (kPlayer.hasFaction()) {
                    Conquest conquest = ConquestPlugin.get().getEvent();
                    if (conquest.getPoints().containsKey(kPlayer.getFaction())) {
                        Faction faction = kPlayer.getFaction();
                        conquest.getPoints().put(faction, conquest.getPoints().get(faction) + ConquestPlugin.get().getConfig().getInt("kill"));
                    }
                }
            }
        }
    }


}
