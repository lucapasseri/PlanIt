package com.example.luca.planit;

/**
 * Created by diego on 24/07/2017.
 */

public class InviteWrapper {
    private final String eventId;
    private final String guestId;

    public InviteWrapper(String eventId, String guestId) {
        this.eventId = eventId;
        this.guestId = guestId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getGuestId() {
        return guestId;
    }
}

