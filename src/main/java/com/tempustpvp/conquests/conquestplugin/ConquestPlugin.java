package com.tempustpvp.conquests.conquestplugin;

import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassivePlugin;
import com.tempustpvp.conquests.conquestplugin.cmd.CmdConquest;
import com.tempustpvp.conquests.conquestplugin.listeners.BlockListener;
import com.tempustpvp.conquests.conquestplugin.listeners.PlayerListener;
import com.tempustpvp.conquests.conquestplugin.placeholders.PluginPlaceholder;
import com.tempustpvp.conquests.conquestplugin.struct.CapColor;
import com.tempustpvp.conquests.conquestplugin.struct.Conquest;
import com.tempustpvp.conquests.conquestplugin.struct.ConquestPlayer;
import com.tempustpvp.conquests.conquestplugin.struct.TimerObj;
import com.tempustpvp.conquests.conquestplugin.utils.Messages;
import com.tempustpvp.conquests.conquestplugin.utils.WorldGuardUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ConquestPlugin extends MassivePlugin {

    private static ConquestPlugin instance;

    private Conquest event;
    private TimerObj timerObj;

    public static ConquestPlugin get() {
        return instance;
    }

    @Override
    public void onLoadPost() {
        instance = this;
    }

    @Override
    public void onDisable() {
        PlaceholderAPI.unregisterExpansion(new PluginPlaceholder());
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveResource("data.yml", false);
        saveDefaultConfig();
//        getCommand("conquest").setExecutor(new ConquestCommand(this));
        log("Loaded config");
        activate(new CmdConquest());
        log("Loaded conquest command");
        initMessages();
        log("Loaded messages");
        event = new Conquest();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        PlaceholderAPI.registerExpansion(new PluginPlaceholder());
        timerObj = new TimerObj(getConfig().getStringList("times"), getConfig().getIntegerList("countdown"));

        /*Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (isEnabled()) {
                timerObj.checkTimer();
            }
        }, 20L, 20L);*/

        Bukkit.getScheduler().runTaskTimer(this, () -> {

            if (getEvent().isRunning()) {


                for (Player player : Bukkit.getOnlinePlayers()) {

                    // Get Region of location
                    String region = WorldGuardUtil.getRegionName(player.getLocation());

                    // Remove all factions that are disbanded
                    getEvent().getPoints().entrySet().removeIf(entry -> FactionColl.get().getByName(entry.getKey().getName()) == null);

                    // Cap Color define
                    CapColor capColor = null;

                    // Loop through all cap names
                    for (String cap : this.getConfig().getConfigurationSection("cap").getKeys(false)) {
                        // If highest region is cap region
                        if (region.equals(this.getConfig().getConfigurationSection("cap").getString(cap))) {
                            // cap is that cap region
                            capColor = CapColor.get(cap);
                        }
                    }

                    // if cap not found
                    if (capColor == null) {
                        getEvent().getConquestPlayers().remove(player.getUniqueId());
                        continue;
                    }

                    // if the player does not have a faction then remove them
                    if (!MPlayer.get(player.getUniqueId()).hasFaction()) {
                        getEvent().getConquestPlayers().remove(player.getUniqueId());
                        continue;
                    }

                    boolean alreadyCappingFound = false;

                    for (ConquestPlayer conquestPlayer : getEvent().getConquestPlayers().values()) {
                        // check for already cap
                        if (conquestPlayer.getCapColor() == capColor && !conquestPlayer.getPlayer().getUniqueId().equals(player.getUniqueId())) {
//                                player.sendMessage(Messages.SOMEONE_ALREADY_CAPPING.format());
                            alreadyCappingFound = true;//Someone already capping
                        }
                    }

                    if (alreadyCappingFound) {
                        continue;
                    }

                    // no fac so cant cap lol
                    if (!MPlayer.get(player).hasFaction()) continue;

                    if (!getEvent().getConquestPlayers().isEmpty()) {
                        // if cap list not empty

                        if (getEvent().getConquestPlayers().containsKey(player.getUniqueId())) {
                            // player already capping

                            getEvent().getConquestPlayers().set(player.getUniqueId(), getEvent().getConquestPlayers().get(player.getUniqueId()).tryCap());

                        } else {
                            // player was not capping and will cap now
                            ConquestPlayer conquestPlayer = new ConquestPlayer(player, capColor).tryCap();

                            getEvent().getConquestPlayers().put(player.getUniqueId(), conquestPlayer);
                            conquestPlayer.getPlayer().sendMessage(Messages.START_CAPPING.format());
                        }

                    } else {
                        // list was empty so yeah...
                        ConquestPlayer conquestPlayer = new ConquestPlayer(player, capColor).tryCap();
                        getEvent().getConquestPlayers().put(player.getUniqueId(), conquestPlayer);
                        conquestPlayer.getPlayer().sendMessage(Messages.START_CAPPING.format());
                    }
                }
            }

        }, 0L, 0L);

        log("Finished enable");
    }

    private void initMessages() {
        File file = new File(getDataFolder() + File.separator + "messages.yml");
        if (file.exists()) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            for (Messages value : Messages.values()) {
                if (!configuration.contains(value.getKey())) {
                    configuration.set(value.getKey(), value.getValue());
                    try {
                        configuration.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    value.setValue(configuration.getString(value.getKey()));
                }
            }
        } else {
            try {
                file.createNewFile();
                FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                for (Messages value : Messages.values()) {
                    configuration.set(value.getKey(), value.getValue());
                }
                configuration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setValue(String s, String key) {
        FileConfiguration fileConfiguration = getConfig();
        fileConfiguration.set(s, key);
        try {
            fileConfiguration.save(new File(getDataFolder() + File.separator + "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        reloadConfig();
    }

    public Conquest getEvent() {
        return event;
    }

    public TimerObj getTimer() {
        return timerObj;
    }

    public void logFile(String message) {

        File logs = new File(ConquestPlugin.get().getDataFolder() + File.separator + "logs.txt");
        if (!logs.exists()) {
            try {
                logs.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter writer = new FileWriter(logs, true);
            writer.append(message + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
