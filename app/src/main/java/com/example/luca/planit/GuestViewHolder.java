package com.example.luca.planit;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Luca on 27/07/2017.
 */

public class GuestViewHolder {

    private final TextView guestNameText;
    private final TextView guestSurnameText;
    private final TextView guestUsernameText;
    private final ImageView guestAnswerImage;

    public GuestViewHolder(TextView guestNameText, TextView guestSurnameText, TextView guestUsernameText,
                           ImageView guestAnswerImage) {
        this.guestNameText = guestNameText;
        this.guestSurnameText = guestSurnameText;
        this.guestUsernameText = guestUsernameText;
        this.guestAnswerImage = guestAnswerImage;
    }

    public TextView getGuestNameText() {
        return guestNameText;
    }

    public TextView getGuestSurnameText() {
        return guestSurnameText;
    }

    public TextView getGuestUsernameText() {
        return guestUsernameText;
    }

    public ImageView getGuestAnswerImage() {
        return guestAnswerImage;
    }
}
