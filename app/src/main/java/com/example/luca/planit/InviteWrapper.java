package com.example.luca.planit;

/**
 * Created by diego on 24/07/2017.
 */
public class InviteWrapper {
    private final String eventId;
    private final String guestId;
    private final GuestState guestState;

    public InviteWrapper(String eventId, String guestId, GuestState guestState) {
        super();
        this.eventId = eventId;
        this.guestId = guestId;
        this.guestState = guestState;
    }


    public GuestState getGuestState() {
        return guestState;
    }


    public String getEventId() {
        return eventId;
    }

    public String getGuestId() {
        return guestId;
    }
}