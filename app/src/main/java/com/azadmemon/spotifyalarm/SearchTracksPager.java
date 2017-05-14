package com.azadmemon.spotifyalarm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.client.Response;

public class SearchTracksPager {

    private final SpotifyService mSpotifyApi;
    private int mCurrentOffset;
    private int mPageSize;
    private String mCurrentQuery;
    private Context mContext;

    public interface CompleteListener {
        void onComplete(List<Track> items);

        void onError(Throwable error);
    }

    public SearchTracksPager(SpotifyService spotifyApi, Context context) {
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

    private void getData(String query, final int offset, final int limit, final CompleteListener listener) {
        showSpinner();

        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.OFFSET, offset);
        options.put(SpotifyService.LIMIT, limit);

        mSpotifyApi.searchTracks(query, options, new SpotifyCallback<TracksPager>() {
            @Override
            public void success(TracksPager tracksPager, Response response) {
                TextView errorView = (TextView) ((SearchActivity) mContext).findViewById(R.id.textView7);
                RecyclerView rView = (RecyclerView) ((SearchActivity) mContext).findViewById(R.id.search_results);

                if (offset == 0 && (tracksPager.tracks.items == null || tracksPager.tracks.items.size() == 0)) {
                    errorView.setVisibility(View.VISIBLE);
                    errorView.setText(R.string.no_results);
                    rView.setVisibility(View.GONE);
                } else {
                    errorView.setVisibility(View.GONE);
                    rView.setVisibility(View.VISIBLE);
                    listener.onComplete(tracksPager.tracks.items);
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
