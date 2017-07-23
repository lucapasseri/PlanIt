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
    public static final String ORGANIZED_EVENT_PAGE = "getOrganizedEvent.php";
    public static final String PART_OF_EVENT_PAGE = "getInvitedEvent.php";
    public static final String REGISTRATION_EVENT_PAGE = "registrationEvent.php";
    public static final String ACCEPT_INVITE_PAGE = "accettaInvito.php";

}
