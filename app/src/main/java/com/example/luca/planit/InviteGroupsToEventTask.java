package com.example.luca.planit;

import android.os.AsyncTask;

import org.json.JSONArray;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by diego on 22/07/2017.
 */

public class InviteGroupsToEventTask extends AsyncTask<List<String>, Void, RequestResult> {
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
        if (result != null) {
            //listener.onSuccessfulLogin(response.toString(),checkBox.isChecked());
        } else {
            //listener.onUnsuccessfulLogin();
        }
    }

    @Override
    protected RequestResult doInBackground(List<String>... params) {
        List<String> idGroups = params[0];
        List<Group> listGroups = new LinkedList<>();
        try {
            URL url = new URL(Resource.BASE_URL + Resource.GET_GROUP_PAGE); // Enter
            // URL
            // here
            JSONObject returned = null;
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST"); // here you are telling
            // that it is a POST
            // request, which can be
            // changed into "PUT",
            // "GET", "DELETE" etc.
            // httpURLConnection.setRequestProperty("Content-Type",
            // "application/json"); // here you are setting the `Content-Type`
            // for the data you are sending which is `application/json`
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            HashMap<String, String> toPass = new HashMap<>();
            toPass.put("id_utente", LoggedAccount.getLoggedAccount().getId());
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
                if (response.toString().isEmpty()) {
                }
                System.out.println(response.toString());
                returned = new JSONObject(response.toString());
            }
            JSONArray groups = returned.getJSONArray("Gruppi");
            for (int i = 0; i < groups.length(); i++) {

                JSONObject infoGroup = groups.getJSONObject(i);
                JSONObject info = infoGroup.getJSONObject("Info");
                if (idGroups.contains(info.getString("id_gruppo"))) {
                    final String groupName = info.getString("nome_gruppo");

                    List<Person> peopleInGroup = new LinkedList<>();

                    JSONArray peopleJSon = infoGroup.getJSONArray("Partecipanti");

                    for (int j = 0; j < peopleJSon.length(); j++) {
                        JSONObject person = (JSONObject) peopleJSon.get(j);

                        if (String.valueOf(person.getString("id")).equals(LoggedAccount.getLoggedAccount().getId())) {

                        } else {
                            httpURLConnection = null;
                            response = new StringBuilder();
                            rd = null;
                            url = new URL(Resource.BASE_URL + Resource.INVITE_TO_EVENT_PAGE); // Enter
                            // URL
                            returned = null;
                            httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setUseCaches(false);
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.setDoInput(true);
                            httpURLConnection.setRequestMethod("POST"); // here you are
                            // telling that
                            // it is a POST
                            // request,
                            // which can be
                            // changed into
                            // "PUT", "GET",
                            // "DELETE" etc.
                            // httpURLConnection.setRequestProperty("Content-Type",
                            // "application/json"); // here you are setting the
                            // `Content-Type` for the data you are sending which is
                            // `application/json`
                            os = httpURLConnection.getOutputStream();
                            toPass = new HashMap<>();
                            writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                            EventInvite invite = EventInviteImpl.getInviteByUserName(SelectedEvent.getSelectedEvent().getEventInfo().getEventId(), person.getString("username"));

                            toPass.put("id_evento", invite.getEventId());
                            if (invite.isMailInvite()) {
                                toPass.put("email", invite.getGuestMail());
                            } else {
                                toPass.put("username", invite.getGuestUsername());
                            }

                            writer.write(getPostDataString(toPass));
                            writer.flush();
                            writer.close();
                            os.close();
                            httpURLConnection.connect();
                            responseCode = httpURLConnection.getResponseCode();
                            if (responseCode == httpURLConnection.HTTP_OK) {
                                InputStream inputStream = httpURLConnection.getInputStream();
                                rd = new BufferedReader(new InputStreamReader(inputStream));
                                String line = "";
                                while ((line = rd.readLine()) != null) {
                                    response.append(line);
                                }
                                System.out.println(response.toString());
                                returned = new JSONObject(response.toString());
                                toReturn = RequestResult.INVITE_SENDED;
                            }

                            System.out.println(returned.getString("result"));
                        }


                    }
                }


//				listGroups.add(new GroupImpl(peopleInGroup, groupName, info.getString("id_gruppo")));
//				System.out.println(listGroups.toString());
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