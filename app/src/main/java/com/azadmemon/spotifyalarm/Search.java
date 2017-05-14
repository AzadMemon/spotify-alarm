package com.azadmemon.spotifyalarm;

import java.util.List;

import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;

public class Search {

    public interface View {
        void reset();

        void addSongs(List<Track> items);

        void addPlaylists(List<PlaylistSimple> items);
    }

    public interface ActionListener {

        void init(String token);

        String getCurrentQuery();

        void search(String searchQuery);

        void loadMoreResults();

        void selectTrack(Track item);

    }
}
