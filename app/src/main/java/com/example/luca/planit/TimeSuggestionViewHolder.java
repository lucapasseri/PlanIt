package com.example.luca.planit;

import android.widget.TextView;

/**
 * Created by Luca on 24/07/2017.
 */

public class TimeSuggestionViewHolder {
    private TextView timeText;
    private TextView suggestionNumberText;

    public TimeSuggestionViewHolder(TextView timeText, TextView suggestionNumberText) {
        this.timeText = timeText;
        this.suggestionNumberText = suggestionNumberText;
    }

    public TextView getTimeText() {
        return timeText;
    }

    public TextView getSuggestionNumberText() {
        return suggestionNumberText;
    }
}
