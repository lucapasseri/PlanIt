package com.example.luca.planit;

import android.widget.TextView;

/**
 * Created by Diego on 24/07/2017.
 */

public class InviteViewHolder {
    private final TextView eventName;


    public InviteViewHolder(TextView eventName) {
        this.eventName = eventName;
    }

    public TextView getEventName() {
        return eventName;
    }
}
