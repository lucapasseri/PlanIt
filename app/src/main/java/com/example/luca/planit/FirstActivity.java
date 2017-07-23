package com.example.luca.planit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class FirstActivity extends AppCompatActivity {

    public static final String FROM_FIRST_EXTRA = "FROM_FIRST";
    public static final String USERNAME_EXTRA = "USERNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Intent intent;

        if (areDataStored()) {
            intent = new Intent(this, HomeActivity.class);
            String username = "";
            intent.putExtra(FROM_FIRST_EXTRA, true);
            intent.putExtra(USERNAME_EXTRA, username);
            startActivity(intent);
        } else {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        finish();
    }

    private boolean areDataStored() {

        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        String email = prefs.getString(getString(R.string.email_pref), null);
        String password = prefs.getString(getString(R.string.password_pref), null);

        if(email!=null && password!=null) {
            return true;
        }

        return false;

    }
}
