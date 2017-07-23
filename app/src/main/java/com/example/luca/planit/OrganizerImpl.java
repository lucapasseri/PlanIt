package com.example.luca.planit;

public class OrganizerImpl implements Organizer {

	private final String name;
	private final String surname;

	public OrganizerImpl(String name, String surname) {
		super();
		this.name = name;
		this.surname = surname;
	}

	@Override
	public String getOrganizerName() {
		return name;
	}

	@Override
	public String getOrganizerSurname() {
		return surname;
	}

}
