package com.example.luca.planit;

public class PlacePreferenceWrapperImpl implements PlacePreferenceWrapper {
	private final Place place;
	private final String userId;
	private final String eventId;
	

public PlacePreferenceWrapperImpl(Place place, String userId, String eventId) {
		super();
		this.place = place;
		this.userId = userId;
		this.eventId = eventId;
	}


	@Override
	public String getUserId() {
		return this.userId;
	}
	@Override
	public Place getPlace() {
		return this.place;
	}

	@Override
	public String getEventId() {
		return this.eventId;
	}

}
