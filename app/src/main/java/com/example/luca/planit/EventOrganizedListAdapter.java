package com.example.luca.planit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
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

public class EventOrganizedListAdapter extends ArrayAdapter<ListViewItem> {

    public static final int TYPE_LEFT = 0;
    public static final int TYPE_RIGHT = 1;

    private final List<ListViewItem> dataset;

    public EventOrganizedListAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<ListViewItem> objects) {
        super(context, resource, textViewResourceId, objects);

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
            if (listViewItemType == TYPE_LEFT) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
                TextView textView = (TextView) convertView.findViewById(R.id.textView);
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.list_item_left);
                textView.setBackground(drawable);
                textView.setGravity(Gravity.LEFT);
                textView.setPadding(50, 75, 0, 20);
            } else if (listViewItemType == TYPE_RIGHT) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
                TextView textView = (TextView) convertView.findViewById(R.id.textView);
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.list_item_right);
                textView.setBackground(drawable);
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                textView.setGravity(Gravity.RIGHT);
                textView.setPadding(0, 75, 50, 20);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            viewHolder = new ViewHolder(textView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.getText().setText(listViewItem.getText());

        return convertView;
    }

}
