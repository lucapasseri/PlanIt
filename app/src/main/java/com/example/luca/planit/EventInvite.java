package com.example.luca.planit;

public interface EventInvite {
	
	public boolean isMailInvite();
	
	public boolean isUserNameInvite();
	
	public String getEventId() ;
	
	public String getGuestUsername();
	
	public String getGuestMail() ;
}
