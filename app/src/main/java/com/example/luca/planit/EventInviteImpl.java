package com.example.luca.planit;

public class EventInviteImpl implements EventInvite {
	private final String eventId;
	private final String guestUsername;
	private final String guestMail;
	
	private EventInviteImpl(String eventId, String guestUsername, String guestMail) {
		super();
		this.eventId = eventId;
		this.guestUsername = guestUsername;
		this.guestMail = guestMail;
	}
	public static EventInviteImpl getInviteByMail(String eventId,String guestMail){
		return new EventInviteImpl(eventId,"",guestMail);
	}
	public static EventInviteImpl getInviteByUserName(String eventId,String guestUsername){
		return new EventInviteImpl(eventId,guestUsername,"");
	}
	public boolean isMailInvite(){
		return this.guestUsername.isEmpty();
	}
	
	public boolean isUserNameInvite(){
		return this.guestMail.isEmpty();
	}
	
	public String getEventId() {
		return eventId;
	}
	
	public String getGuestUsername() {
		if(this.isMailInvite()){
			throw new IllegalStateException();
		}
		return guestUsername;
	}
	
	public String getGuestMail() {
		if(this.isUserNameInvite()){
			throw new IllegalStateException();
		}
		
		return guestMail;
	}
	
}
