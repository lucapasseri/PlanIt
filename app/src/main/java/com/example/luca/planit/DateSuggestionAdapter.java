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

public class DateSuggestionAdapter extends ArrayAdapter<DatePreference> {

    private final List<DatePreference> dataset;

    public DateSuggestionAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<DatePreference> objects) {
        super(context, resource, objects);

        dataset = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DateSuggestionViewHolder viewHolder = null;
        DatePreference datePreference = dataset.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_date_item, parent, false);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (DateSuggestionViewHolder) convertView.getTag();
        }

        TextView dateText = (TextView) convertView.findViewById(R.id.date_text);
        TextView numberProposalText = (TextView) convertView.findViewById(R.id.number_proposal);

        viewHolder = new DateSuggestionViewHolder(dateText, numberProposalText);

        String date = datePreference.getDate();
        int numberProposal = datePreference.getNumPreferences();

        viewHolder.getDateText().setText(date);
        viewHolder.getSuggestionNumberText().setText(String.valueOf(numberProposal));

        return convertView;
    }

}
