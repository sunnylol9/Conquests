package com.tempustpvp.conquests.conquestplugin.struct;

import com.tempustpvp.conquests.conquestplugin.ConquestPlugin;
import com.tempustpvp.conquests.conquestplugin.utils.LongUtil;
import com.tempustpvp.conquests.conquestplugin.utils.Messages;
import org.bukkit.Bukkit;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TimerObj {

    private List<String> events;
    private List<Integer> countdown;
    private List<Integer> done = new ArrayList<>();

    public TimerObj(List<String> events, List<Integer> countdown) {
        this.events = events;
        this.countdown = countdown;
    }

    public List<String> getEvents() {
        return events;
    }

    public void checkTimer() {
        for (String event : events) {
            if (hasTimeReached(event)) {
                ConquestPlugin.get().getEvent().startEvent();
            }
        }

        if (ConquestPlugin.get().getEvent().isRunning() && ConquestPlugin.get().getEvent().isFinish()) {
            ConquestPlugin.get().getEvent().stopEvent();
        }
    }

    private int[] convert(String time) {
        return new int[]{Integer.parseInt(time.split(":")[0]), Integer.parseInt(time.split(":")[1])};
    }

    private boolean hasTimeReached(String time) {
        LocalDateTime now = LocalDateTime.now();

        int hour = convert(time)[0];
        int min = convert(time)[1];


        int identifier = now.getHour() + now.getMinute();// newtime, 56, ready, 41, 23:18

        String t = findDifference(new String[]{time, now.getDayOfWeek().name()});
        for (Integer integer : countdown) {
            if (LongUtil.toLongForm(integer).equalsIgnoreCase(t)) {
                Bukkit.broadcastMessage(Messages.COUNTDOWN.format("time", LongUtil.toLongForm(integer)));
            }
        }

        if (done.contains(identifier)) {
            return false;
        }

        if (!done.isEmpty() && !done.contains(identifier)) {
            done.clear();
        }

        boolean ready = now.getHour() == hour && now.getMinute() == min;
        if (ready) {
            done.add(identifier);
        }

        return ready;
    }

    public String getTimeLeft() {
        for (String event : events) {
            int hour = convert(event)[0];
            int min = convert(event)[1];

            int identifier = hour + min;
            if (!isPast(event, LocalDateTime.now().getDayOfWeek().getValue()) && !done.contains(identifier)) {
                return findDifference(new String[]{event, LocalDateTime.now().getDayOfWeek().name()});
            }
        }


        for (String event : events) {
            return findDifference(new String[]{event, DayOfWeek.of(LocalDateTime.now().getDayOfWeek().getValue() + 1).name()});
        }

        return "None";

    }


    public List<Integer> getCountdown() {
        return countdown;
    }

    public String findDifference(String[] time) {
        LocalDateTime fromDateTime = LocalDateTime.now();
        LocalDateTime toDateTime = LocalDateTime.now().withSecond(0);
        String day = time[1];

        int h = convert(time[0])[0];
        int m = convert(time[0])[1];
        int nh = fromDateTime.getHour();
        int nm = fromDateTime.getMinute();

        int laterDate = DayOfWeek.valueOf(day.toUpperCase()).getValue();
        int nowDate = fromDateTime.getDayOfWeek().getValue();

        int dh = nh - h;
        int dm = nm - m;

        if (isPast(time[0], laterDate)) {
            // in past
            // now = 5, later = 1

            int dd = (7 - nowDate) + laterDate;

            toDateTime = toDateTime.plusDays(dd);
            toDateTime = toDateTime.minusHours(dh);
            toDateTime = toDateTime.minusMinutes(dm);
        } else if (nowDate == laterDate) {
            // current day
            toDateTime = toDateTime.minusHours(dh);
            toDateTime = toDateTime.minusMinutes(dm);
        } else {
            // not in past
            int dd = laterDate - nowDate;
            toDateTime = toDateTime.plusDays(dd);
            toDateTime = toDateTime.minusHours(dh);
            toDateTime = toDateTime.minusMinutes(dm);
        }

        LocalDateTime tempDateTime = LocalDateTime.from(fromDateTime);

        long years = tempDateTime.until(toDateTime, ChronoUnit.YEARS);
        tempDateTime = tempDateTime.plusYears(years);

        long months = tempDateTime.until(toDateTime, ChronoUnit.MONTHS);
        tempDateTime = tempDateTime.plusMonths(months);

        long days = tempDateTime.until(toDateTime, ChronoUnit.DAYS);
        tempDateTime = tempDateTime.plusDays(days);

        long hours = tempDateTime.until(toDateTime, ChronoUnit.HOURS);
        tempDateTime = tempDateTime.plusHours(hours);

        long minutes = tempDateTime.until(toDateTime, ChronoUnit.MINUTES);
        tempDateTime = tempDateTime.plusMinutes(minutes);

        long seconds = tempDateTime.until(toDateTime, ChronoUnit.SECONDS);


        StringBuilder message = new StringBuilder();

        if (days > 0) {
            message.append(days + " " + (days > 1 ? "days" : "day") + " ");
        }
        if (hours > 0) {
            message.append(hours + " " + (hours > 1 ? "hours" : "hour") + " ");
        }
        if (minutes > 0) {
            message.append(minutes + " " + (minutes > 1 ? "mins" : "min") + " ");
        }
        if (seconds > 0) {
            message.append(seconds + " " + (seconds > 1 ? "seconds" : "second"));
        }

        return message.toString();
    }


    private boolean isPast(String time, int day) {
        LocalDateTime now = LocalDateTime.now();

        int nhour = now.getHour();
        int nminute = now.getMinute();
        int hour = convert(time)[0];
        int minute = convert(time)[1];

        boolean past = false;

        if (now.getDayOfWeek().getValue() > day) {
            return true;
        } else if (now.getDayOfWeek().getValue() < day) {
            return false;
        }

        if (nhour > hour) {
            past = true;
        } else if (nhour < hour) {
            past = false;
        } else if (nhour == hour) {
            if (nminute == minute) {
                past = true;
            } else if (nminute < minute) {
                past = false;
            } else if (nminute > minute) {
                past = true;
            }
        }
        return past;
    }
}
