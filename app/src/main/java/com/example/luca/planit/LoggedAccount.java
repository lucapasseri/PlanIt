package com.example.luca.planit;

/**
 * Created by Luca on 25/07/2017.
 */

public class LoggedAccount {

    private static Account loggedAccount;

    private LoggedAccount() {

    }

    public static void createAccount(Account account) {
        loggedAccount = account;
    }

    public static Account getLoggedAccount() {
        return loggedAccount;
    }
}
