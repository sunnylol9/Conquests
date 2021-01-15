package com.tempustpvp.conquests.conquestplugin.struct;

import com.tempustpvp.conquests.conquestplugin.ConquestPlugin;
import com.tempustpvp.conquests.conquestplugin.utils.SoundUtils;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Random;

public class ConquestPlayer {

    private long lastCap;
    private Player player;
    private double cappingTime;
    private CapColor capColor;

    public ConquestPlayer(Player player, CapColor capColor) {
        this.capColor = capColor;
        this.player = player;
        this.cappingTime = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public double getCappingTime() {
        return cappingTime;
    }

    public void setCappingTime(long cappingTime) {
        this.cappingTime = cappingTime;
    }

    public CapColor getCapColor() {
        return capColor;
    }

    public void setCapColor(CapColor capColor) {
        this.capColor = capColor;
    }

    public ConquestPlayer tryCap() {

        int time = ConquestPlugin.get().getConfig().getInt("time-per-cap." + getCapColor().name().toLowerCase());

        if (System.currentTimeMillis() - lastCap >= 0.01 * (time * 1000)) {
            cappingTime+=0.1;
            lastCap = System.currentTimeMillis();
        }

        if (cappingTime >= 10) {
            ConquestPlugin.get().getEvent().addPoint(player);
            SoundUtils.playSound(player, "Progress");
            cappingTime = 0;
        }

        /*
        StringBuilder msg = new StringBuilder();

        double percent = cappingTime / 10D;
        percent *= 100D;
        percent = (int) percent;

        for (long i = 0; i < percent; i++) {
            msg.append(ChatColor.translateAlternateColorCodes('&',
                    ConquestPlugin.get().getConfig().getString("complete")));
        }

        for (long i = 0; i < 100 - percent; i++) {
            msg.append(ChatColor.translateAlternateColorCodes('&',
                    ConquestPlugin.get().getConfig().getString("incomplete")));
        }

        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + msg + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityPlayer.playerConnection.sendPacket(ppoc);*/

        return this;
    }

}
