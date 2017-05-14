package com.azadmemon.spotifyalarm;

public class SearchType {
    public static final String TYPE_SONG = "SONG";
    public static final String TYPE_PLAYLIST = "PLAYLIST";
    public String type;


    public SearchType(String type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
