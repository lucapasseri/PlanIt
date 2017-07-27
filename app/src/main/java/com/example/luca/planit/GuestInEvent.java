package com.example.luca.planit;

/**
 * Created by Luca on 27/07/2017.
 */

public class GuestInEvent extends InviteWrapper {

    private final String guestName;
    private final String guestSurname;
    private final String guestUsername;


    public GuestInEvent(String eventId, String guestId, GuestState guestState, String eventName,
                        String guestName, String guestSurname, String guestUsername) {

        super(eventId, guestId, guestState, eventName);
        this.guestName = guestName;
        this.guestSurname = guestSurname;
        this.guestUsername = guestUsername;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getGuestSurname() {
        return guestSurname;
    }

    public String getGuestUsername() {
        return guestUsername;
    }

    @Override
    public String toString() {
        return "GuestInEvent{" +
                "guestName='" + guestName + '\'' +
                ", guestSurname='" + guestSurname + '\'' +
                ", guestUsername='" + guestUsername + '\'' +
                '}'+super.toString();
    }
}
