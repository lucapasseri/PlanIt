package com.example.luca.planit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
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

import static android.view.View.GONE;

/**
 * Created by diego on 26/07/2017.
 */

public class MakeProposalDialog extends Dialog {

    private volatile int maxTask = 0;
    private volatile int nTaskFinished = 0;

    private RadioGroup placeRadio;
    private EditText placeNameEdit;
    private EditText placeProvinceEdit;
    private EditText placeCityEdit;
    private EditText placeAddressEdit;
    private TextView dateFormText;
    private RadioGroup dateRadio;
    private EditText startDateEdit;
    private EditText endDateEdit;
    private RadioGroup timeRadio;
    private NumberPicker startHourNp;
    private NumberPicker startMinutesNp;
    private NumberPicker endHourNp;
    private NumberPicker endMinutesNp;
    private Button sendProposalButton;
    private View progressView;
    private ScrollView makeProposalFormView;
    private LinearLayout placeLayout;
    private LinearLayout dateLayout;
    private LinearLayout timeLayout;

    private Calendar startDateCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener startDate;
    private Calendar endDateCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener endDate;

    private final EventManagementActivity.ToShow toShow;

    protected MakeProposalDialog(EventManagementActivity.ToShow toShow, @NonNull Context context) {
        super(context);
        this.toShow = toShow;
    }

    protected MakeProposalDialog(EventManagementActivity.ToShow toShow, @NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.toShow = toShow;
    }

    protected MakeProposalDialog(EventManagementActivity.ToShow toShow, @NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.toShow = toShow;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_make_proposal);


        makeProposalFormView = (ScrollView) findViewById(R.id.make_proposal_form);
        progressView = findViewById(R.id.proposal_progress);
        placeRadio = (RadioGroup) findViewById(R.id.radio_place);
        placeNameEdit = (EditText) findViewById(R.id.place_name_edit);
        placeProvinceEdit = (EditText) findViewById(R.id.place_province_edit);
        placeCityEdit = (EditText) findViewById(R.id.place_city_edit);
        placeAddressEdit = (EditText) findViewById(R.id.place_address_edit);
        dateFormText = (TextView) findViewById(R.id.date_form_text);
        dateRadio = (RadioGroup) findViewById(R.id.radio_date);
        startDateEdit = (EditText) findViewById(R.id.start_date_edit);
        endDateEdit = (EditText) findViewById(R.id.end_date_edit);
        timeRadio = (RadioGroup) findViewById(R.id.radio_time);
        startHourNp = (NumberPicker) findViewById(R.id.start_hour_np);
        startMinutesNp = (NumberPicker) findViewById(R.id.start_minutes_np);
        endHourNp = (NumberPicker) findViewById(R.id.end_hour_np);
        endMinutesNp = (NumberPicker) findViewById(R.id.end_minutes_np);
        sendProposalButton = (Button) findViewById(R.id.send_proposal_button);

        dateLayout = (LinearLayout) findViewById(R.id.date_layout);
        placeLayout = (LinearLayout) findViewById(R.id.place_layout);
        timeLayout = (LinearLayout) findViewById(R.id.time_layout);

        startHourNp.setMinValue(0);
        startHourNp.setMaxValue(23);
        startMinutesNp.setMinValue(0);
        startMinutesNp.setMaxValue(59);
        endHourNp.setMinValue(0);
        endHourNp.setMaxValue(23);
        endMinutesNp.setMinValue(0);
        endMinutesNp.setMaxValue(59);


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

        if (toShow == EventManagementActivity.ToShow.DATE) {
            findViewById(R.id.time_full_layout).setVisibility(View.GONE);
        } else {
            findViewById(R.id.date_full_layout).setVisibility(View.GONE);
        }

        dateRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radio_date_defined:
                        dateLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio_date_not_defined:
                        dateLayout.setVisibility(GONE);
                }
            }
        });

        placeRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radio_place_defined:
                        placeLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio_place_not_defined:
                        placeLayout.setVisibility(GONE);
                }
            }
        });

        timeRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radio_time_defined:
                        timeLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radio_time_not_defined:
                        timeLayout.setVisibility(GONE);
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

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);

                if (startDateCalendar.getTime().before(cal.getTime())) {
                    Toast.makeText(getContext(), "The startDate must be at least today", Toast.LENGTH_SHORT).show();
                } else {
                    updateStartDateLabel();
                }

            }

        };

        endDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                endDateCalendar.set(Calendar.YEAR, year);
                endDateCalendar.set(Calendar.MONTH, monthOfYear);
                endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if(startDateEdit.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Select start date first", Toast.LENGTH_SHORT).show();
                } else {

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, monthOfYear);
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth-1);

                    if (startDateCalendar.getTime().before(cal.getTime())) {
                        updateEndDateLabel();
                    } else {
                        Toast.makeText(getContext(), "The date must be at least the same of the start date", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        };

        startDateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startDateEdit.setError(null);
                new DatePickerDialog(getContext(), startDate, startDateCalendar
                        .get(Calendar.YEAR), startDateCalendar.get(Calendar.MONTH),
                        startDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                endDateEdit.setError(null);
                new DatePickerDialog(getContext(), endDate, endDateCalendar
                        .get(Calendar.YEAR), endDateCalendar.get(Calendar.MONTH),
                        endDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ((ProgressBar) progressView).getIndeterminateDrawable().
                setColorFilter(ContextCompat.getColor(getContext(), R.color.green_sea), android.graphics.PorterDuff.Mode.MULTIPLY);


        this.sendProposalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSendProposal();
            }
        });

    }

    private void updateStartDateLabel() {

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormatType.DD_MM_YYYY_BACKSLASH.getFormat(), Locale.US);
        startDateEdit.setText(sdf.format(startDateCalendar.getTime()));
    }

    private void updateEndDateLabel() {

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormatType.DD_MM_YYYY_BACKSLASH.getFormat(), Locale.US);
        endDateEdit.setText(sdf.format(endDateCalendar.getTime()));
    }

    private void attemptSendProposal() {
        placeNameEdit.setError(null);
        placeProvinceEdit.setError(null);
        placeCityEdit.setError(null);
        placeAddressEdit.setError(null);
        startDateEdit.setError(null);
        endDateEdit.setError(null);

        // Store values at the time of the login attempt.

        Choice placeChoice;
        Choice dateChoice;
        Choice timeChoice;

        if (placeRadio.getCheckedRadioButtonId() == R.id.radio_place_defined) {
            placeChoice = Choice.DEFINED;
        } else {
            placeChoice = Choice.NOT_DEFINED;
        }

        if (dateRadio.getCheckedRadioButtonId() == R.id.radio_date_defined &&
                toShow == EventManagementActivity.ToShow.DATE) {
            dateChoice = Choice.DEFINED;
        } else {
            dateChoice = Choice.NOT_DEFINED;
        }
        if (timeRadio.getCheckedRadioButtonId() == R.id.radio_time_defined &&
                toShow == EventManagementActivity.ToShow.TIME) {
            timeChoice = Choice.DEFINED;
        } else {
            timeChoice = Choice.NOT_DEFINED;
        }


        String placeName = placeNameEdit.getText().toString();
        String placeProvince = placeProvinceEdit.getText().toString();
        String placeCity = placeCityEdit.getText().toString();
        String placeAddress = placeAddressEdit.getText().toString();
        String startDate = startDateEdit.getText().toString();
        String endDate = endDateEdit.getText().toString();
        String startHour = String.format("%02d", startHourNp.getValue());
        String endHour = String.format("%02d", endHourNp.getValue());
        String startMinutes = String.format("%02d", startMinutesNp.getValue());
        String endMinutes = String.format("%02d", endMinutesNp.getValue());
        String startTime = startHour + ":" + startMinutes;
        String endTime = endHour + ":" + endMinutes;

        boolean cancel = false;
        View focusView = null;

        if (placeChoice == Choice.DEFINED) {
            if (TextUtils.isEmpty(placeName)) {
                placeNameEdit.setError(getContext().getString(R.string.error_field_required));
                focusView = placeNameEdit;
                cancel = true;
            } else if (TextUtils.isEmpty(placeProvince)) {
                placeProvinceEdit.setError(getContext().getString(R.string.error_field_required));
                focusView = placeProvinceEdit;
                cancel = true;
            } else if (TextUtils.isEmpty(placeCity)) {
                placeCityEdit.setError(getContext().getString(R.string.error_field_required));
                focusView = placeCityEdit;
                cancel = true;
            }
        }
        if (!cancel && (dateChoice == Choice.DEFINED)) {
            if (TextUtils.isEmpty(startDate)) {
                startDateEdit.setError(getContext().getString(R.string.error_field_required));
                focusView = startDateEdit;
                cancel = true;
                makeProposalFormView.fullScroll(ScrollView.FOCUS_DOWN);
            } else if (TextUtils.isEmpty(endDate)) {
                endDateEdit.setError(getContext().getString(R.string.error_field_required));
                focusView = startDateEdit;
                cancel = true;
                makeProposalFormView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        } else if (!cancel && (timeChoice == Choice.DEFINED)) {
            if ((endHourNp.getValue() - startHourNp.getValue()) < 1) {
                Toast.makeText(getContext(), "The end time must be at least after 1 hour of the start time", Toast.LENGTH_SHORT).show();
                focusView = null;
                cancel = true;
                makeProposalFormView.fullScroll(ScrollView.FOCUS_DOWN);
            } else if ((endHourNp.getValue() - startHourNp.getValue()) == 1) {
                if (startMinutesNp.getValue() > endMinutesNp.getValue()) {
                    Toast.makeText(getContext(), "The end time must be at least after 1 hour of the start time", Toast.LENGTH_SHORT).show();
                    focusView = null;
                    cancel = true;
                }
            }
        }

        if (cancel) {
            if(focusView != null) {
                focusView.requestFocus();
            }

        } else {

            showProgress(true);

            SimpleDateFormat fromFormatter = new SimpleDateFormat(DateFormatType.DD_MM_YYYY_BACKSLASH.getFormat(), Locale.US);
            SimpleDateFormat toFormatter = new SimpleDateFormat(DateFormatType.YYYY_MM_DD_DASH.getFormat(), Locale.US);

            try {
                Date startDateFormatted = fromFormatter.parse(startDate);
                startDate = toFormatter.format(startDateFormatted);

                Date endDateFormatted = fromFormatter.parse(endDate);
                endDate = toFormatter.format(endDateFormatted);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if(placeChoice == Choice.DEFINED) {

                Log.d("proposal_", "start place task");

                maxTask++;
                Place place = new PlaceImpl.Builder()
                        .setNamePlace(placeName)
                        .setProvince(placeProvince)
                        .setCity(placeCity)
                        .setAddress(placeAddress)
                        .build();

                PlacePreferenceWrapper placePreferenceWrapper = new PlacePreferenceWrapperImpl(
                        place, LoggedAccount.getLoggedAccount().getId(), SelectedEvent.getSelectedEvent().getEventInfo().getEventId());

                MakeProposalDialog.InsertPlacePreferenceTask insertPlacePreferenceTask = new MakeProposalDialog.InsertPlacePreferenceTask();
                insertPlacePreferenceTask.execute(placePreferenceWrapper);
            }

            if (dateChoice == Choice.DEFINED) {

                Log.d("prop_", "start date task");

                maxTask++;
                DatePreferenceWrapper datePreferenceWrapper = new DatePreferenceWrapperImpl.Builder()
                        .setStartDate(startDate)
                        .setEndDate(endDate)
                        .setUserId(LoggedAccount.getLoggedAccount().getId())
                        .setEventId(SelectedEvent.getSelectedEvent().getEventInfo().getEventId())
                        .build();

                MakeProposalDialog.InsertDatePreferenceTask insertDatePreferenceTask = new MakeProposalDialog.InsertDatePreferenceTask();
                insertDatePreferenceTask.execute(datePreferenceWrapper);
            }

            if(timeChoice == Choice.DEFINED) {

                Log.d("proposal_", "start time task");

                maxTask++;
                TimePreferenceWrapper timePreferenceWrapper = new TimePreferenceWrapperImpl.Builder()
                        .setStartTime(startTime)
                        .setEndTime(endTime)
                        .setUserId(LoggedAccount.getLoggedAccount().getId())
                        .setEventId(SelectedEvent.getSelectedEvent().getEventInfo().getEventId())
                        .build();

                MakeProposalDialog.InsertTimePreferenceTask insertTimePreferenceTask = new MakeProposalDialog.InsertTimePreferenceTask();
                insertTimePreferenceTask.execute(timePreferenceWrapper);
            }

        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);

            if(makeProposalFormView == null) {
                Log.d("proposal_", "scroll view null");
            }

            sendProposalButton.setVisibility(show ? GONE : View.VISIBLE);
            makeProposalFormView.setVisibility(show ? GONE : View.VISIBLE);

            makeProposalFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    makeProposalFormView.setVisibility(show ? GONE : View.VISIBLE);
                }
            });

            sendProposalButton.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    sendProposalButton.setVisibility(show ? GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : GONE);
                }
            });
        } else {
            sendProposalButton.setVisibility(show ? GONE : View.VISIBLE);
            makeProposalFormView.setVisibility(show ? GONE : View.VISIBLE);

            progressView.setVisibility(show ? View.VISIBLE : GONE);
        }
    }



    private enum Choice {
        DEFINED, NOT_DEFINED
    }

    private synchronized void increaseTaskFinished() {
        nTaskFinished++;

        if(nTaskFinished == maxTask) {
            MakeProposalDialog.this.dismiss();
        }
    }



    public class InsertPlacePreferenceTask extends AsyncTask<PlacePreferenceWrapper,Void,RequestResult> {
        private String getPostDataString(HashMap<String,String > params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if(first)
                    first = false;
                else
                    result.append("&");
                result.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
            }

            return  result.toString();
        }

        //Nome dei parametri del json di risposta
        RequestResult toReturn  = null;
        HttpURLConnection httpURLConnection = null;
        StringBuilder response = new StringBuilder();
        BufferedReader rd = null;

        @Override
        protected void onPostExecute(RequestResult result) {
            if(result == RequestResult.INSERTED_PLACE_PREFERENCE ){
                increaseTaskFinished();
            }
        }

        @Override
        protected RequestResult doInBackground(PlacePreferenceWrapper... params) {
            try {

                URL url = new URL(Resource.BASE_URL+Resource.INSERT_PLACE_PREFERENCE_PAGE); //Enter URL here
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                //httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                HashMap<String,String> toPass = new HashMap<>();

                toPass.put("id_utente",params[0].getUserId());
                toPass.put("id_evento",params[0].getEventId());
                toPass.put("nome",params[0].getPlace().getNamePlace());
                toPass.put("indirizzo",params[0].getPlace().getAddress());
                toPass.put("citta",params[0].getPlace().getCity());
                toPass.put("provincia",params[0].getPlace().getProvince());

                writer.write(getPostDataString(toPass));
                writer.flush();
                writer.close();
                os.close();
                httpURLConnection.connect();
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == httpURLConnection.HTTP_OK){
                    InputStream inputStream = httpURLConnection.getInputStream();
                    rd = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = rd.readLine())!= null){
                        response.append(line);
                    }
                    System.out.println(response.toString());
                    JSONObject returned = new JSONObject(response.toString());
                    System.out.println(returned.getString("result"));
                    return RequestResult.INSERTED_PLACE_PREFERENCE;
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if( rd != null){
                    try {
                        rd.close();
                    }catch (Exception e){

                    }
                }
                if(httpURLConnection != null){
                    httpURLConnection.disconnect();
                }
            }

            return toReturn;
        }
    }



    public class InsertDatePreferenceTask extends AsyncTask<DatePreferenceWrapper,Void,RequestResult> {
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
        RequestResult toReturn = null;
        HttpURLConnection httpURLConnection = null;
        StringBuilder response = new StringBuilder();
        BufferedReader rd = null;

        @Override
        protected void onPostExecute(RequestResult result) {
            if (result == RequestResult.INSERTED_DATA_PREFERENCE) {
                increaseTaskFinished();
            }
        }

        @Override
        protected RequestResult doInBackground(DatePreferenceWrapper... params) {
            try {

                URL url = new URL(Resource.BASE_URL + Resource.INSERT_DATE_PREFERENCE_PAGE); //Enter URL here
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                //httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                HashMap<String, String> toPass = new HashMap<>();

                toPass.put("id_utente", params[0].getUserId());
                toPass.put("id_evento", params[0].getEventId());
                toPass.put("data_inizio", params[0].getStartDate());
                toPass.put("data_fine", params[0].getEndDate());

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
                    JSONObject returned = new JSONObject(response.toString());
                    System.out.println(returned.getString("result"));
                    return RequestResult.INSERTED_DATA_PREFERENCE;
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




    public class InsertTimePreferenceTask extends AsyncTask<TimePreferenceWrapper,Void,RequestResult> {
        private String getPostDataString(HashMap<String,String > params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if(first)
                    first = false;
                else
                    result.append("&");
                result.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
            }

            return  result.toString();
        }

        //Nome dei parametri del json di risposta
        RequestResult toReturn  = null;
        HttpURLConnection httpURLConnection = null;
        StringBuilder response = new StringBuilder();
        BufferedReader rd = null;

        @Override
        protected void onPostExecute(RequestResult result) {
            if(result == RequestResult.INSERTED_TIME_PREFERENCE ){
                increaseTaskFinished();
            }
        }

        @Override
        protected RequestResult doInBackground(TimePreferenceWrapper... params) {
            try {

                URL url = new URL(Resource.BASE_URL+Resource.INSERT_TIME_PREFERENCE_PAGE); //Enter URL here
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                //httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                HashMap<String,String> toPass = new HashMap<>();

                toPass.put("id_utente",params[0].getUserId());
                toPass.put("id_evento",params[0].getEventId());
                toPass.put("ora_inizio", params[0].getStartTime());
                toPass.put("ora_fine", params[0].getEndTime());

                writer.write(getPostDataString(toPass));
                writer.flush();
                writer.close();
                os.close();
                httpURLConnection.connect();
                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == httpURLConnection.HTTP_OK){
                    InputStream inputStream = httpURLConnection.getInputStream();
                    rd = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = rd.readLine())!= null){
                        response.append(line);
                    }
                    System.out.println(response.toString());
                    JSONObject returned = new JSONObject(response.toString());
                    System.out.println(returned.getString("result"));
                    return RequestResult.INSERTED_TIME_PREFERENCE;
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if( rd != null){
                    try {
                        rd.close();
                    }catch (Exception e){

                    }
                }
                if(httpURLConnection != null){
                    httpURLConnection.disconnect();
                }
            }

            return toReturn;
        }
    }
}
