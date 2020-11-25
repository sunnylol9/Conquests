package com.tempustpvp.conquests.conquestplugin.cmd;

import com.massivecraft.massivecore.MassiveCorePerm;
import com.massivecraft.massivecore.command.MassiveCommand;

public class CmdConquest extends MassiveCommand {

    public CmdConquest() {
//        aliases.add("conquest");
//        requiredArgs.add("claim");
        addAliases("conquest");

//        addParameter(new Parameter<>(TypeString.get(), "claim"));
//        addParameter(new Parameter<>(TypeString.get(), "times"));
//        addParameter(new Parameter<>(TypeString.get(), "start"));
//        addParameter(new Parameter<>(TypeString.get(), "stop"));

//        addParameter(n)
//        setArgs(Arrays.asList("times", "claim", "start", "stop"));

//        setHelp(ChatColor.AQUA + "/conquest " + ChatColor.GRAY + "times",
//                ChatColor.AQUA + "/conquest " + ChatColor.GRAY + "claim",
//                ChatColor.AQUA + "/conquest " + ChatColor.GRAY + "start",
//                ChatColor.AQUA + "/conquest " + ChatColor.GRAY + "stop");

        setupPermClass = MassiveCorePerm.class;

        addChild(new CmdClaim());
        addChild(new CmdTimes());
        addChild(new CmdSetCap());
        addChild(new CmdTimeLeft());
        addChild(new CmdAdmin());
    }


}
