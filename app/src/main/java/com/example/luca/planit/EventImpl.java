package com.example.luca.planit;

import java.util.Collections;
import java.util.List;

public class EventImpl implements Event {
	private final EventInfo eventInfo;
	private final List<Guest> guestList;
	
	public EventImpl(EventInfo eventInfo, List<Guest> guestList) {
		super();
		this.eventInfo = eventInfo;
		this.guestList = guestList;
	}

	@Override
	public EventInfo getEventInfo() {
		return this.eventInfo;
	}

	@Override
	public List<Guest> getGuestList() {
		return Collections.unmodifiableList(this.guestList);
	}

	@Override
	public String toString() {
		return "EventImpl [eventInfo=" + eventInfo + ", guestList=" + guestList + "]";
	}

}
