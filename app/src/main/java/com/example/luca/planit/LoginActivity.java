package com.example.luca.planit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    ArrayAdapter<String> adapter;

    private AutoCompleteTextView emailEditText;
    private EditText passwordEditText;
    private View progressView;
    private View loginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);

        emailEditText = (AutoCompleteTextView) findViewById(R.id.login_email);
        passwordEditText = (EditText) findViewById(R.id.login_password);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        List<String> emailList = new LinkedList<>();
        Set<String> emailPrefs = prefs.getStringSet(getString(R.string.email_list_pref), new HashSet<String>());

        for (String e : emailPrefs) {
            emailList.add(e);
        }

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, emailList);
        emailEditText.setAdapter(adapter);

        Button loginButton = (Button) findViewById(R.id.login_login_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button signupButton = (Button) findViewById(R.id.login_signup_button);
        signupButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);

        ((ProgressBar) progressView).getIndeterminateDrawable().
                setColorFilter(ContextCompat.getColor(this, R.color.green_sea), android.graphics.PorterDuff.Mode.MULTIPLY);

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        emailEditText.setError(null);
        passwordEditText.setError(null);

        // Store values at the time of the login attempt.
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError(getString(R.string.error_field_required));
            focusView = emailEditText;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.error_field_required));
            focusView = passwordEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            LoginTask loginTask = new LoginTask(this);
            loginTask.execute(LoginData.getLoginDataInstanceByEmail(email, password));
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

            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private class LoginTask extends AsyncTask<LoginData,Void,Result> {

        private final Context context;

        public LoginTask(Context context) {
            this.context = context;
        }

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


        Result toReturn ;
        HttpURLConnection httpURLConnection = null;
        StringBuilder response = new StringBuilder();
        BufferedReader rd = null;

        @Override
        protected void onPostExecute(Result result) {
            if(result.getResult()== (RequestResult.OK_LOGIN)){
                SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                Set<String> emailSet = new HashSet<>();
                Set<String> emailPrefs = prefs.getStringSet(getString(R.string.email_list_pref), new HashSet<String>());

                for (String e : emailPrefs) {
                    emailSet.add(e);
                }

                String emailResult = result.getAccount().getEmail();
                String passwordResult = result.getAccount().getPassword();

                emailSet.add(emailResult);

                editor.putStringSet(getString(R.string.email_list_pref), emailSet);

                editor.putString(getString(R.string.email_pref), emailResult);
                editor.putString(getString(R.string.password_pref), passwordResult);

                editor.apply();

                Intent intent = new Intent(getApplication(), HomeActivity.class);
                intent.putExtra(getString(R.string.extra_from_login), true);
                startActivity(intent);
                ((Activity) context).finish();
            } else {
                showProgress(false);
                if(result.getResult() == RequestResult.MAIL_NOT_PRESENT) {
                    emailEditText.setError(getString(R.string.error_invalid_email));
                    emailEditText.requestFocus();
                } else if(result.getResult() == RequestResult.WRONG_PASSWORD) {
                    passwordEditText.setError(getString(R.string.error_incorrect_password));
                    passwordEditText.requestFocus();
                }
            }
        }

        @Override
        protected Result doInBackground(LoginData... params) {
            try {
                URL url = new URL(Resource.BASE_URL+Resource.LOGIN_PAGE); //Enter URL here

                httpURLConnection = (HttpURLConnection)url.openConnection();

                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                //httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                HashMap<String,String> toPass = new HashMap<>();
                if(params[0].isMailLoginData()){
                    toPass.put("email",params[0].getEmail());
                }else{
                    toPass.put("username",params[0].getUsername());
                }
                toPass.put("password",params[0].getPassword());
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
                }
                if(response.toString().equals(RequestResult.MAIL_NOT_PRESENT.toString())){
                    toReturn = new Result(RequestResult.MAIL_NOT_PRESENT);
                    Log.d("risposta","MAIL_NOT_PRESENT");
                }else if(response.toString().equals(RequestResult.WRONG_PASSWORD.toString())){
                    toReturn = new Result(RequestResult.WRONG_PASSWORD);
                    Log.d("risposta","WRONG_PASSWORD");
                }else{
                    try {
                        JSONObject returned = new JSONObject(response.toString());
                        Log.d("risposta",response.toString());
                        Account loggedAccount = new AccountImpl.Builder()
                                .setBorndate(returned.getString("data_nascita"))
                                .setEmail(returned.getString("email"))
                                .setId(String.valueOf(returned.getInt("id")))
                                .setName(returned.getString("nome"))
                                .setSurname(returned.getString("cognome"))
                                .setPassword(returned.getString("password"))
                                .setUsername(returned.getString("username"))
                                .build();
                        toReturn = new Result(RequestResult.OK_LOGIN,loggedAccount);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
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

