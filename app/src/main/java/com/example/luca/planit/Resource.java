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
    public static final String ASSIGN_TASK_PAGE = "assegnaCompiti.php";
    public static final String GET_BEST_DATE_PAGE = "computeBestDate.php";
    public static final String GET_BEST_PLACE_PAGE = "computeBestPlace.php";
    public static final String GET_PLACE_BY_ID = "getLuogoByID.php";
    public static final String CREATE_GROUP_PAGE = "createGroup.php";
    public static final String DELETE_EVENT_PAGE = "deleteEvent.php";
}
