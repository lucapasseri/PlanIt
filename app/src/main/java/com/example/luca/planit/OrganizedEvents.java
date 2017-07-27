package com.example.luca.planit;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Luca on 27/07/2017.
 */

public class OrganizedEvents {

    private static List<EventInfo> organizedEventInfoList = new LinkedList<>();

    private OrganizedEvents() {
    }

    public static boolean addEventInfo(EventInfo event) {
        if (!organizedEventInfoList.contains(event)) {
            organizedEventInfoList.add(event);
            return true;
        } else {
            return false;
        }

    }

    public static boolean removeEventInfo(EventInfo event) {
        if (organizedEventInfoList.contains(event)) {
            organizedEventInfoList.remove(event);
            return true;
        } else {
            return false;
        }
    }

    public static void clearEventInfoList(EventInfo event) {
        organizedEventInfoList.clear();
    }

    public static boolean contains(EventInfo eventInfo) {
        for (EventInfo info : organizedEventInfoList) {
            if (info.getEventId().equals(eventInfo.getEventId())) {
                return true;
            }
        }
        return false;
    }

}
