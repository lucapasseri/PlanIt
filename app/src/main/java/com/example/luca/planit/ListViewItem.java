package com.example.luca.planit;

/**
 * Created by Luca on 24/07/2017.
 */

public class ListViewItem {
    private String eventName;
    private String organizerName;
    private String organizerSurname;
    private int type;
    private String eventId;

    public ListViewItem(String eventName, String organizerName, String organizerSurname, int type, String eventId) {
        this.eventName = eventName;
        this.organizerName = organizerName;
        this.organizerSurname = organizerSurname;
        this.type = type;
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setText(String text) {
        this.eventName = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEventId() {
        return eventId;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public String getOrganizerSurname() {
        return organizerSurname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListViewItem that = (ListViewItem) o;

        if (type != that.type) return false;
        if (eventName != null ? !eventName.equals(that.eventName) : that.eventName != null) return false;
        return eventId != null ? eventId.equals(that.eventId) : that.eventId == null;

    }

    @Override
    public int hashCode() {
        int result = eventName != null ? eventName.hashCode() : 0;
        result = 31 * result + type;
        result = 31 * result + (eventId != null ? eventId.hashCode() : 0);
        return result;
    }
}
