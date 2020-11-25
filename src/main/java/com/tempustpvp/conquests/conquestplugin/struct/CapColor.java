package com.tempustpvp.conquests.conquestplugin.struct;

public enum CapColor {

    BLUE, YELLOW, RED, GREEN;

    public static CapColor get(String cap) {
        return valueOf(cap.toUpperCase());
    }
}
