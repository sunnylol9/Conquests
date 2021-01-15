package com.tempustpvp.conquests.conquestplugin.cmd;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.tempustpvp.conquests.conquestplugin.ConquestPlugin;
import com.tempustpvp.conquests.conquestplugin.struct.Conquest;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CmdLeaderboard extends MassiveCommand {

    public CmdLeaderboard() {
        addAliases("leaderboard", "lb");
        setDesc("view the conquest leaderboard");

        Parameter<Integer> page = new Parameter<>(TypeInteger.get(), "page");
        page.setRequiredFromConsole(false);
        page.setDefaultValue(1);

        setParameters(Collections.singletonList(
                page
        ));
    }

    private List<Faction> getPagedList(int page, int perPage, Conquest event) {

        Collection<Faction> collection = FactionColl.get().getAll();

        collection = collection.stream().sorted((o1, o2) -> {
            Integer s1 = event.getPoints().getOrDefault(o1, 0);
            Integer s2 = event.getPoints().getOrDefault(o2, 0);

            return s1.compareTo(s2);

        }).collect(Collectors.toList());

        List<Faction> sorted = new ArrayList<>(collection);

        Collections.reverse(sorted);

        List<Faction> pagedList = new ArrayList<>();

        int n = page - 1;
        int n2 = sorted.size() / perPage;

        sorted.removeIf(faction -> {

            if (faction == FactionColl.get().getSafezone()) return true;
            if (faction == FactionColl.get().getWarzone()) return true;
            return faction == FactionColl.get().getNone();

        });

        for (int i = 1 + perPage * n; i <= perPage * (n + 1); ++i) {

            if (sorted.get(i) != null) {

                Faction faction = sorted.get(i);
                pagedList.add(faction);
            }
        }

        return pagedList;
    }

    @Override
    public void perform() {

        if (!ConquestPlugin.get().getEvent().isRunning()) return;

        String header = ConquestPlugin.get().getConfig().getString("leaderboard.header");
        String replace = ConquestPlugin.get().getConfig().getString("leaderboard.line");

        Conquest event = ConquestPlugin.get().getEvent();

        int page;

        try {
            page = args.isEmpty() ? 1 : Integer.parseInt(args.get(0));
        } catch (NumberFormatException ex) {
            page = 1;
        }

        if (page <= 0) {
            page = 1;
        }

        int perPage = 10;

        List<Faction> pagedList = getPagedList(page, perPage, event);

        if (page != 1 && pagedList.isEmpty()) {
            page = 1;
            pagedList = getPagedList(page, perPage, event);
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', header)); // ??? WONT PRINT OUT

        int index = 0;

        for (Faction faction : pagedList) {

            index++;

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    replace
                            .replace("%index%", String.valueOf(perPage * (page - 1) + index))
                            .replace("%capping%", String.valueOf(event.getConquestPlayers().keySet().stream()
                                        .filter(uuid -> MPlayer.get(uuid).getFaction().getName().equals(faction.getName())).count()))
                            .replace("%total_members%", String.valueOf(faction.getMPlayers().size()))
                            .replace("%faction%", faction.getName(MPlayer.get(sender)))
                            .replace("%points%", String.valueOf(event.getPoints().getOrDefault(faction, 0)))));
        }
    }

}
