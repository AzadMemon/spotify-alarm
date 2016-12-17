package com.azadmemon.spotifyalarm;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private List<String> alarms;

    public ExpandableListAdapter(Activity context, List<String> alarms) {
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
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_alarm, null);
        }

        final TableRow days = (TableRow) convertView.findViewById(R.id.days);
        CheckBox repeat = (CheckBox) convertView.findViewById(R.id.checkBox);

        if (repeat.isChecked()) {
            days.setVisibility(View.VISIBLE);
        } else {
            days.setVisibility(View.GONE);
        }
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    days.setVisibility(View.VISIBLE);
                } else {
                    days.setVisibility(View.GONE);
                }
            }
        });

        RelativeLayout up = (RelativeLayout) convertView.findViewById(R.id.lapel);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ExpandableListView)parent).collapseGroup(groupPosition);
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
        String alarmTime = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_alarm, null);
        }

        View divider = convertView.findViewById(R.id.divider);
        TextView days = (TextView) convertView.findViewById(R.id.textView4);
        TextView down = (TextView) convertView.findViewById(R.id.textView5);
        TextView item = (TextView) convertView.findViewById(R.id.textView);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(alarmTime);

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