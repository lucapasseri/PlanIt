package com.example.luca.planit;

public class GuestImpl implements Guest {
	private final Person personalInfo;
	private final boolean confirmed;


	public GuestImpl(String name, String surname, boolean confirmed) {
		super();
		this.personalInfo = new PersonImpl(name,surname);
		this.confirmed = confirmed;
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
	public boolean isConfirmed() {
		return this.confirmed;
	}

	@Override
	public String toString() {
		return "GuestImpl [name=" + this.personalInfo.getName() + ", surname=" + this.personalInfo.getSurname() + ", confirmed=" + confirmed + "]";
	}



}
