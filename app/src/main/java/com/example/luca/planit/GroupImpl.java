package com.example.luca.planit;

import java.util.List;

public class GroupImpl implements Group {
	private final List<Person> peopleInGroup;
	private final String nameGroup;
	public GroupImpl(List<Person> peopleInGroup, String nameGroup) {
		super();
		this.peopleInGroup = peopleInGroup;
		this.nameGroup = nameGroup;
	}
	
	public List<Person> getPeopleInGroup() {
		return peopleInGroup;
	}
	public String getNameGroup() {
		return nameGroup;
	}

	@Override
	public String toString() {
		return "GroupImpl [peopleInGroup=" + peopleInGroup + ", nameGroup=" + nameGroup + "]";
	}
	


	
}
