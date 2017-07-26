package com.example.luca.planit;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Luca on 23/07/2017.
 */

public class EventListAdapter extends ArrayAdapter<ListViewItem> {

    public static final int TYPE_LEFT = 0;
    public static final int TYPE_RIGHT = 1;

    private final List<ListViewItem> dataset;

    public EventListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ListViewItem> objects) {
        super(context, resource, objects);

        dataset = objects;
    }

    @Override
    public int getItemViewType(int position) {
        return position%2==0 ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        ListViewItem listViewItem = dataset.get(position);
        int listViewItemType = getItemViewType(position);

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

            if (listViewItemType == TYPE_LEFT) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
                TextView textView = (TextView) convertView.findViewById(R.id.textView);
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.list_item_left);
                textView.setBackground(drawable);

                TextView organizerText = (TextView) convertView.findViewById(R.id.organizer_name);
                Drawable organizerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.list_item_organizer_left);
                organizerText.setBackground(organizerDrawable);

            } else if (listViewItemType == TYPE_RIGHT) {

                TextView textView = (TextView) convertView.findViewById(R.id.textView);
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.list_item_right);
                textView.setBackground(drawable);

                TextView organizerText = (TextView) convertView.findViewById(R.id.organizer_name);
                Drawable organizerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.list_item_organizer_right);
                organizerText.setBackground(organizerDrawable);

            }

            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            TextView organizerText = (TextView) convertView.findViewById(R.id.organizer_name);
            viewHolder = new ViewHolder(textView, organizerText);

            String initialNameLetter = listViewItem.getEvent().getEventInfo().getOrganizer().getOrganizerName().substring(0, 1);
            String initialSurnameLetter = listViewItem.getEvent().getEventInfo().getOrganizer().getOrganizerSurname().substring(0, 1);

            viewHolder.getEventNameventName().setText(listViewItem.getEvent().getEventInfo().getNameEvent());
            viewHolder.getOrganizerText().setText(initialNameLetter + initialSurnameLetter);

            StateListDrawable drawable = (StateListDrawable)  viewHolder.getOrganizerText().getBackground();
            drawable.setColorFilter(ContextCompat.getColor(getContext(),listViewItem.getColor()), PorterDuff.Mode.SRC_ATOP);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

}
