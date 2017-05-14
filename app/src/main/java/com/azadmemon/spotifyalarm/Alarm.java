package com.azadmemon.spotifyalarm;

import android.content.Context;

public class Alarm {
    private int hourOfDay;
    private int minute;
    private boolean isEnabled;
    private Song song;
    private Week week;
    private Context context;

    public Alarm(Context context, int hourOfDay, int minute, boolean isEnabled, Week week, Song song) {
        this.context = context;
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.isEnabled = isEnabled;
        this.week = week;
        this.song = song;
        ((MainActivity)this.context).getAlarms().notifyAndSave();
    }

    public void setTime(int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        ((MainActivity)this.context).getAlarms().notifyAndSave();
    }

    public void setIsOn(boolean isEnabled) {
        this.isEnabled = isEnabled;
        ((MainActivity)this.context).getAlarms().notifyAndSave();
    }

    public void updateDayOfWeek(String day, boolean isChecked) {
        this.week.updateDayOfWeek(day, isChecked);
        ((MainActivity)this.context).getAlarms().notifyAndSave();
    }

    public void setSong(String name, String uri) {
        this.song = new Song(name, uri);
        ((MainActivity)this.context).getAlarms().notifyAndSave();
    }

    public boolean isRepeat() {
        return week.isRepeat();
    }

    public String getSongName() {
        return this.song.getName();
    }

    public Week getWeek() {
        return this.week;
    }

    public String getTime() {
        int twelveHourOfDay = hourOfDay > 12 ? hourOfDay - 12 : hourOfDay;
        return new StringBuilder().append(twelveHourOfDay).append(":").append(String.format("%02d", minute)).toString();
    }

    public Boolean isEnabled() {
        return this.isEnabled;
    }

    public String getPartOfDay() {
        return hourOfDay > 12? "PM" : "AM";
    }
}

class Week {
    private boolean sunday = false;
    private boolean monday = false;
    private boolean tuesday = false;
    private boolean wednesday = false;
    private boolean thursday = false;
    private boolean friday = false;
    private boolean saturday = false;

    public Week() {

    }

    public boolean isSundayChecked() {
        return sunday;
    }

    public boolean isSaturdayChecked() {
        return saturday;
    }

    public boolean isMondayChecked() {
        return monday;
    }

    public boolean isTuesdayChecked() {
        return tuesday;
    }

    public boolean isWednesdayChecked() {
        return wednesday;
    }

    public boolean isThursdayChecked() {
        return thursday;
    }

    public boolean isFridayChecked() {
        return friday;
    }

    public void updateDayOfWeek(String day, boolean isChecked) {
        switch (day) {
            case "sunday":
                this.sunday = isChecked;
                break;
            case "monday":
                this.monday = isChecked;
                break;
            case "tuesday":
                this.tuesday = isChecked;
                break;
            case "wednesday":
                this.wednesday = isChecked;
                break;
            case "thursday":
                this.thursday = isChecked;
                break;
            case "friday":
                this.friday = isChecked;
                break;
            case "saturday":
                this.saturday = isChecked;
                break;
        }
    }

    public String getLabel() {
        String[] label = new String[7];

        if (this.sunday) {
            label[0] = "Sun";
        }
        if (this.monday) {
            label[1] = "Mon";
        }
        if (this.tuesday) {
            label[2] = "Tue";
        }
        if (this.wednesday) {
            label[3] = "Wed";
        }
        if (this.thursday) {
            label[4] = "Thu";
        }
        if (this.friday) {
            label[5] = "Fri";
        }
        if (this.saturday) {
            label[6] = "Sat";
        }

        StringBuilder builder = new StringBuilder();
        for (String s : label) {
            if (s != null) {
                builder.append(s);
                builder.append(", ");
            }
        }

        if (builder.lastIndexOf(", ") != -1) {
            builder.delete(builder.lastIndexOf(", "), builder.length());
        }

        return builder.toString();
    }

    public boolean isRepeat() {
        return this.sunday || this.monday || this.tuesday || this.wednesday || this.thursday || this.friday || this.saturday;
    }
}

class Song {
    private String name;
    private String uri;

    public Song() {

    }

    public Song(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return this.name;
    }

    public String getUri() {
        return this.uri;
    }
}