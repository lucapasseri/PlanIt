package com.example.luca.planit;

public interface EventInfo {
	String getDate(DateFormatType formatType);
	String getAddress();
	String getProvince();
	String getNamePlace();
	String getCity();
	Organizer getOrganizer();
	String getNameEvent() ;
	String getTime() ;
	String getEventId();
}
