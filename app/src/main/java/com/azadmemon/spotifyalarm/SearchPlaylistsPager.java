package com.azadmemon.spotifyalarm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistsPager;
import retrofit.client.Response;

public class SearchPlaylistsPager {

    private final SpotifyService mSpotifyApi;
    private int mCurrentOffset;
    private int mPageSize;
    private String mCurrentQuery;
    private Context mContext;

    public interface CompleteListener {
        void onComplete(List<PlaylistSimple> items);

        void onError(Throwable error);
    }

    public SearchPlaylistsPager(SpotifyService spotifyApi, Context context) {
        mSpotifyApi = spotifyApi;
        mContext = context;
    }

    public void getFirstPage(String query, int pageSize, CompleteListener listener) {
        mCurrentOffset = 0;
        mPageSize = pageSize;
        mCurrentQuery = query;
        getData(query, 0, pageSize, listener);
    }

    public void getNextPage(CompleteListener listener) {
        mCurrentOffset += mPageSize;
        getData(mCurrentQuery, mCurrentOffset, mPageSize, listener);
    }

    private void getData(final String query, final int offset, final int limit, final CompleteListener listener) {
        if (query == null) {
            return;
        }

        showSpinner();

        final TextView errorView = (TextView) ((SearchActivity) mContext).findViewById(R.id.textView7);
        final RecyclerView rView = (RecyclerView) ((SearchActivity) mContext).findViewById(R.id.search_results);
        mSpotifyApi.getMyPlaylists(new SpotifyCallback<Pager<PlaylistSimple>>() {
            @Override
            public void success(Pager<PlaylistSimple> playListsPager, Response response) {
                final TextView errorView = (TextView) ((SearchActivity) mContext).findViewById(R.id.textView7);
                final RecyclerView rView = (RecyclerView) ((SearchActivity) mContext).findViewById(R.id.search_results);

                errorView.setVisibility(View.GONE);
                rView.setVisibility(View.VISIBLE);

                final List<PlaylistSimple> filteredPlaylists = new ArrayList<PlaylistSimple>();

                for (PlaylistSimple playlist : playListsPager.items) {
                    if (query != null && playlist != null && playlist.name != null && playlist.name.contains(query)) {
                        filteredPlaylists.add(playlist);
                    }
                }

                getOtherPlaylists(query, offset, limit, errorView, rView, filteredPlaylists, listener);
            }

            @Override
            public void failure(SpotifyError error) {
                listener.onError(error);
                getOtherPlaylists(query, offset, limit, errorView, rView, new ArrayList<PlaylistSimple>(), listener);
            }
        });
    }

    private void getOtherPlaylists(final String query, final int offset, final int limit, final TextView errorView, final RecyclerView rView, final List<PlaylistSimple> userPlaylists, final CompleteListener listener) {
        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.OFFSET, offset);
        options.put(SpotifyService.LIMIT, limit);

        mSpotifyApi.searchPlaylists(query, options, new SpotifyCallback<PlaylistsPager>() {
            @Override
            public void success(PlaylistsPager playListsPager, Response response) {
                if (offset == 0 && (playListsPager.playlists.items == null || playListsPager.playlists.items.size() == 0) && userPlaylists.size() == 0) {
                    errorView.setVisibility(View.VISIBLE);
                    errorView.setText(R.string.no_results);
                    rView.setVisibility(View.GONE);
                } else {
                    errorView.setVisibility(View.GONE);
                    rView.setVisibility(View.VISIBLE);

                    userPlaylists.addAll(playListsPager.playlists.items);
                    listener.onComplete(userPlaylists);
                }

                hideSpinner();
            }

            @Override
            public void failure(SpotifyError error) {
                hideSpinner();
                listener.onError(error);
            }
        });
    }

    private void hideSpinner(){
        RecyclerView rView = (RecyclerView) ((SearchActivity) mContext).findViewById(R.id.search_results);
        rView.setVisibility(View.VISIBLE);

        ProgressBar spinner = (ProgressBar) ((SearchActivity) mContext).findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);
    }

    private void showSpinner() {
        RecyclerView rView = (RecyclerView) ((SearchActivity) mContext).findViewById(R.id.search_results);
        rView.setVisibility(View.GONE);

        ProgressBar spinner = (ProgressBar) ((SearchActivity) mContext).findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
    }
}
