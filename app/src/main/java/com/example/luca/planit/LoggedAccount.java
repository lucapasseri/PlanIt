package com.example.luca.planit;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Luca on 25/07/2017.
 */

public class LoggedAccount {

    private static Account loggedAccount;
    private static LinkedList<Integer> colors = new LinkedList<>(Arrays.asList(
                                                                                R.color.emeral,
                                                                                R.color.colorAccent,
                                                                                R.color.colorPrimary));
    private static LinkedList<Integer> colorsT = new LinkedList<>(Arrays.asList(
            R.color.emeral,
            R.color.colorAccent,
            R.color.colorPrimary));

    private LoggedAccount() {

    }

    public static void storeLoggedAccount(Account account) {
        loggedAccount = account;
    }

    public static Account getLoggedAccount() {
        return loggedAccount;
    }
    public static Integer getColor(){
        Integer colorToReturn = LoggedAccount.colors.removeFirst();
        LoggedAccount.colors.addLast(colorToReturn);
        return colorToReturn;
    }
    public static Integer getColorT(){
        Integer colorToReturn = LoggedAccount.colorsT.removeFirst();
        LoggedAccount.colorsT.addLast(colorToReturn);
        return colorToReturn;
    }
}
