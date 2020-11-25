package com.tempustpvp.conquests.conquestplugin.struct;

import com.tempustpvp.conquests.conquestplugin.ConquestPlugin;
import com.tempustpvp.conquests.conquestplugin.utils.SoundUtils;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ConquestPlayer {

    private long lastCap;
    private Player player;
    private long cappingTime;
    private CapColor capColor;

    public ConquestPlayer(Player player, CapColor capColor) {
        this.capColor = capColor;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public long getCappingTime() {
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

    public void tryCap() {

        int time = ConquestPlugin.get().getConfig().getInt("time-per-cap");
        if (System.currentTimeMillis() - lastCap >= 100 * time) {
            cappingTime++;
            lastCap = System.currentTimeMillis();
        }

//        cappingTime++;
//
        if (cappingTime >= 10) {
            ConquestPlugin.get().getEvent().addPoint(player);
            SoundUtils.playSound(player, "Progress");
            cappingTime = 0;
        }
//
        StringBuilder msg = new StringBuilder();

//        Bukkit.broadcastMessage("complete: " + complete + " a: " + (cappingTime % max) + " b: " + max + " c: " + cappingTime);

        for (long i = 0; i < cappingTime; i++) {
            msg.append(ChatColor.translateAlternateColorCodes('&',
                    ConquestPlugin.get().getConfig().getString("complete")));
        }

        for (long i = 0; i < 10 - cappingTime; i++) {
            msg.append(ChatColor.translateAlternateColorCodes('&',
                    ConquestPlugin.get().getConfig().getString("incomplete")));
        }

//        msg.append(" " + ChatColor.YELLOW + "(" + ConquestPlugin.get().getEvent().getPoints(player) + ")");

        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + msg + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityPlayer.playerConnection.sendPacket(ppoc);

//        Bukkit.broadcastMessage("Current Cap: " + cappingTime);
    }

}
