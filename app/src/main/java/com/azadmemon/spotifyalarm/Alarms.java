package com.azadmemon.spotifyalarm;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Alarms {
    private List<Alarm> alarms;
    private Context context;

    public Alarms(Context context, List<Alarm> alarms) {
        this.alarms = alarms;
        this.context = context;
    }

    public void add(Alarm alarm) {
        alarms.add(alarm);

        save();
        notifyDataSetChanged();
    }

    public void set(int index, Alarm alarm) {
        alarms.set(index, alarm);

        save();
        notifyDataSetChanged();
    }

    public void remove(int index) {
        alarms.remove(index);

        save();
        notifyDataSetChanged();
    }

    public void save() {
        SharedPreferences mPrefs = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this);
        prefsEditor.putString("alarms", json);
        prefsEditor.apply();
    }

    public void notifyAndSave() {
        notifyDataSetChanged();
        save();
    }

    public Alarm get(int index) {
        return this.alarms.get(index);
    }

    public int size() {
        return alarms.size();
    }

    private void notifyDataSetChanged() {
        ((MainActivity)context).alarmListAdapter.notifyDataSetChanged();
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
