package com.example.luca.planit;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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

public class EventTakePartListAdapter extends ArrayAdapter<ListViewItem> {

    private final List<ListViewItem> dataset;

    public EventTakePartListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ListViewItem> objects) {
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
            if (listViewItemType == EventOrganizedListAdapter.TYPE_LEFT) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
                TextView textView = (TextView) convertView.findViewById(R.id.textView);
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.list_item_left);
                textView.setBackground(drawable);

                TextView organizerText = (TextView) convertView.findViewById(R.id.organizer_name);
                Drawable organizerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.list_item_organizer_left);
                organizerText.setBackground(organizerDrawable);

            } else if (listViewItemType == EventOrganizedListAdapter.TYPE_RIGHT) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
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

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String initialNameLetter = listViewItem.getOrganizerName().substring(0, 1);
        String initialSurnameLetter = listViewItem.getOrganizerSurname().substring(0, 1);

        viewHolder.getEventNameventName().setText(listViewItem.getEventName());
        viewHolder.getOrganizerText().setText(initialNameLetter + initialSurnameLetter);

        viewHolder.getOrganizerText().setBackgroundResource(R.drawable.list_item_organizer_left);

        StateListDrawable drawable = (StateListDrawable)  viewHolder.getOrganizerText().getBackground();
        drawable.setColorFilter(ContextCompat.getColor(getContext(),listViewItem.getColor()), PorterDuff.Mode.SRC_ATOP);
        //drawable.setColor(ContextCompat.getColor(getContext(),R.color.alice_blue));

        return convertView;
    }

}
