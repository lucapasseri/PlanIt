package com.example.luca.planit;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Luca on 27/07/2017.
 */

public class OrganizedEvents {

    private static List<Event> organizedEvents = new LinkedList<>();

    private OrganizedEvents() {
    }

    public static boolean addEvent(Event event) {
        if (!organizedEvents.contains(event)) {
            organizedEvents.add(event);
            return true;
        } else {
            return false;
        }

    }

    public static boolean removeEvent(Event event) {
        if (organizedEvents.contains(event)) {
            organizedEvents.remove(event);
            return true;
        } else {
            return false;
        }
    }

    public static void clearEvents(Event event) {
        organizedEvents.clear();
    }

}
