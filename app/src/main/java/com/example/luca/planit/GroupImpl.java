package com.example.luca.planit;

import java.util.List;

public class GroupImpl implements Group {
	private final List<Person> peopleInGroup;
	private final String nameGroup;
	private final String groupId;

	public GroupImpl(List<Person> peopleInGroup, String nameGroup, String groupId) {
		this.peopleInGroup = peopleInGroup;
		this.nameGroup = nameGroup;
		this.groupId = groupId;
	}

	@Override
	public String getGroupId() {
		return groupId;
	}

	public List<Person> getPeopleInGroup() {
		return peopleInGroup;
	}
	public String getNameGroup() {
		return nameGroup;
	}

	@Override
	public String toString() {
		return nameGroup;
	}
}
