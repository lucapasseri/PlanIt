package com.example.luca.planit;

public class GuestImpl implements Guest {
	private final Person personalInfo;
	private final GuestState guestState;


	public GuestImpl(String name, String surname, GuestState guestState) {
		super();
		this.personalInfo = new PersonImpl(name,surname);
		this.guestState = guestState;
	}

	@Override
	public String getName() {
		return this.personalInfo.getName();
	}

	@Override
	public String getSurname() {
		return this.personalInfo.getSurname();
	}

	@Override
	public GuestState getGuestState() {
		return guestState;
	}

	@Override
	public String toString() {
		return "GuestImpl [personalInfo=" + personalInfo + ", guestState=" + guestState + "]";
	}



}
