package com.example.luca.planit;

/**
 * Created by diego on 26/07/2017.
 */

public class ListInviteItem {
    private final InviteWrapper inviteWrapper;

    public ListInviteItem(InviteWrapper inviteWrapper) {
        this.inviteWrapper = inviteWrapper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListInviteItem that = (ListInviteItem) o;

        return inviteWrapper.equals(that.inviteWrapper);

    }

    public InviteWrapper getInviteWrapper() {
        return inviteWrapper;
    }

    @Override
    public int hashCode() {
        return inviteWrapper.hashCode();
    }
}
