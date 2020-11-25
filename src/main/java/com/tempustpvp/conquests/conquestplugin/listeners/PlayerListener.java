package com.tempustpvp.conquests.conquestplugin.listeners;

import be.maximvdw.featherboard.api.FeatherBoardAPI;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.tempustpvp.conquests.conquestplugin.ConquestPlugin;
import com.tempustpvp.conquests.conquestplugin.struct.CapColor;
import com.tempustpvp.conquests.conquestplugin.struct.Conquest;
import com.tempustpvp.conquests.conquestplugin.struct.ConquestPlayer;
import com.tempustpvp.conquests.conquestplugin.utils.Messages;
import com.tempustpvp.conquests.conquestplugin.utils.WorldGuardUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerListener implements Listener {

    private Conquest conquest = ConquestPlugin.get().getEvent();

    public PlayerListener() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(ConquestPlugin.get(), PacketType.Play.Client.FLYING,
                PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                try {
                    if (event.getPlayer() != null && ConquestPlugin.get().getEvent().isRunning()) {
                        Player player = event.getPlayer();
                        String region = WorldGuardUtil.getRegionName(player.getLocation());

                        conquest.getPoints().entrySet().removeIf(entry -> Factions.getInstance().getByTag(entry.getKey().getTag()) == null);

                        CapColor capColor = null;
                        for (String cap : plugin.getConfig().getConfigurationSection("cap").getKeys(false)) {
                            if (region.equals(cap)) {
                                capColor = CapColor.get(cap);
                                break;
                            }
                        }

                        if (capColor == null) {
                            if (!conquest.getConquestPlayers().isEmpty()) {
                                List<ConquestPlayer> removeQueue;
                                removeQueue = conquest.getConquestPlayers().stream().filter(p -> p.getPlayer().getUniqueId().equals(player.getUniqueId())).collect(Collectors.toList());
                                removeQueue.forEach(conquest.getConquestPlayers()::remove);
                            }
                            return;
                        }


                        List<ConquestPlayer> removeQueue;
                        removeQueue = conquest.getConquestPlayers().stream().filter(p -> !FPlayers.getInstance().getByPlayer(p.getPlayer()).hasFaction()).collect(Collectors.toList());
                        removeQueue.forEach(conquest.getConquestPlayers()::remove);

                        if (event.getPacketType() == PacketType.Play.Client.FLYING) {
                            for (ConquestPlayer conquestPlayer : conquest.getConquestPlayers()) {
                                if (conquestPlayer.getCapColor() == capColor && !conquestPlayer.getPlayer().getUniqueId().equals(player.getUniqueId())) {
//                                player.sendMessage(Messages.SOMEONE_ALREADY_CAPPING.format());
                                    return;//Someone already capping
                                }
                            }

                            if (!FPlayers.getInstance().getByPlayer(player).hasFaction()) return;

                            if (!conquest.getConquestPlayers().isEmpty()) {
                                Optional<ConquestPlayer> playerOptional = conquest.getConquestPlayers().stream().filter(p -> p.getPlayer().getUniqueId().equals(player.getUniqueId())).findFirst();

                                if (playerOptional.isPresent()) {
                                    ConquestPlayer conquestPlayer = playerOptional.get();
                                    conquest.getConquestPlayers().remove(conquestPlayer);
                                    conquestPlayer.tryCap();
                                    conquest.getConquestPlayers().add(conquestPlayer);
                                } else {
                                    ConquestPlayer conquestPlayer = new ConquestPlayer(player, capColor);
                                    conquestPlayer.tryCap();
                                    conquest.getConquestPlayers().add(conquestPlayer);
                                    conquestPlayer.getPlayer().sendMessage(Messages.START_CAPPING.format());
                                }
                            } else {
                                ConquestPlayer conquestPlayer = new ConquestPlayer(player, capColor);
                                conquestPlayer.tryCap();
                                conquest.getConquestPlayers().add(conquestPlayer);
                                conquestPlayer.getPlayer().sendMessage(Messages.START_CAPPING.format());
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (ConquestPlugin.get().getEvent().isRunning()) {
            FeatherBoardAPI.showScoreboard(event.getPlayer(), "conquest");
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (ConquestPlugin.get().getEvent().isRunning()) {
            FPlayer fPlayer = FPlayers.getInstance().getByPlayer(event.getEntity());
            if (fPlayer.hasFaction()) {
                Conquest conquest = ConquestPlugin.get().getEvent();
                if (conquest.getPoints().containsKey(fPlayer.getFaction())) {
                    Faction faction = fPlayer.getFaction();
                    conquest.getPoints().put(faction,
                            Math.max(conquest.getPoints().get(faction) - ConquestPlugin.get().getConfig().getInt("death"), 0));
                }
            }

            Player killer = event.getEntity().getKiller();
            if (killer != null) {
                FPlayer fKiller = FPlayers.getInstance().getByPlayer(killer);
                if (fKiller.hasFaction()) {
                    Conquest conquest = ConquestPlugin.get().getEvent();
                    if (conquest.getPoints().containsKey(fKiller.getFaction())) {
                        Faction faction = fKiller.getFaction();
                        conquest.getPoints().put(faction, conquest.getPoints().get(faction) + ConquestPlugin.get().getConfig().getInt("kill"));
                    }
                }
            }
        }
    }


}
