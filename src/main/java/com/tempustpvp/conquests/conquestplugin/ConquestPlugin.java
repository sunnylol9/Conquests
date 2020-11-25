package com.tempustpvp.conquests.conquestplugin;

import com.massivecraft.massivecore.MassivePlugin;
import com.tempustpvp.conquests.conquestplugin.cmd.CmdConquest;
import com.tempustpvp.conquests.conquestplugin.listeners.BlockListener;
import com.tempustpvp.conquests.conquestplugin.listeners.PlayerListener;
import com.tempustpvp.conquests.conquestplugin.placeholders.PluginPlaceholder;
import com.tempustpvp.conquests.conquestplugin.struct.Conquest;
import com.tempustpvp.conquests.conquestplugin.struct.TimerObj;
import com.tempustpvp.conquests.conquestplugin.utils.Messages;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

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

    public static int test() {
        return 3;
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
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (isEnabled()) {
                timerObj.checkTimer();
            }
        }, 20L, 20L);
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

}
