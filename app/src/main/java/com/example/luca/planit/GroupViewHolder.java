package com.example.luca.planit;

import android.widget.TextView;

/**
 * Created by Luca on 24/07/2017.
 */

public class GroupViewHolder {
    private final TextView eventName;


    public GroupViewHolder(TextView eventName) {
        this.eventName = eventName;
    }

    public TextView getEventName() {
        return eventName;
    }
}
