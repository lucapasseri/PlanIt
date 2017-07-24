package com.example.luca.planit;

public class DatePreferenceWrapperImpl implements DatePreferenceWrapper {
	private final String startDate;
	private final String endDate;
	private final String userId;
	private final String eventId;
	
	
	
	private DatePreferenceWrapperImpl(String startDate, String endDate, String userId, String eventId) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.userId = userId;
		this.eventId = eventId;
	}
	
	public String getStartDate() {
		return startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public String getUserId() {
		return userId;
	}
	public String getEventId() {
		return eventId;
	}
	
	
	public static class Builder{
		private  String startDate;
		private  String endDate;
		private  String userId;
		private  String eventId;
		
		public Builder setStartDate(String startDate) {
			this.startDate = startDate;
			return this;
		}
		public Builder setEndDate(String endDate) {
			this.endDate = endDate;
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
		
		public DatePreferenceWrapperImpl build(){
			return new DatePreferenceWrapperImpl(this.startDate,this.endDate,this.userId,this.eventId);
		}
	}
	
}
