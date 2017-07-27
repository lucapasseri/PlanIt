package com.example.luca.planit;

/**
 * Created by diego on 24/07/2017.
 */
public class InviteWrapper {
    private final String eventId;
    private final String guestId;
    private final String eventName;
    private final GuestState guestState;

    public InviteWrapper(String eventId, String guestId, GuestState guestState,String eventName) {
        super();
        this.eventId = eventId;
        this.guestId = guestId;
        this.guestState = guestState;
        this.eventName = eventName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InviteWrapper that = (InviteWrapper) o;

        if (!eventId.equals(that.eventId)) return false;
        return guestId.equals(that.guestId);

    }

    public String getEventName() {
        return eventName;
    }

    @Override
    public int hashCode() {
        int result = eventId.hashCode();
        result = 31 * result + guestId.hashCode();
        return result;
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