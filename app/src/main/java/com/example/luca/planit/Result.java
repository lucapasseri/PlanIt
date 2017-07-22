package com.example.luca.planit;

/**
 * Created by diego on 22/07/2017.
 */

public class Result {
    private final LoginResult result;
    private final Account account;

    public Result(LoginResult result) {
        this.result = result;
        this.account = null;
    }

    public Result(LoginResult result, Account account) {
        this.result = result;
        this.account = account;
    }

    boolean isPresentAccount(){
        return account == null;
    }

    public LoginResult getResult() {
        return result;
    }

    public Account getAccount() {
        return account;
    }
}
