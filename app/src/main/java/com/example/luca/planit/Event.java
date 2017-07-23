package com.example.luca.planit;

import java.util.List;

public interface Event {
	EventInfo getEventInfo();
	List<Guest> getGuestList();
}
