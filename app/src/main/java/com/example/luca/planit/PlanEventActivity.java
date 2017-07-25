package com.example.luca.planit;

import android.app.DatePickerDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PlanEventActivity extends AppCompatActivity {

    private View progressView;
    private EditText eventNameEdit;
    private RadioGroup dateRadio;
    private EditText dateEdit;
    private RadioGroup placeRadio;
    private EditText placeNameEdit;
    private RadioGroup hourRadio;
    private NumberPicker hourNp;
    private NumberPicker minutesNp;

    private LinearLayout placeLayout;
    private LinearLayout dateLayout;
    private LinearLayout hourLayout;

    private Calendar dateCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_event);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressView = findViewById(R.id.signup_progress);

        eventNameEdit = (EditText) findViewById(R.id.event_name_edit);

        placeNameEdit = (EditText) findViewById(R.id.place_name_edit);
        placeRadio = (RadioGroup) findViewById(R.id.radio_place);

        dateEdit = (EditText) findViewById(R.id.date_edit);
        dateRadio = (RadioGroup) findViewById(R.id.radio_date);

        hourRadio = (RadioGroup) findViewById(R.id.radio_hour);
        hourNp = (NumberPicker) findViewById(R.id.hour_np);
        minutesNp = (NumberPicker) findViewById(R.id.minutes_np);

        dateLayout = (LinearLayout) findViewById(R.id.date_layout);
        placeLayout = (LinearLayout) findViewById(R.id.place_layout);
        hourLayout = (LinearLayout) findViewById(R.id.hour_layout);

        hourNp.setMinValue(0);
        hourNp.setMaxValue(23);
        minutesNp.setMinValue(0);
        minutesNp.setMaxValue(59);

        hourNp.setWrapSelectorWheel(false);
        minutesNp.setWrapSelectorWheel(false);

        hourNp.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        minutesNp.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });



        dateRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radio_date_defined:
                        dateLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio_date_not_defined:
                        dateLayout.setVisibility(View.GONE);
                }
            }
        });

        placeRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radio_place_defined:
                        placeLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio_place_not_defined:
                        placeLayout.setVisibility(View.GONE);
                }
            }
        });

        hourRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radio_hour_defined:
                        hourLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio_hour_not_defined:
                        hourLayout.setVisibility(View.GONE);
                }
            }
        });

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                dateCalendar.set(Calendar.YEAR, year);
                dateCalendar.set(Calendar.MONTH, monthOfYear);
                dateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }

        };

        dateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dateEdit.setError(null);
                new DatePickerDialog(PlanEventActivity.this, date, dateCalendar
                        .get(Calendar.YEAR), dateCalendar.get(Calendar.MONTH),
                        dateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ((ProgressBar) progressView).getIndeterminateDrawable().
                setColorFilter(ContextCompat.getColor(this, R.color.green_sea), android.graphics.PorterDuff.Mode.MULTIPLY);

    }

    private void updateDateLabel() {

        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

        dateEdit.setText(sdf.format(dateCalendar.getTime()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }



}
