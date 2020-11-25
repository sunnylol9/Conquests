package com.tempustpvp.conquests.conquestplugin.cmd;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.tempustpvp.conquests.conquestplugin.ConquestPlugin;
import com.tempustpvp.conquests.conquestplugin.utils.Messages;
import com.tempustpvp.conquests.conquestplugin.utils.WorldGuardUtil;
import org.bukkit.entity.Player;

import java.util.Map;

public class CmdRegenBlocks extends MassiveCommand {

    public CmdRegenBlocks() {
        addAliases("setregenblocks");
        setDesc("set regenblocks region");
        setDescPermission(getDesc());
        addRequirements(new RequirementHasPerm("conquestplugin.regenblocks"));
        addParameter(new Parameter<>(TypeString.get(), "region"));
    }

    @Override
    public void perform() {
        if (senderIsConsole || !(sender instanceof Player)) return;
        Player player = (Player) sender;

        String r = args.get(0);

        Map.Entry<String, ProtectedRegion> region = WorldGuardUtil.findRegion(player.getWorld(), r);
        if (region == null) {
            player.sendMessage(Messages.INCORRECT_REGION.format());
            return;
        }

        ConquestPlugin.get().setValue("regenblock.region", region.getKey());
        player.sendMessage(Messages.SET_REGENBLOCKS.format("region", ConquestPlugin.get().getConfig().getString("regenblock.region")));
    }
}
