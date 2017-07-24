package com.example.luca.planit;

public class TimePreferenceWrapperImpl implements TimePreferenceWrapper {
	private final String startTime;
	private final String endTime;
	private final String userId;
	private final String eventId;
	
	
	
	private TimePreferenceWrapperImpl(String startTime, String endTime, String userId, String eventId) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.userId = userId;
		this.eventId = eventId;
	}
	
	public String getStartTime() {
		return startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public String getUserId() {
		return userId;
	}
	public String getEventId() {
		return eventId;
	}
	
	
	public static class Builder{
		private  String startTime;
		private  String endTime;
		private  String userId;
		private  String eventId;
		
		public Builder setStartTime(String startTime) {
			this.startTime = startTime;
			return this;
		}
		public Builder setEndTime(String endTime) {
			this.endTime = endTime;
			return this;
		}
		public Builder setUserId(String userId) {
			this.userId = userId;
			return this;
		}
		public Builder setEventId(String eventId) {
			this.eventId = eventId;
			return this;
		}
		
		public TimePreferenceWrapperImpl build(){
			return new TimePreferenceWrapperImpl(this.startTime,this.endTime,this.userId,this.eventId);
		}
	}
	
}
