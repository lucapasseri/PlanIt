package com.example.luca.planit;

/**
 * Created by Luca on 24/07/2017.
 */

public class ListViewItem {
    private Event event;
    private Integer color;
    private int type;
    private String eventId;

    public ListViewItem(Event event, int type, String eventId,Integer color) {
        this.event = event;
        this.type = type;
        this.eventId = eventId;
        this.color = color;
    }

    public Event getEvent() {
        return event;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getColor() {
        return color;
    }

    public String getEventId() {
        return eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListViewItem that = (ListViewItem) o;

        return eventId != null ? eventId.equals(that.eventId) : that.eventId == null;

    }

    @Override
    public int hashCode() {
        int result = 31 * type;
        result = 31 * result + (eventId != null ? eventId.hashCode() : 0);
        return result;
    }
}
