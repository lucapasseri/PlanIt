package com.example.luca.planit;

import android.widget.TextView;

/**
 * Created by Luca on 24/07/2017.
 */

public class ViewHolder {
    TextView text;

    public ViewHolder(TextView text) {
        this.text = text;
    }

    public TextView getText() {
        return text;
    }

    public void setText(TextView text) {
        this.text = text;
    }

}
