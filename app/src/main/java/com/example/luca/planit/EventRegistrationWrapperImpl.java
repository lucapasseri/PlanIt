package com.example.luca.planit;

import java.util.Objects;

public class EventRegistrationWrapperImpl implements EventRegistrationWrapper {

	private final String organizer_id;
	private final String name_event;
	private final Place place;
	private final String date;
	
	

	private EventRegistrationWrapperImpl(String organizer_id, String name_event, Place placeP, String dateP) {
		super();
		this.organizer_id = organizer_id;
		this.name_event = name_event;
		if(placeP == null){
			this.place = null;
		}else{
			this.place = placeP;
		}
		if (dateP == null){
			this.date = null;
		}else{
			this.date = dateP;
		}
	}

	public String getOrganizer_id() {
		return organizer_id;
	}

	public String getName_event() {
		return name_event;
	}

	public Place getPlace() {
		return place;
	}

	public String getDate() {
		return date;
	}
	
	public static class Builder {
		private  String organizer_id;
		private  String name_event;
		private  Place place;
		private  String date;
		
		
		public Builder setOrganizer_id(String organizer_id) {
			this.organizer_id = organizer_id;
			return this;
		}
		public Builder setName_event(String name_event) {
			this.name_event = name_event;
			return this;
		}
		public Builder setPlace(Place place) {
			this.place = place;
			return this;
		}
		public Builder setDate(String date) {
			this.date = date;
			return this;
		}
		
		public EventRegistrationWrapperImpl build() {
			return new EventRegistrationWrapperImpl(Objects.requireNonNull(this.organizer_id),
					Objects.requireNonNull(this.name_event),this.place, this.date);
		}
		
	}
	
	
}
