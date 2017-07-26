package com.example.luca.planit;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Luca on 23/07/2017.
 */

public class GroupListAdapter extends ArrayAdapter<ListGroupItem> {

    private final List<ListGroupItem> dataset;

    public GroupListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ListGroupItem> objects) {
        super(context, resource, objects);

        dataset = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GroupViewHolder viewHolder = null;
        ListGroupItem listViewItem = dataset.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_group_item,parent,false);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        TextView textView = (TextView) convertView.findViewById(R.id.group_text_view);
        viewHolder = new GroupViewHolder(textView);
        textView.setText(listViewItem.getGroupName());

        return convertView;
    }

}
