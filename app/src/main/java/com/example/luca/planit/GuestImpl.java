package com.example.luca.planit;

public class GuestImpl implements Guest {
	private String name;
	private String surname;
	private final boolean confirmed;
	

	public GuestImpl(String name, String surname, boolean confirmed) {
		super();
		this.name = name;
		this.surname = surname;
		this.confirmed = confirmed;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getSurname() {
		return this.surname;
	}

	@Override
	public boolean isConfirmed() {
		return this.confirmed;
	}

	@Override
	public String toString() {
		return "GuestImpl [name=" + name + ", surname=" + surname + ", confirmed=" + confirmed + "]";
	}
	

}
