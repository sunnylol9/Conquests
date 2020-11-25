package com.tempustpvp.conquests.conquestplugin.utils;

import org.bukkit.ChatColor;

public enum Messages {

    NO_REWARDS("&cYou have no rewards to claim"),

    UNCLAIMED_REWARDS("&aYou have &e%amount% &aunclaimed conquest rewards!"),
    CONQUEST_WIN("\n&8&m---------------------------------------\n" +
            "&7Conquest has ended resulting in &a%winner% &7winning\n" +
            "&8&m---------------------------------------\n"),

    INCORRECT_COLOR("&7Cap colors are &cred, blue, yellow and green"),
    INCORRECT_REGION("&cRegion was not found"),

    SET_CAP("&7Successfully set &a%color%&7 cap region to &a%region%"),
    SET_REGENBLOCKS("&7Successfully set &a%region% &7to regenerate"),

    EVENT_TIMER("&8&m---------------------------------------\n&7Conquest will end in &c%time%\n&8&m---------------------------------------"),

    CONQUEST_START("\n&8&m---------------------------------------\n" +
            "&7Conquest has begun\n" +
            "&8&m---------------------------------------\n"),
    CONQUEST_END("\n&8&m---------------------------------------\n" +
            "&7Conquest has ended\n" +
            "&8&m---------------------------------------\n"),

    START_CAPPING("&aStarted capping conquest"),

    CMD_TIMES("&8&m---------------------------------------\n" +
            "&7Schedule: &a6AM 6PM EST\n" +
            "&8&m---------------------------------------"),
    EVENT_TIMER_TO_START("&8&m---------------------------------------\n&7Conquest will start in &c%time%\n&8&m---------------------------------------"),

    COUNTDOWN("&aConquest about to start in &e%time%"),

    REGEN_BLOCK_BROKEN("&4&lVanity&f&lMC &7» &fDurability: &4%durability%");

    private String key, value;

    Messages(String value) {
        this.key = name();
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String format(Object... args) {
        if (args.length > 0) {
            String s = getValue();
            for (int i = 0; i < args.length; i += 2) {
                s = s.replace("%" + args[i].toString() + "%", args[i + 1].toString());
            }
            return ChatColor.translateAlternateColorCodes('&', s);
        }
        return ChatColor.translateAlternateColorCodes('&', getValue());
    }

    public String getValue() {
        return value;
    }

    public void setValue(String newValue) {
        this.value = newValue;
    }

}
