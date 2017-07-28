package com.example.luca.planit;

import android.widget.TextView;

/**
 * Created by Luca on 24/07/2017.
 */

public class DateSuggestionViewHolder {
    private TextView dateText;
    private TextView suggestionNumberText;

    public DateSuggestionViewHolder(TextView dateText, TextView suggestionNumberText) {
        this.dateText = dateText;
        this.suggestionNumberText = suggestionNumberText;
    }

    public TextView getDateText() {
        return dateText;
    }

    public TextView getSuggestionNumberText() {
        return suggestionNumberText;
    }
}
