package com.example.luca.planit;

/**
 * Created by diego on 26/07/2017.
 */

public class ListGroupItem {
    private final String groupName ;
    private final String groupId;
    private final Group group;

    public ListGroupItem(String groupName, String groupId,Group group) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.group = group;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public Group getGroup() {
        return group;
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
