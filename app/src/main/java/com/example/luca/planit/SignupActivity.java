package com.example.luca.planit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

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

public class SignupActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;

    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText emailEditText;
    private EditText usernameEditText;
    private EditText firstPasswordEditText;
    private EditText confirmPasswordEditText;
    private EditText bornDateEditText;
    private Button signupButton;
    private View progressView;
    private ScrollView signupFormView;

    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_signup);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        nameEditText = (EditText) findViewById(R.id.signup_name);
        surnameEditText = (EditText) findViewById(R.id.signup_surname);
        emailEditText = (EditText) findViewById(R.id.signup_email);
        usernameEditText = (EditText) findViewById(R.id.signup_username);
        firstPasswordEditText = (EditText) findViewById(R.id.signup_first_password);
        confirmPasswordEditText = (EditText) findViewById(R.id.signup_confirm_password);
        bornDateEditText = (EditText) findViewById(R.id.signup_born_date);

        signupButton = (Button) findViewById(R.id.signup_signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
            }
        });

        signupFormView = (ScrollView) findViewById(R.id.signup_form);
        progressView = findViewById(R.id.signup_progress);

        calendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        bornDateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                bornDateEditText.setError(null);
                new DatePickerDialog(SignupActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ((ProgressBar) progressView).getIndeterminateDrawable().
                setColorFilter(ContextCompat.getColor(this, R.color.green_sea), android.graphics.PorterDuff.Mode.MULTIPLY);

    }

    private void updateLabel() {

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormatType.DD_MM_YYYY_BACKSLASH.getFormat(), Locale.US);

        bornDateEditText.setText(sdf.format(calendar.getTime()));
    }


    private void attemptSignup() {

        // Reset errors.
        nameEditText.setError(null);
        surnameEditText.setError(null);
        emailEditText.setError(null);
        usernameEditText.setError(null);
        firstPasswordEditText.setError(null);
        confirmPasswordEditText.setError(null);
        bornDateEditText.setError(null);


        // Store values at the time of the login attempt.
        String name = nameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = firstPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String bornDate = bornDateEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError(getString(R.string.error_field_required));
            focusView = nameEditText;
            cancel = true;
        } else if (TextUtils.isEmpty(surname)) {
            surnameEditText.setError(getString(R.string.error_field_required));
            focusView = surnameEditText;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            emailEditText.setError(getString(R.string.error_field_required));
            focusView = emailEditText;
            cancel = true;
        } else if (TextUtils.isEmpty(username)) {
            usernameEditText.setError(getString(R.string.error_field_required));
            focusView = usernameEditText;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            firstPasswordEditText.setError(getString(R.string.error_field_required));
            focusView = firstPasswordEditText;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            confirmPasswordEditText.setError(getString(R.string.error_field_required));
            focusView = confirmPasswordEditText;
            cancel = true;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError(getString(R.string.error_password_not_matching));
            focusView = confirmPasswordEditText;
            cancel = true;
        } else if (TextUtils.isEmpty(bornDate)) {
            bornDateEditText.setError(getString(R.string.error_field_required));
            focusView = bornDateEditText;
            cancel = true;
            signupFormView.fullScroll(ScrollView.FOCUS_DOWN);
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            SimpleDateFormat fromFormatter = new SimpleDateFormat(DateFormatType.DD_MM_YYYY_BACKSLASH.getFormat(), Locale.US);
            SimpleDateFormat toFormatter = new SimpleDateFormat(DateFormatType.YYYY_MM_DD_DASH.getFormat(), Locale.US);

            try {
                Date date = fromFormatter.parse(bornDate);
                bornDate = toFormatter.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            RegistrationTask registrationTask = new RegistrationTask(this);
            registrationTask.execute(new RegistrationData.Builder()
                    .setName(name)
                    .setSurname(surname)
                    .setEmail(email)
                    .setUsername(username)
                    .setPassword(password)
                    .setBorndate(bornDate)
                    .build());
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

            signupButton.setVisibility(show ? View.GONE : View.VISIBLE);
            signupFormView.setVisibility(show ? View.GONE : View.VISIBLE);

            signupFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    signupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            signupButton.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    signupButton.setVisibility(show ? View.GONE : View.VISIBLE);
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
            signupButton.setVisibility(show ? View.GONE : View.VISIBLE);
            signupFormView.setVisibility(show ? View.GONE : View.VISIBLE);

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private class RegistrationTask extends AsyncTask<RegistrationData,Void,Result> {

        private final Context context;

        public RegistrationTask(Context context) {
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


        Result toReturn;
        HttpURLConnection httpURLConnection = null;
        StringBuilder response = new StringBuilder();
        BufferedReader rd = null;

        @Override
        protected void onPostExecute(Result result) {
            if (result.getResult() == RequestResult.NO_CONNECTION) {
                new AlertDialog.Builder(this.context)
                        .setMessage("Check your connection")
                        .setTitle("Internet not available")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }else {
                if (result.getResult().equals(RequestResult.OK_REGISTRATION)) {

                    Account account = result.getAccount();
                    String name = account.getName();
                    String surname = account.getSurname();
                    String email = account.getEmail();
                    String username = account.getUsername();
                    String password = account.getPassword();
                    String bornDate = account.getBorndate();
                    String id = account.getId();

                    SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString(getString(R.string.name_pref), name);
                    editor.putString(getString(R.string.surname_pref), surname);
                    editor.putString(getString(R.string.email_pref), email);
                    editor.putString(getString(R.string.username_pref), username);
                    editor.putString(getString(R.string.password_pref), password);
                    editor.putString(getString(R.string.born_date_pref), bornDate);
                    editor.putString(getString(R.string.id_pref), id);

                    editor.apply();

                    Intent intent = new Intent(getApplication(), HomeActivity.class);
                    intent.putExtra(getString(R.string.extra_from_signup), true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    showProgress(false);
                    if (result.getResult().equals(RequestResult.MAIL_ALREADY_PRESENT)) {
                        emailEditText.setError(getString(R.string.error_already_present_email));
                        emailEditText.requestFocus();
                    } else if (result.getResult().equals(RequestResult.USERNAME_ALREADY_EXISTING)) {
                        usernameEditText.setError(getString(R.string.error_already_existing_username));
                        usernameEditText.requestFocus();
                    }
                }
            }

        }

        @Override
        protected Result doInBackground(RegistrationData... params) {
            try {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    URL url = new URL(Resource.BASE_URL + Resource.REGISTRATION_PAGE); //Enter URL here
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
                    toPass.put("email", params[0].getEmail());
                    toPass.put("password", params[0].getPassword());
                    toPass.put("nome", params[0].getName());
                    toPass.put("cognome", params[0].getSurname());
                    toPass.put("username", params[0].getUsername());
                    toPass.put("data_nascita", params[0].getBorndate());
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
                        returned = new JSONObject(response.toString());
                    }
                    if (returned.getString("result").equals(RequestResult.USERNAME_ALREADY_EXISTING.toString())) {
                        toReturn = new Result(RequestResult.USERNAME_ALREADY_EXISTING);
                        Log.d("risposta", "Credenziali errate");
                    } else if (returned.getString("result").equals(RequestResult.MAIL_ALREADY_PRESENT.toString())) {
                        toReturn = new Result(RequestResult.MAIL_ALREADY_PRESENT);
                        Log.d("risposta", "Credenziali errate");
                    } else {
                        try {
                            Account loggedAccount = new AccountImpl.Builder()
                                    .setBorndate(params[0].getBorndate())
                                    .setEmail(params[0].getEmail())
                                    .setId(returned.getString("result"))
                                    .setName(params[0].getName())
                                    .setSurname(params[0].getSurname())
                                    .setPassword(params[0].getPassword())
                                    .setUsername(params[0].getUsername())
                                    .build();
                            toReturn = new Result(RequestResult.OK_REGISTRATION, loggedAccount);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }else {
                    return new Result(RequestResult.NO_CONNECTION);
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
