package com.azadmemon.spotifyalarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.RadioGroup;

import java.util.List;

import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;

public class SearchActivity extends AppCompatActivity implements Search.View {
    private static final String KEY_CURRENT_QUERY = "CURRENT_QUERY";

    private Search.ActionListener mActionListener;
    private SearchResultsAdapter resultsAdapter;

    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    private ScrollListener mScrollListener = new ScrollListener(mLayoutManager);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_music);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        final SearchView searchView = (SearchView) myToolbar.findViewById(R.id.action_search);

        String token = this.getSharedPreferences(getString(R.string.preference_file_name), MODE_PRIVATE).getString("TOKEN", "token");

        RadioGroup rGroup = (RadioGroup)findViewById(R.id.radio_group);
        final SearchType sType = new SearchType(SearchType.TYPE_SONG);

        mActionListener = new SearchPresenter(this, this, sType);
        mActionListener.init(token);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mActionListener.search(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        resultsAdapter = new SearchResultsAdapter(this, new SearchResultsAdapter.ItemSelectedListener() {
            @Override
            public void onItemSelected(View itemView, String uri, String name) {
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("uri", uri);
                bundle.putInt("groupPosition", getIntent().getIntExtra("groupPosition" ,-1));
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        RecyclerView resultsList = (RecyclerView) findViewById(R.id.search_results);
        resultsList.setHasFixedSize(true);
        resultsList.setLayoutManager(mLayoutManager);
        resultsList.setAdapter(resultsAdapter);
        resultsList.addOnScrollListener(mScrollListener);

        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                resultsAdapter.clearData();
                switch(checkedId){
                    case R.id.radio1:
                        sType.setType(SearchType.TYPE_SONG);
                        break;
                    case R.id.radio2:
                        sType.setType(SearchType.TYPE_PLAYLIST);
                        break;
                }
            }
        });
        // If Activity was recreated wit active search restore it
        if (savedInstanceState != null) {
            String currentQuery = savedInstanceState.getString(KEY_CURRENT_QUERY);
            mActionListener.search(currentQuery);
        }
    }

    private class ScrollListener extends ResultListScrollListener {
        public ScrollListener(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public void onLoadMore() {
            mActionListener.loadMoreResults();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        SearchView searchView = (SearchView) myToolbar.findViewById(R.id.action_search);
        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    public void reset() {
        mScrollListener.reset();
        resultsAdapter.clearData();
    }

    @Override
    public void addSongs(List<Track> items) {
        resultsAdapter.addSongs(items);
    }

    @Override
    public void addPlaylists(List<PlaylistSimple> items) {
        resultsAdapter.addPlaylists(items);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActionListener.getCurrentQuery() != null) {
            outState.putString(KEY_CURRENT_QUERY, mActionListener.getCurrentQuery());
        }
    }
}
