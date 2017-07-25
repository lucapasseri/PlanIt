package com.example.luca.planit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
    private RadioGroup timeRadio;
    private NumberPicker hourNp;
    private NumberPicker minutesNp;
    private Button createButton;
    private View progressView;
    private ScrollView createEventFormView;

    private LinearLayout placeLayout;
    private LinearLayout dateLayout;
    private LinearLayout timeLayout;

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
        timeRadio = (RadioGroup) findViewById(R.id.radio_time);
        hourNp = (NumberPicker) findViewById(R.id.hour_np);
        minutesNp = (NumberPicker) findViewById(R.id.minutes_np);
        createButton = (Button) findViewById(R.id.create_button);

        dateLayout = (LinearLayout) findViewById(R.id.date_layout);
        placeLayout = (LinearLayout) findViewById(R.id.place_layout);
        timeLayout = (LinearLayout) findViewById(R.id.time_layout);
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

        timeRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radio_time_defined:
                        timeLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio_time_not_defined:
                        timeLayout.setVisibility(View.GONE);
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
        Choice timeChoice;

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
        if (timeRadio.getCheckedRadioButtonId() == R.id.radio_time_defined) {
            timeChoice = Choice.NOW;
        } else {
            timeChoice = Choice.AFTER;
        }


        String eventName = eventNameEdit.getText().toString();
        String placeName = placeNameEdit.getText().toString();
        String placeProvince = placeProvinceEdit.getText().toString();
        String placeCity = placeCityEdit.getText().toString();
        String placeAddress = placeAddressEdit.getText().toString();
        String date = dateEdit.getText().toString();
        String hour = String.format("%02d", hourNp.getValue());
        String minutes = String.format("%02d", minutesNp.getValue());
        String time = hour + ":" + minutes;

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
            EventRegistrationWrapperImpl.Builder builder = new EventRegistrationWrapperImpl.Builder();

            if (placeChoice == Choice.NOW) {
                Place toAddPlace = new PlaceImpl.Builder()
                        .setAddress(placeAddress)
                        .setCity(placeCity)
                        .setProvince(placeProvince)
                        .setNamePlace(placeName)
                        .build();
                builder.setPlace(toAddPlace);
            }
            if(dateChoice == Choice.NOW){
                builder.setDate(date);
            }
            if(timeChoice == Choice.NOW){
                builder.setTime(time);
            }
            //MEttere set time e controllo data subito
            EventRegistrationWrapper wrapper =  builder
                                                .setName_event(eventName)
                                                .setOrganizer_id(LoggedAccount.getLoggedAccount().getId())
                                                .build();

            RegistrationEventTask registrationTask = new RegistrationEventTask(this);
            registrationTask.execute(wrapper);

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


    private class RegistrationEventTask extends AsyncTask<EventRegistrationWrapper, Void, EventInfo> {

        Context context;

        public RegistrationEventTask(Context context) {
            this.context = context;
        }

        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }

        //Nome dei parametri del json di risposta


        EventInfo toReturn;
        HttpURLConnection httpURLConnection = null;
        StringBuilder response = new StringBuilder();
        BufferedReader rd = null;

        @Override
        protected void onPostExecute(EventInfo result) {
            if (result != null) {

                Intent intent = new Intent(getApplication(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            } else {
                showProgress(false);
            }
        }

        @Override
        protected EventInfo doInBackground(EventRegistrationWrapper... params) {
            try {
                URL url = new URL(Resource.BASE_URL + Resource.REGISTRATION_EVENT_PAGE); //Enter URL here
                JSONObject returned = null;
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                //httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                HashMap<String, String> toPass = new HashMap<>();
            /*
            Place toAddPlace = new PlaceImpl.Builder()
                    .setAddress("Via Boscone 715")
                    .setCity("Cesena")
                    .setProvince("FC")
                    .setNamePlace("Casadeis")
                    .build();
            EventRegistrationWrapper wrapper = new EventRegistrationWrapperImpl.Builder()
                    .setName_event("Serata GOT")
                    .setOrganizer_id("19")
                    .setPlace(toAddPlace)
                    .setDate("2017-07-24")
                    .build();
            */
                if (params[0].getPlace() != null) {
                    toPass.put("nome", params[0].getPlace().getNamePlace());
                    toPass.put("indirizzo", params[0].getPlace().getAddress());
                    toPass.put("citta", params[0].getPlace().getCity());
                    toPass.put("provincia", params[0].getPlace().getProvince());
                }

                if (params[0].getDate() != null) {
                    toPass.put("data", params[0].getDate());
                }
                if (params[0].getTime() != null) {
                    toPass.put("ora", params[0].getTime());
                }

                toPass.put("id_org", params[0].getOrganizer_id());
                toPass.put("nome_evento", params[0].getName_event());

                writer.write(getPostDataString(toPass));
                writer.flush();
                writer.close();
                os.close();
                httpURLConnection.connect();
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == httpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    rd = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println(response.toString());
                    returned = new JSONObject(response.toString());

                    EventInfoImpl.Builder builder = new EventInfoImpl.Builder();

                    if (params[0].getPlace() != null) {

                                builder
                                .setAddress(params[0].getPlace().getAddress())
                                .setCity(params[0].getPlace().getCity())
                                .setProvince(params[0].getPlace().getProvince())
                                .setNamePlace(params[0].getPlace().getNamePlace());

                    }
                    if (params[0].getDate() != null) {
                        builder.setData(params[0].getDate());
                    }
                    if (params[0].getTime() != null) {
                        builder.setTime(params[0].getTime());
                    }

                    builder.setNameEvent(params[0].getName_event())
                            .setEventId(returned.getString("result"))
                            .setOrganizer(new OrganizerImpl(LoggedAccount.getLoggedAccount().getName(),LoggedAccount.getLoggedAccount().getSurname()))
                            .build();
                    toReturn = builder.build();

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (rd != null) {
                    try {
                        rd.close();
                    } catch (Exception e) {

                    }
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return toReturn;
        }
    }



}
