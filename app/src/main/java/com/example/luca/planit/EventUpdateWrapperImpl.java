package com.example.luca.planit;

import java.util.Objects;

public class EventUpdateWrapperImpl implements EventUpdateWrapper {

	private final String eventId;
	private final Place place;
	private final String date;
	private final String time;
	

	private EventUpdateWrapperImpl(String eventId, Place placeP, String dateP,String time) {
		super();
		this.eventId = eventId;
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
		if (time == null){
			this.time = null;
		}else{
			this.time = time;
		}
	}

	@Override
	public String getEventId() {
		return this.eventId;
	}
	public Place getPlace() {
		return place;
	}

	public String getDate() {
		return date;
	}
	
	@Override
	public String getTime() {
		return this.time;
	}
	
	public static class Builder {
		private  String eventId;
		private  Place place;
		private  String date;
		private String time;
		

		public Builder setEventId(String eventId) {
			this.eventId = eventId;
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
		public Builder setTime(String time) {
			this.time = time;
			return this;
		}
		
		public EventUpdateWrapperImpl build() {
			return new EventUpdateWrapperImpl(Objects.requireNonNull(this.eventId), this.place, this.date,this.time);
		}
		
	}






	
	
}
