package com.example.luca.planit;

public class PersonImpl implements Person {
	private final String name;
	private final String surname;
	
	public PersonImpl(String name, String surname) {
		super();
		this.name = name;
		this.surname = surname;
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
	public String toString() {
		return "PersonImpl [name=" + name + ", surname=" + surname + "]";
	}
	
}
