package com.example.luca.planit;

/**
 * Created by Luca on 26/07/2017.
 */

public class SelectedEvent {

    private static Event selectedEvent;

    private SelectedEvent() {
    }

    public static void storeSelectedEvent(Event event) {
        selectedEvent = event;
    }

    public static Event getSelectedEvent() {
        return selectedEvent;
    }
}
