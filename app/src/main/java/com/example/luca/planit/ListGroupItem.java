package com.example.luca.planit;

/**
 * Created by diego on 26/07/2017.
 */

public class ListGroupItem {
    private final String groupName ;
    private final String groupId;

    public ListGroupItem(String groupName, String groupId) {
        this.groupName = groupName;
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListGroupItem that = (ListGroupItem) o;

        return groupId.equals(that.groupId);

    }

    @Override
    public int hashCode() {
        return groupId.hashCode();
    }

    @Override
    public String toString() {
        return "ListGroupItem{" +
                "groupName='" + groupName + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
