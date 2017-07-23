package com.example.luca.planit;

/**
 * Created by diego on 22/07/2017.
 */

public class Result {
    private final RequestResult result;
    private final Account account;

    public Result(RequestResult result) {
        this.result = result;
        this.account = null;
    }

    public Result(RequestResult result, Account account) {
        this.result = result;
        this.account = account;
    }

    boolean isPresentAccount(){
        return account == null;
    }

    public RequestResult getResult() {
        return result;
    }

    public Account getAccount() {
        return account;
    }
}
