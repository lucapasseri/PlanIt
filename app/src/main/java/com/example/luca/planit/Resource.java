package com.example.luca.planit;

/**
 * Created by diego on 02/04/2017.
 */

public final class Resource {
    private Resource(){
        throw new AssertionError();
    }
    public static final String BASE_URL = "http://192.168.1.12/planit/";
    public static final String LOGIN_PAGE = "login.php";
    public static final String REGISTRATION_PAGE = "registration.php";
}
