package com.example.luca.planit;

/**
 * Created by diego on 26/07/2017.
 */

public class SelectedInvite {
    private static InviteToGroupWrapper selectedInvite;
    private SelectedInvite(){

    }

    public static InviteToGroupWrapper getSelectedInvite() {
        return selectedInvite;
    }

    public static void setSelectedInvite(InviteToGroupWrapper selectedInvited) {
        selectedInvite = selectedInvited;
    }
}
