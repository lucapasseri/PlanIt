package com.example.luca.planit;


/**
 * Created by Luca on 26/07/2017.
 */

public enum DateFormatType {

    DD_MM_YYYY_BACKSLASH("dd/MM/yyyy"), YYYY_MM_DD_DASH("yyyy-MM-dd");

    private final String format;

    DateFormatType(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
