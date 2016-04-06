package com.zyuma.remindme;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ScheduleAdapter extends ArrayAdapter<CustomNotification> {


    private final Context context;
    private final ArrayList<CustomNotification> itemsArrayList;
    private final Typeface raleway_italian;
    private final Typeface raleway_regular;

    public ScheduleAdapter(Context context, ArrayList<CustomNotification> itemsArrayList) {

        super(context, R.layout.row_schedule, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
        raleway_regular = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Regular.ttf");
        raleway_italian = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Italic.ttf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row_schedule, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.reminder);
        labelView.setTypeface(raleway_italian);
        TextView valueView = (TextView) rowView.findViewById(R.id.reminder_time);
        valueView.setTypeface(raleway_regular);

        // 4. Set the text for textView
        labelView.setText(itemsArrayList.get(position).getReminder());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        Date resultTime = new Date(itemsArrayList.get(position).getReminderTime());
        if (sdf.format(resultTime) == null){
            Log.i("ADAPTER", "null");
            return rowView;
    }
        valueView.setText(sdf.format(resultTime));

        return rowView;
    }
}