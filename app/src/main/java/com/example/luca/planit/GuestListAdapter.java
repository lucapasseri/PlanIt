package com.example.luca.planit;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Luca on 23/07/2017.
 */

public class GuestListAdapter extends ArrayAdapter<GuestInEvent> {

    private final List<GuestInEvent> dataset;

    public GuestListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<GuestInEvent> objects) {
        super(context, resource, objects);

        dataset = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GuestViewHolder viewHolder = null;
        GuestInEvent listViewItem = dataset.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_guest_item,parent,false);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (GuestViewHolder) convertView.getTag();
        }

        TextView guestNameText = (TextView) convertView.findViewById(R.id.invite_name_text);
        TextView guestSurnameText = (TextView) convertView.findViewById(R.id.invite_surname_text);
        TextView guestUsernameText = (TextView) convertView.findViewById(R.id.invite_username_text);
        ImageView guestAnswerImage = (ImageView) convertView.findViewById(R.id.image_answer);

        viewHolder = new GuestViewHolder(guestNameText, guestSurnameText, guestUsernameText, guestAnswerImage);

        viewHolder.getGuestNameText().setText(dataset.get(position).getGuestName());
        viewHolder.getGuestSurnameText().setText(dataset.get(position).getGuestSurname());
        viewHolder.getGuestUsernameText().setText(dataset.get(position).getGuestUsername());

        GuestState guestState = dataset.get(position).getGuestState();
        if (guestState == GuestState.CONFIRMED) {
            viewHolder.getGuestAnswerImage().setImageDrawable(ContextCompat.getDrawable(
                    getContext(), R.drawable.success));
        } else if (guestState == GuestState.DECLINED) {
            viewHolder.getGuestAnswerImage().setImageDrawable(ContextCompat.getDrawable(
                    getContext(), R.drawable.error));
        } else if (guestState == GuestState.NOT_CONFIRMED) {
            viewHolder.getGuestAnswerImage().setImageDrawable(ContextCompat.getDrawable(
                    getContext(), R.drawable.ic_not_confirmed_64dp));
        }


        return convertView;
    }

}
