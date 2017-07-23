package com.example.luca.planit;

import android.os.AsyncTask;
import android.util.Log;

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
import java.util.Map;

/**
 * Created by diego on 22/07/2017.
 */

public class RegistrationTask extends AsyncTask<RegistrationData,Void,Result> {
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
        if(result.getResult().equals(RequestResult.OK_LOGIN)){
            //listener.onSuccessfulLogin(response.toString(),checkBox.isChecked());
        }else{
            //listener.onUnsuccessfulLogin();
        }
    }

    @Override
    protected Result doInBackground(RegistrationData... params) {
        try {
            URL url = new URL(Resource.BASE_URL+Resource.REGISTRATION_PAGE); //Enter URL here
            JSONObject returned = null;
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            //httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            HashMap<String,String> toPass = new HashMap<>();
            toPass.put("email",params[0].getEmail());
            toPass.put("password",params[0].getPassword());
            toPass.put("nome",params[0].getName());
            toPass.put("cognome",params[0].getSurname());
            toPass.put("username",params[0].getUsername());
            toPass.put("data_nascita",params[0].getBorndate());
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
                returned = new JSONObject(response.toString());
            }
            if(returned.getString("result").equals(RequestResult.USERNAME_ALREADY_EXISTING.toString())){
                toReturn = new Result(RequestResult.USERNAME_ALREADY_EXISTING);
                Log.d("risposta","Credenziali errate");
            }else if(returned.getString("result").equals(RequestResult.MAIL_ALREADY_PRESENT.toString())){
                toReturn = new Result(RequestResult.USERNAME_ALREADY_EXISTING);
                Log.d("risposta","Credenziali errate");
            }else{
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
                    toReturn = new Result(RequestResult.OK_REGISTRATION,loggedAccount);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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