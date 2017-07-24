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
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by diego on 22/07/2017.
 */

public class GetUserGroupTask extends AsyncTask<String, Void, List<Group>> {
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
    List<Group> listGroups = new LinkedList<>();
    HttpURLConnection httpURLConnection = null;
    StringBuilder response = new StringBuilder();
    BufferedReader rd = null;

    @Override
    protected void onPostExecute(List<Group> groups) {
        if (groups.isEmpty()) {
            //Vuoto
        } else {
            //
        }
    }

    @Override
    protected List<Group> doInBackground(String... params) {
        List<Event> listEvent = new LinkedList<>();
        try {
            URL url = new URL(Resource.BASE_URL + Resource.PART_OF_EVENT_PAGE); //Enter URL here
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
            toPass.put("id_utente",params[0]);
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
                if(response.toString().isEmpty()){
                    return listGroups;
                }
                System.out.println(response.toString());
                returned = new JSONObject(response.toString());
            }
            JSONArray groups = returned.getJSONArray("Gruppi");
            for ( int i = 0; i< groups.length() ; i++){

                JSONObject infoGroup = groups.getJSONObject(i);
                JSONObject info = infoGroup.getJSONObject("Info");

                final String groupName = info.getString("nome_gruppo");

                List<Person> peopleInGroup = new LinkedList<>();

                JSONArray peopleJSon = infoGroup.getJSONArray("Partecipanti");

                for ( int j = 0; j< peopleJSon.length() ; j++){
                    JSONObject person = (JSONObject) peopleJSon.get(j);
                    peopleInGroup.add(new PersonImpl(person.getString("nome"),person.getString("cognome")));
                }

                listGroups.add(new GroupImpl(peopleInGroup,groupName));

            }
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
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

        return listGroups;
    }
}