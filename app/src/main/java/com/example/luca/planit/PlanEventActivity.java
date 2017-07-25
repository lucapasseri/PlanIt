package com.example.luca.planit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PlanEventActivity extends AppCompatActivity {

    private EditText eventNameEdit;
    private RadioGroup placeRadio;
    private EditText placeNameEdit;
    private EditText placeProvinceEdit;
    private EditText placeCityEdit;
    private EditText placeAddressEdit;
    private TextView dateFormText;
    private RadioGroup dateRadio;
    private EditText dateEdit;
    private RadioGroup hourRadio;
    private NumberPicker hourNp;
    private NumberPicker minutesNp;
    private Button createButton;
    private View progressView;
    private ScrollView createEventFormView;

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
        placeRadio = (RadioGroup) findViewById(R.id.radio_place);
        placeNameEdit = (EditText) findViewById(R.id.place_name_edit);
        placeProvinceEdit = (EditText) findViewById(R.id.place_province_edit);
        placeCityEdit = (EditText) findViewById(R.id.place_city_edit);
        placeAddressEdit = (EditText) findViewById(R.id.place_address_edit);
        dateFormText = (TextView) findViewById(R.id.date_form_text);
        dateRadio = (RadioGroup) findViewById(R.id.radio_date);
        dateEdit = (EditText) findViewById(R.id.date_edit);
        hourRadio = (RadioGroup) findViewById(R.id.radio_hour);
        hourNp = (NumberPicker) findViewById(R.id.hour_np);
        minutesNp = (NumberPicker) findViewById(R.id.minutes_np);
        createButton = (Button) findViewById(R.id.create_button);

        dateLayout = (LinearLayout) findViewById(R.id.date_layout);
        placeLayout = (LinearLayout) findViewById(R.id.place_layout);
        hourLayout = (LinearLayout) findViewById(R.id.hour_layout);
        createEventFormView = (ScrollView) findViewById(R.id.plan_event_form);

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

                if (dateCalendar.getTime().after(Calendar.getInstance().getTime())) {
                    updateDateLabel();
                } else {
                    Toast.makeText(PlanEventActivity.this, "The date must be after today", Toast.LENGTH_SHORT).show();
                }

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

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptCreateEvent();
            }
        });

        ((ProgressBar) progressView).getIndeterminateDrawable().
                setColorFilter(ContextCompat.getColor(this, R.color.green_sea), android.graphics.PorterDuff.Mode.MULTIPLY);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateDateLabel() {

        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);

        dateEdit.setText(sdf.format(dateCalendar.getTime()));
    }

    private void attemptCreateEvent() {
        // Reset errors.
        eventNameEdit.setError(null);
        placeNameEdit.setError(null);
        placeProvinceEdit.setError(null);
        placeCityEdit.setError(null);
        placeAddressEdit.setError(null);
        dateEdit.setError(null);


        // Store values at the time of the login attempt.

        Choice placeChoice;
        Choice dateChoice;
        Choice hourChoice;

        if (placeRadio.getCheckedRadioButtonId() == R.id.radio_place_defined) {
            placeChoice = Choice.NOW;
        } else {
            placeChoice = Choice.AFTER;
        }

        if (dateRadio.getCheckedRadioButtonId() == R.id.radio_date_defined) {
            dateChoice = Choice.NOW;
        } else {
            dateChoice = Choice.AFTER;
        }
        if (hourRadio.getCheckedRadioButtonId() == R.id.radio_hour_defined) {
            hourChoice = Choice.NOW;
        } else {
            hourChoice = Choice.AFTER;
        }


        String eventName = eventNameEdit.getText().toString();
        String placeName = placeNameEdit.getText().toString();
        String placeProvince = placeProvinceEdit.getText().toString();
        String placeCity = placeCityEdit.getText().toString();
        String placeAddress = placeAddressEdit.getText().toString();
        String date = dateEdit.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(eventName)) {
            eventNameEdit.setError(getString(R.string.error_field_required));
            focusView = eventNameEdit;
            cancel = true;
        } else if (placeChoice == Choice.NOW) {
            if (TextUtils.isEmpty(placeName)) {
                placeNameEdit.setError(getString(R.string.error_field_required));
                focusView = placeNameEdit;
                cancel = true;
            } else if (TextUtils.isEmpty(placeProvince)) {
                placeProvinceEdit.setError(getString(R.string.error_field_required));
                focusView = placeProvinceEdit;
                cancel = true;
            } else if (TextUtils.isEmpty(placeCity)) {
                placeCityEdit.setError(getString(R.string.error_field_required));
                focusView = placeCityEdit;
                cancel = true;
            }
        }
        if (!cancel && (dateChoice == Choice.NOW)) {
            if (TextUtils.isEmpty(date)) {
                dateEdit.setError(getString(R.string.error_field_required));
                focusView = dateEdit;
                cancel = true;
                createEventFormView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            String fromFormat = "dd/MM/yyy";
            String toFormat = "yyyy-MM-dd";

            SimpleDateFormat fromFormatter = new SimpleDateFormat(fromFormat, Locale.US);
            SimpleDateFormat toFormatter = new SimpleDateFormat(toFormat, Locale.US);

            try {
                Date dateFormatted = fromFormatter.parse(date);
                date = toFormatter.format(dateFormatted);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            /*SignupActivity.RegistrationTask registrationTask = new SignupActivity.RegistrationTask(this);
            registrationTask.execute(new RegistrationData.Builder()
                    .setName(name)
                    .setSurname(surname)
                    .setEmail(email)
                    .setUsername(username)
                    .setPassword(password)
                    .setBorndate(bornDate)
                    .build()); */
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            createButton.setVisibility(show ? View.GONE : View.VISIBLE);
            createEventFormView.setVisibility(show ? View.GONE : View.VISIBLE);

            createEventFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    createEventFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            createButton.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    createButton.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            createButton.setVisibility(show ? View.GONE : View.VISIBLE);
            createEventFormView.setVisibility(show ? View.GONE : View.VISIBLE);

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private enum Choice {
        NOW, AFTER
    }



}
