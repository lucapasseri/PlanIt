package com.example.luca.planit;

import android.app.DatePickerDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private EditText startDateEdit;
    private EditText endDateEdit;
    private RadioGroup placeRadio;
    private EditText placeNameEdit;
    private RadioGroup hourRadio;
    private NumberPicker startHourNp;
    private NumberPicker startMinutesNp;
    private NumberPicker endHourNp;
    private NumberPicker endMinutesNp;

    private LinearLayout placeLayout;
    private LinearLayout dateLayout;
    private LinearLayout hourLayout;

    private Calendar startDateCalendar = Calendar.getInstance();
    private Calendar endDateCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener startDate;
    private DatePickerDialog.OnDateSetListener endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_event);

        progressView = findViewById(R.id.signup_progress);

        eventNameEdit = (EditText) findViewById(R.id.event_name_edit);

        placeNameEdit = (EditText) findViewById(R.id.place_name_edit);
        placeRadio = (RadioGroup) findViewById(R.id.radio_place);

        startDateEdit = (EditText) findViewById(R.id.start_date_edit);
        endDateEdit = (EditText) findViewById(R.id.end_date_edit);
        dateRadio = (RadioGroup) findViewById(R.id.radio_date);

        hourRadio = (RadioGroup) findViewById(R.id.radio_hour);
        startHourNp = (NumberPicker) findViewById(R.id.start_hour_np);
        startMinutesNp = (NumberPicker) findViewById(R.id.start_minutes_np);
        endHourNp = (NumberPicker) findViewById(R.id.end_hour_np);
        endMinutesNp = (NumberPicker) findViewById(R.id.end_minutes_np);

        dateLayout = (LinearLayout) findViewById(R.id.date_layout);
        placeLayout = (LinearLayout) findViewById(R.id.place_layout);
        hourLayout = (LinearLayout) findViewById(R.id.hour_range_layout);

        startHourNp.setMinValue(0);
        startHourNp.setMaxValue(23);
        startMinutesNp.setMinValue(0);
        startMinutesNp.setMaxValue(60);

        endHourNp.setMinValue(0);
        endHourNp.setMaxValue(23);
        endMinutesNp.setMinValue(0);
        endMinutesNp.setMaxValue(60);

        startHourNp.setWrapSelectorWheel(false);
        startMinutesNp.setWrapSelectorWheel(false);
        endHourNp.setWrapSelectorWheel(false);
        endMinutesNp.setWrapSelectorWheel(false);

        startHourNp.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        startMinutesNp.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        endHourNp.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        endMinutesNp.setFormatter(new NumberPicker.Formatter() {
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

        startDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                startDateCalendar.set(Calendar.YEAR, year);
                startDateCalendar.set(Calendar.MONTH, monthOfYear);
                startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(DateType.START_DATE);
            }

        };

        endDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                endDateCalendar.set(Calendar.YEAR, year);
                endDateCalendar.set(Calendar.MONTH, monthOfYear);
                endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(DateType.END_DATE);
            }

        };

        startDateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startDateEdit.setError(null);
                new DatePickerDialog(PlanEventActivity.this, startDate, startDateCalendar
                        .get(Calendar.YEAR), startDateCalendar.get(Calendar.MONTH),
                        startDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                endDateEdit.setError(null);
                new DatePickerDialog(PlanEventActivity.this, endDate, endDateCalendar
                        .get(Calendar.YEAR), endDateCalendar.get(Calendar.MONTH),
                        endDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ((ProgressBar) progressView).getIndeterminateDrawable().
                setColorFilter(ContextCompat.getColor(this, R.color.green_sea), android.graphics.PorterDuff.Mode.MULTIPLY);

    }

    private void updateDateLabel(DateType dateType) {

        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

        switch (dateType) {
            case START_DATE:
                startDateEdit.setText(sdf.format(startDateCalendar.getTime()));
                break;
            case END_DATE:
                endDateEdit.setText(sdf.format(endDateCalendar.getTime()));
                break;
        }
    }


    private enum DateType {
        START_DATE, END_DATE
    }



}
