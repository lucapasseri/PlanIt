package com.example.luca.planit;

public class TaskRequestWrapper {
	private final String userName;
	private final String eventId;
	
	
	public TaskRequestWrapper(String userName, String eventId) {
		super();
		this.userName = userName;
		this.eventId = eventId;
	}
	
	public String getUserName() {
		return userName;
	}
	public String getEventId() {
		return eventId;
	}
	
}
