package com.tempustpvp.conquests.conquestplugin.cmd;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.tempustpvp.conquests.conquestplugin.ConquestPlugin;
import com.tempustpvp.conquests.conquestplugin.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class CmdClaim extends MassiveCommand {

    public CmdClaim() {
        addAliases("claim", "c");
        setDesc("claim any unclaimed rewards");
    }

    @Override
    public void perform() {
        File file = new File(ConquestPlugin.get().getDataFolder() + File.separator + "data.yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        Player player = (Player) sender;
        int amount = data.getInt("loot." + player.getUniqueId().toString());

        if (amount <= 0 || !data.contains("loot." + player.getUniqueId().toString())) {
            player.sendMessage(Messages.NO_REWARDS.format());
            if (amount < 0) {
                data.set("loot." + player.getUniqueId().toString(), 0);
                try {
                    data.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            ConquestPlugin.get().getConfig().getStringList("claim-command").stream().map(command -> command.replace("%player%", player.getName()))
                    .forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
            data.set("loot." + player.getUniqueId().toString(), amount - 1);
            try {
                data.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            amount = data.getInt("loot." + player.getUniqueId().toString());
            if (amount > 0) player.sendMessage(Messages.UNCLAIMED_REWARDS.format("amount", amount));
        }
    }
}
