package com.azadmemon.spotifyalarm;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

// TODO:
// - First screen should prompt user to login
// - Once logged in, screen should show all alarms (or a message that no alarms exist) + a button to create alarms
// - When you click create alarm, a new activity will appear
//   - In this activity you can select date/time/repeating
//   - You can also click add song which will initiate yet another "searc" activity
// - In the search activity you'll be able to select your own playlists, as well as artists/songs/albums
// - Should have a fallback if there are any errors to play the system alarm sound
// - Screen should wake up when alarm rings
// - Add an advertisement at the bottom when setting the alarm
// - Add an advertisement when screen wakes up for alarm
public class MainActivity extends AppCompatActivity {

    private List<String> groupList = new ArrayList<String>();
    private ExpandableListView expListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        groupList.add("7:00");
        groupList.add("8:00");

        expListView = (ExpandableListView) findViewById(R.id.alarm_list);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(this, groupList);
        expListView.setAdapter(expListAdapter);

        Display newDisplay = getWindowManager().getDefaultDisplay();
        int width = newDisplay.getWidth();
        expListView.setIndicatorBounds(width-50, width);
    }
}
