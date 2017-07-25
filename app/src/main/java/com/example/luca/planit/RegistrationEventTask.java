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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by diego on 22/07/2017.
 */

public class RegistrationEventTask extends AsyncTask<EventRegistrationWrapper, Void, EventInfo> {
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
            //listener.onSuccessfulLogin(response.toString(),checkBox.isChecked());
        } else {
            //listener.onUnsuccessfulLogin();
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
                            .setNamePlace(params[0].getPlace().getNamePlace())
                            .build();

                }
                if (params[0].getDate() != null) {
                    builder.setData(params[0].getDate());
                }
                if (params[0].getTime() != null) {
                    builder.setTime(params[0].getTime());
                }

                builder.setNameEvent(params[0].getName_event())
                        .setEventId(returned.getString("id_evento"))
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