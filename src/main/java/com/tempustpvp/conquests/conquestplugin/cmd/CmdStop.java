package com.tempustpvp.conquests.conquestplugin.cmd;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.tempustpvp.conquests.conquestplugin.ConquestPlugin;
import com.tempustpvp.conquests.conquestplugin.struct.Conquest;

public class CmdStop extends MassiveCommand {

    public CmdStop() {
        addAliases("stop");
        setDesc("stop conquest");
        setDescPermission(getDesc());
        addRequirements(new RequirementHasPerm("conquestplugin.stop"));
    }

    @Override
    public void perform() {
        Conquest conquest = ConquestPlugin.get().getEvent();
        if (conquest.isRunning()) {
            conquest.stopEvent();
        }
    }
}
