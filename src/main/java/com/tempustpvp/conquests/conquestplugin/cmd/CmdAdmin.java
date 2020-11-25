package com.tempustpvp.conquests.conquestplugin.cmd;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdAdmin extends MassiveCommand {

    public CmdAdmin() {
        addAliases("admin");
        addChild(new CmdStart());
        addRequirements(new RequirementHasPerm("conquestplugin.admin"));
        setDesc("perform admin commands");
        setDescPermission(getDesc());
        addChild(new CmdStop());
        addChild(new CmdRegenBlocks());
        addChild(new CmdReload());
    }
}
