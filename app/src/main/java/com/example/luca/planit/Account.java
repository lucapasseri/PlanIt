package com.example.luca.planit;

/**
 * Created by diego on 18/03/2017.
 */

public interface Account {
    String getEmail();

    String getPassword();

    public String getUsername();

    public String getName();

    public String getSurname();

    public String getId();

    public String getBornDate(DateFormatType dateFormatType);

}
