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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CmdSetCap extends MassiveCommand {

    private List<String> colors;

    public CmdSetCap() {
        colors = Arrays.
                asList("red", "blue", "green", "yellow");
        addAliases("setcap");
        setDesc("set cap locations");
        setDescPermission(getDesc());
        addRequirements(new RequirementHasPerm("conquestplugin.setcap"));
        setParameters(Arrays.asList(
                new Parameter<>(TypeString.get(), "color"),
                new Parameter<>(TypeString.get(), "region")
        ));
    }

    @Override
    public void perform() {
        if (senderIsConsole || !(sender instanceof Player)) return;
        Player player = (Player) sender;

        String capColor = args.get(0);
        String regionName = args.get(1);

        if (!colors.contains(capColor.toLowerCase())) {
            player.sendMessage(Messages.INCORRECT_COLOR.format());
            return;
        }

        Map.Entry<String, ProtectedRegion> region = WorldGuardUtil.findRegion(player.getWorld(), regionName);
        if (region == null) {
            player.sendMessage(Messages.INCORRECT_REGION.format());
            return;
        }

        ConquestPlugin.get().setValue("cap." + capColor.toLowerCase(), region.getKey());
        player.sendMessage(Messages.SET_CAP.format("color", capColor.toLowerCase(), "region", ConquestPlugin.get().getConfig().getString("cap." + capColor.toLowerCase())));
    }
}
