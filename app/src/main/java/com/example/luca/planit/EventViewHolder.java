package com.example.luca.planit;

import android.widget.TextView;

/**
 * Created by Luca on 24/07/2017.
 */

public class EventViewHolder {
    private TextView eventName;
    private TextView organizerText;

    public EventViewHolder(TextView eventName, TextView organizerText) {
        this.eventName = eventName;
        this.organizerText = organizerText;
    }

    public TextView getEventNameventName() {
        return eventName;
    }

    public TextView getOrganizerText() {
        return organizerText;
    }


}
