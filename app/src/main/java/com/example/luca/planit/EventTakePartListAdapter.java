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

public class EventTakePartListAdapter extends ArrayAdapter<ListViewItem> {

    private final List<ListViewItem> dataset;

    public EventTakePartListAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<ListViewItem> objects) {
        super(context, resource, textViewResourceId, objects);

        dataset = objects;
    }

    @Override
    public int getItemViewType(int position) {
        return position%2==0 ? 1 : 0;
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
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                textView.setPadding(50, 75, 0, 20);
            } else if (listViewItemType == EventOrganizedListAdapter.TYPE_RIGHT) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
                TextView textView = (TextView) convertView.findViewById(R.id.textView);
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.list_item_right);
                textView.setBackground(drawable);
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                textView.setGravity(Gravity.CENTER);
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
