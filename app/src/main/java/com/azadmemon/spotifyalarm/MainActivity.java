package com.azadmemon.spotifyalarm;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;

import java.util.ArrayList;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class MainActivity extends AppCompatActivity implements ConnectionStateCallback {

    private static final String CLIENT_ID = "c060c45b321f4fe1971d0c61d6ede6d8";
    private static final String REDIRECT_URI = "spotify-alarm-login://callback";
    public static final int SEARCH_REQUEST_CODE = 1234;

    private Alarms alarms;
    public AlarmListAdapter alarmListAdapter;
    public ExpandableListView expListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences(getString(R.string.preference_file_name), MODE_PRIVATE);
        String json = mPrefs.getString("alarms", "");
        alarms = gson.fromJson(json, Alarms.class);

        if (alarms == null) {
            alarms = new Alarms(this, new ArrayList<Alarm>());
        }

        alarms.setContext(this);

        alarmListAdapter = new AlarmListAdapter(this, alarms);
        expListView = (ExpandableListView) findViewById(R.id.alarm_list);
        expListView.setAdapter(alarmListAdapter);

        Display newDisplay = getWindowManager().getDefaultDisplay();
        int width = newDisplay.getWidth();
        expListView.setIndicatorBounds(width - 50, width);

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousItem)
                    expListView.collapseGroup(previousItem);

                previousItem = groupPosition;
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_alarm:
                DialogFragment newFragment = new TimePickerFragment(this, -1);
                newFragment.show(getFragmentManager(), "timePickerAdd");
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                this.getSharedPreferences(getString(R.string.preference_file_name), MODE_PRIVATE).edit().putString("TOKEN", response.getAccessToken()).apply();
            }
        } else if (requestCode == SEARCH_REQUEST_CODE) {
            if (intent != null) {
                int index = intent.getIntExtra("groupPosition", -1);
                alarms.get(index).setSong(intent.getStringExtra("name"), intent.getStringExtra("uri"));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        alarms.save();
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(com.spotify.sdk.android.player.Error error) {
        // TODO: Handle error
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        // TODO: Handle error
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    public Alarms getAlarms() {
        return alarms;
    }
}

