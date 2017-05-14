package com.azadmemon.spotifyalarm;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;

public class SearchPresenter implements Search.ActionListener {

    private static final String TAG = SearchPresenter.class.getSimpleName();
    public static final int PAGE_SIZE = 20;

    private final Context mContext;
    private final Search.View mView;
    private String mCurrentQuery;
    private SearchType searchType;

    private SearchTracksPager mSearchTracksPager;
    private SearchPlaylistsPager mSearchPlaylistsPager;
    private SearchTracksPager.CompleteListener mSearchTracksListener;
    private SearchPlaylistsPager.CompleteListener mSearchPlaylistsListener;

    public SearchPresenter(Context context, Search.View view, SearchType type) {
        mContext = context;
        mView = view;
        searchType = type;
    }

    @Override
    public void init(String accessToken) {
        logMessage("Api Client created");
        SpotifyApi spotifyApi = new SpotifyApi();

        if (accessToken != null) {
            spotifyApi.setAccessToken(accessToken);
        } else {
            logError("No valid access token");
        }

        mSearchTracksPager = new SearchTracksPager(spotifyApi.getService(), mContext);
        mSearchPlaylistsPager = new SearchPlaylistsPager(spotifyApi.getService(), mContext);
    }


    @Override
    public void search(@Nullable String searchQuery) {
        if (searchQuery != null && !searchQuery.isEmpty()) {
            logMessage("query text submit " + searchQuery);
            mCurrentQuery = searchQuery;
            mView.reset();
            mSearchTracksListener = new SearchTracksPager.CompleteListener() {
                @Override
                public void onComplete(List<Track> items) {
                    mView.addSongs(items);
                }

                @Override
                public void onError(Throwable error) {
                    logError(error.getMessage());

                    RecyclerView rView = (RecyclerView) ((SearchActivity) mContext).findViewById(R.id.search_results);
                    rView.setVisibility(View.GONE);

                    TextView errorView = (TextView) ((SearchActivity) mContext).findViewById(R.id.textView7);
                    errorView.setVisibility(View.VISIBLE);
                    errorView.setText(R.string.search_error);
                }
            };
            mSearchPlaylistsListener = new SearchPlaylistsPager.CompleteListener() {
                @Override
                public void onComplete(List<PlaylistSimple> items) {
                    mView.addPlaylists(items);
                }

                @Override
                public void onError(Throwable error) {
                    RecyclerView rView = (RecyclerView) ((SearchActivity) mContext).findViewById(R.id.search_results);
                    rView.setVisibility(View.GONE);

                    TextView errorView = (TextView) ((SearchActivity) mContext).findViewById(R.id.textView7);
                    errorView.setVisibility(View.VISIBLE);
                    errorView.setText(R.string.search_error);

                    logError(error.getMessage());
                }
            };

            if (searchType.type.equals(SearchType.TYPE_SONG)) {
                mSearchTracksPager.getFirstPage(searchQuery, PAGE_SIZE, mSearchTracksListener);
            } else {
                mSearchPlaylistsPager.getFirstPage(searchQuery, PAGE_SIZE, mSearchPlaylistsListener);
            }
        }
    }

    @Override
    @Nullable
    public String getCurrentQuery() {
        return mCurrentQuery;
    }

    @Override
    public void loadMoreResults() {
        Log.d(TAG, "Load more...");
        if (searchType.type.equals(SearchType.TYPE_SONG)) {
            mSearchTracksPager.getNextPage(mSearchTracksListener);
        } else {
            mSearchPlaylistsPager.getNextPage(mSearchPlaylistsListener);
        }
    }

    @Override
    public void selectTrack(Track item) {
        String previewUrl = item.preview_url;

        if (previewUrl == null) {
            logMessage("Track doesn't have a preview");
        }
    }

    private void logError(String msg) {
        Log.e(TAG, msg);
    }

    private void logMessage(String msg) {
        Log.d(TAG, msg);
    }
}
