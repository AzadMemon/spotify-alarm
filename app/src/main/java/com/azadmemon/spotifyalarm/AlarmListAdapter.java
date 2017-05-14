package com.azadmemon.spotifyalarm;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AlarmListAdapter extends BaseExpandableListAdapter {
    private MainActivity context;
    private Alarms alarms;

    public AlarmListAdapter(MainActivity context, Alarms alarms) {
        this.context = context;
        this.alarms = alarms;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return alarms.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        final Alarm alarm = alarms.get(groupPosition);

        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_alarm, null);
        }

        final TableRow days = (TableRow) convertView.findViewById(R.id.days);
        CheckBox repeat = (CheckBox) convertView.findViewById(R.id.checkBox);
        repeat.setChecked(alarm.isRepeat());

        if (repeat.isChecked()) {
            days.setVisibility(View.VISIBLE);
        } else {
            days.setVisibility(View.GONE);
        }
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    days.setVisibility(View.VISIBLE);
                } else {
                    days.setVisibility(View.GONE);
                }
            }
        });

        View.OnClickListener dayClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToggleButton tb = (ToggleButton) v;
                alarm.updateDayOfWeek((String) tb.getTag(R.string.day_tag), tb.isChecked());
            }
        };

        ToggleButton sunday = (ToggleButton) days.findViewById(R.id.toggleButton);
        sunday.setTag(R.string.day_tag, "sunday");
        sunday.setOnClickListener(dayClickListener);
        sunday.setChecked(alarm.getWeek().isSundayChecked());

        ToggleButton monday = (ToggleButton) days.findViewById(R.id.toggleButton2);
        monday.setTag(R.string.day_tag, "monday");
        monday.setOnClickListener(dayClickListener);
        monday.setChecked(alarm.getWeek().isMondayChecked());

        ToggleButton tuesday = (ToggleButton) days.findViewById(R.id.toggleButton3);
        tuesday.setTag(R.string.day_tag, "tuesday");
        tuesday.setOnClickListener(dayClickListener);
        tuesday.setChecked(alarm.getWeek().isTuesdayChecked());

        ToggleButton wednesday = (ToggleButton) days.findViewById(R.id.toggleButton4);
        wednesday.setTag(R.string.day_tag, "wednesday");
        wednesday.setOnClickListener(dayClickListener);
        wednesday.setChecked(alarm.getWeek().isWednesdayChecked());

        ToggleButton thursday = (ToggleButton) days.findViewById(R.id.toggleButton5);
        thursday.setTag(R.string.day_tag, "thursday");
        thursday.setOnClickListener(dayClickListener);
        thursday.setChecked(alarm.getWeek().isThursdayChecked());

        ToggleButton friday = (ToggleButton) days.findViewById(R.id.toggleButton6);
        friday.setTag(R.string.day_tag, "friday");
        friday.setOnClickListener(dayClickListener);
        friday.setChecked(alarm.getWeek().isFridayChecked());

        ToggleButton saturday = (ToggleButton) days.findViewById(R.id.toggleButton7);
        saturday.setTag(R.string.day_tag, "saturday");
        saturday.setOnClickListener(dayClickListener);
        saturday.setChecked(alarm.getWeek().isSaturdayChecked());

        RelativeLayout up = (RelativeLayout) convertView.findViewById(R.id.lapel);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ExpandableListView) parent).collapseGroup(groupPosition);
            }
        });

        Button delete = (Button) convertView.findViewById(R.id.button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarms.remove(groupPosition);
                notifyDataSetChanged();
            }
        });

        TextView song = (TextView) convertView.findViewById(R.id.textView2);
        if (alarm.getSongName() != null) {
            song.setText(alarm.getSongName());
        } else {
            song.setText(R.string.pick_a_song);
        }

        song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("groupPosition", groupPosition);
                intent.putExtras(bundle);
                context.startActivityForResult(intent, MainActivity.SEARCH_REQUEST_CODE);
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return alarms.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return alarms.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {
        final Alarm alarm = (Alarm) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_alarm, null);
        }

        View divider = convertView.findViewById(R.id.divider);
        TextView days = (TextView) convertView.findViewById(R.id.textView4);
        TextView down = (TextView) convertView.findViewById(R.id.textView5);
        TextView time = (TextView) convertView.findViewById(R.id.textView);
        time.setTypeface(null, Typeface.BOLD);
        time.setText(alarm.getTime());

        Switch sw = (Switch) convertView.findViewById(R.id.switch2);
        if (alarm.isEnabled()) {
            sw.setChecked(true);
        } else {
            sw.setChecked(false);
        }
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch sw = (Switch) v;
                if (sw.isChecked()) {
                    alarm.setIsOn(true);
                } else {
                    alarm.setIsOn(false);
                }
            }
        });

        TextView partOfDay = (TextView) convertView.findViewById(R.id.textView3);
        partOfDay.setText(alarm.getPartOfDay());

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ExpandableListView) parent).expandGroup(groupPosition);
                DialogFragment newFragment = new TimePickerFragment(context, groupPosition);
                newFragment.show(context.getFragmentManager(), "timePicker");
            }
        });

        days.setText(alarm.getWeek().getLabel());

        if (isExpanded) {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            divider.setVisibility(View.GONE);
            days.setVisibility(View.GONE);
            down.setVisibility(View.GONE);
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTransparent));
            divider.setVisibility(View.VISIBLE);
            days.setVisibility(View.VISIBLE);
            down.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}