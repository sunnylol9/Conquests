package com.tempustpvp.conquests.conquestplugin.cmd;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.tempustpvp.conquests.conquestplugin.utils.Messages;

public class CmdTimes extends MassiveCommand {

    public CmdTimes() {
        addAliases("times", "time");
        setDesc("view conquest schedule");
    }

    @Override
    public void perform() {
        sender.sendMessage(Messages.CMD_TIMES.format());
    }
}
