package com.example.luca.planit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView displayName = (TextView) findViewById(R.id.signup_name);
        TextView displaySurname = (TextView) findViewById(R.id.signup_surname);
        TextView displayEmail = (TextView) findViewById(R.id.signup_email);
        TextView displayUsername = (TextView) findViewById(R.id.signup_username);
        TextView displayBornDate = (TextView) findViewById(R.id.signup_born_date);

        displayName.setText(LoggedAccount.getLoggedAccount().getName());
        displaySurname.setText(LoggedAccount.getLoggedAccount().getSurname());
        displayEmail.setText(LoggedAccount.getLoggedAccount().getEmail());
        displayUsername.setText(LoggedAccount.getLoggedAccount().getUsername());
        displayBornDate.setText(LoggedAccount.getLoggedAccount().getBorndate());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
