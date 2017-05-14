package com.azadmemon.spotifyalarm;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.AbsListView;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private MainActivity context;
    private int groupPosition;

    public TimePickerFragment(MainActivity context, int groupPosition) {
        this.context = context;
        this.groupPosition = groupPosition;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Alarm newAlarm;

        if (groupPosition != -1) {
            newAlarm = context.getAlarms().get(groupPosition);
            newAlarm.setTime(hourOfDay, minute);

            context.getAlarms().set(groupPosition, newAlarm);
            context.expListView.expandGroup(groupPosition);

            context.alarmListAdapter.notifyDataSetChanged();

            context.expListView.expandGroup(groupPosition);
        } else {
            newAlarm = new Alarm(context, hourOfDay, minute, true, new Week(), new Song());

            context.getAlarms().add(newAlarm);
            context.expListView.expandGroup(context.getAlarms().size() - 1);

            context.alarmListAdapter.notifyDataSetChanged();

            context.expListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            context.expListView.expandGroup(context.getAlarms().size() - 1);
            context.expListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
        }
    }
}