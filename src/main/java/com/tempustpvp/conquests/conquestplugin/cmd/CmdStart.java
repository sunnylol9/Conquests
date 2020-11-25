package com.tempustpvp.conquests.conquestplugin.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.tempustpvp.conquests.conquestplugin.ConquestPlugin;
import com.tempustpvp.conquests.conquestplugin.struct.Conquest;

public class CmdStart extends MassiveCommand {

    public CmdStart() {
        addAliases("start");
        setDesc("start conquest");
        setDescPermission(getDesc());
        addRequirements(new RequirementHasPerm("conquestplugin.start"));
    }

    @Override
    public void perform() throws MassiveException {
        Conquest conquest = ConquestPlugin.get().getEvent();
        if (!conquest.isRunning()) {
            conquest.startEvent();
        }
    }
}
