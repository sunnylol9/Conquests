package com.tempustpvp.conquests.conquestplugin.cmd;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.tempustpvp.conquests.conquestplugin.ConquestPlugin;
import com.tempustpvp.conquests.conquestplugin.struct.Conquest;
import com.tempustpvp.conquests.conquestplugin.utils.LongUtil;
import com.tempustpvp.conquests.conquestplugin.utils.Messages;

public class CmdTimeLeft extends MassiveCommand {

    public CmdTimeLeft() {
        addAliases("timeleft");
        setDesc("view time before conquest");
    }

    @Override
    public void perform() {
        Conquest conquest = ConquestPlugin.get().getEvent();
        if (conquest.isRunning()) {
            long time = (conquest.getStarted() + (conquest.getLength() * 1000)) - System.currentTimeMillis();
            time /= 1000;

            sender.sendMessage(Messages.EVENT_TIMER.format("time", LongUtil.toLongForm(time)));
        } else {
            sender.sendMessage(Messages.EVENT_TIMER_TO_START.format("time", ConquestPlugin.get().getTimer().getTimeLeft()));
        }
    }
}
