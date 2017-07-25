package com.example.luca.planit;

/**
 * Created by Luca on 24/07/2017.
 */

public class ListViewItem {
    private String text;
    private int type;
    private String eventId;

    public ListViewItem(String text, int type, String eventId) {
        this.text = text;
        this.type = type;
        this.eventId = eventId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListViewItem that = (ListViewItem) o;

        if (type != that.type) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        return eventId != null ? eventId.equals(that.eventId) : that.eventId == null;

    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + type;
        result = 31 * result + (eventId != null ? eventId.hashCode() : 0);
        return result;
    }
}
