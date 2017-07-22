package com.example.luca.planit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
    }

    private boolean areDataStored() {
        return false;
    }
}
