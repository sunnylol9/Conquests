package com.tempustpvp.conquests.conquestplugin.utils;

import com.tempustpvp.conquests.conquestplugin.ConquestPlugin;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundUtils {

    public static void playSound(Player p, String path) {
        if (ConquestPlugin.get().getConfig().getBoolean("Sounds." + path + ".Enabled")) {
            float volume = (float) ConquestPlugin.get().getConfig().getDouble("Sounds." + path + ".Volume");
            float pitch = (float) ConquestPlugin.get().getConfig().getDouble("Sounds." + path + ".Pitch");
            p.playSound(p.getLocation(), Sound.valueOf(ConquestPlugin.get().getConfig().getString("Sounds." + path + ".Name").toUpperCase()), volume, pitch);
        }
    }

}
