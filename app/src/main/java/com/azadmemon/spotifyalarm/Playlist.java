package com.azadmemon.spotifyalarm;


import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.PlaylistSimple;

public class Playlist {
    public final String name;
    public final String owner;
    public final Image image;

    public Playlist(PlaylistSimple playlist) {
        this.name = playlist.name;
        this.owner = playlist.owner.id != null ? playlist.owner.id : playlist.owner.display_name;
        Image smallestImage = new Image();
        for (Image image : playlist.images) {
            if (smallestImage.width != null && image.width != null && image.width < smallestImage.width) {
                smallestImage = image;
            }
        }
        this.image = smallestImage;
    }
}
