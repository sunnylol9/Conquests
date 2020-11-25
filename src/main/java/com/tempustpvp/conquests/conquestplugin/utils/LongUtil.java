package com.tempustpvp.conquests.conquestplugin.utils;

public class LongUtil {

    public static String toLongForm(long seconds) {
        if (seconds <= 0L) {
            return "0 seconds";
        }

        long minute = seconds / 60L;
        seconds %= 60L;
        long hour = minute / 60L;
        minute %= 60L;
        long day = hour / 24L;
        hour %= 24L;

        StringBuilder time = new StringBuilder();
        if (day != 0L) {
            time.append(day);
        }
        if (day == 1L) {
            time.append(" day ");
        } else if (day > 1L) {
            time.append(" days ");
        }
        if (hour != 0L) {
            time.append(hour);
        }
        if (hour == 1L) {
            time.append(" hour ");
        } else if (hour > 1L) {
            time.append(" hours ");
        }
        if (minute != 0L) {
            time.append(minute);
        }
        if (minute == 1L) {
            time.append(" minute ");
        } else if (minute > 1L) {
            time.append(" minutes ");
        }
        if (seconds != 0L) {
            time.append(seconds);
        }
        if (seconds == 1L) {
            time.append(" second");
        } else if (seconds > 1L) {
            time.append(" seconds");
        }

        return time.toString().trim();
    }
}
