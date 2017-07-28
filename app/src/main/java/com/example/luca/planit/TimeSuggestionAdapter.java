package com.example.luca.planit;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Luca on 23/07/2017.
 */

public class TimeSuggestionAdapter extends ArrayAdapter<TimePreference> {

    private final List<TimePreference> dataset;

    public TimeSuggestionAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<TimePreference> objects) {
        super(context, resource, objects);

        dataset = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TimeSuggestionViewHolder viewHolder = null;
        TimePreference timePreference = dataset.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_time_item, parent, false);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (TimeSuggestionViewHolder) convertView.getTag();
        }

        TextView dateText = (TextView) convertView.findViewById(R.id.time_text);
        TextView numberProposalText = (TextView) convertView.findViewById(R.id.number_proposal);

        viewHolder = new TimeSuggestionViewHolder(dateText, numberProposalText);

        String date = timePreference.getTime();
        int numberProposal = timePreference.getNumPreferences();

        viewHolder.getTimeText().setText(date);
        viewHolder.getSuggestionNumberText().setText(String.valueOf(numberProposal));

        return convertView;
    }

}
