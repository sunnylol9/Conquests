package com.tempustpvp.conquests.conquestplugin.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.tempustpvp.conquests.conquestplugin.ConquestPlugin;
import org.bukkit.ChatColor;

public class CmdReload extends MassiveCommand {

    public CmdReload() {
        addAliases("reload");
        setDesc("reload the plugin");
        setDescPermission(getDesc());
        addRequirements(new RequirementHasPerm("conquestplugin.reload"));
    }

    @Override
    public void perform() throws MassiveException {
        ConquestPlugin.get().reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Reloaded");
    }
}
