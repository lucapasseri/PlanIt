package com.example.luca.planit;

/**
 * Created by diego on 26/07/2017.
 */

public class SelectedGroup {
    private static Group selectedGroup;

    public static Group getSelectedGroup() {
        return selectedGroup;
    }

    public static void setSelectedGroup(Group selectedGroup) {
        selectedGroup = selectedGroup;
    }
}
