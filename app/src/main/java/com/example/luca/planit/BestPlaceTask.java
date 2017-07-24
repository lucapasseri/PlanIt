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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by diego on 22/07/2017.
 */

public class BestPlaceTask extends AsyncTask<String,Void,List<PlacePreference>> {
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


    List<PlacePreference> toReturn ;
    HttpURLConnection httpURLConnection = null;
    StringBuilder response = new StringBuilder();
    BufferedReader rd = null;

    @Override
    protected void onPostExecute(List<PlacePreference> result) {
        if(!result.isEmpty() ){
            //listener.onSuccessfulLogin(response.toString(),checkBox.isChecked());
        }else{
            //listener.onUnsuccessfulLogin();
        }
    }

    @Override
    protected List<PlacePreference> doInBackground(String... params) {
        try {
            URL url = new URL(Resource.BASE_URL+Resource.GET_BEST_PLACE_PAGE); //Enter URL here
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
            toPass.put("id_evento",params[0]);
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
                returned = new JSONObject(response.toString());
            }
            List<PlacePreference> listPreferences = new LinkedList<>();
            JSONArray dates = returned.optJSONArray("Luoghi_Possibili");
            if (dates == null){
                JSONObject bestDate = returned.getJSONObject("Miglior_Luogo");
                url = new URL(Resource.BASE_URL + Resource.GET_PLACE_BY_ID); // Enter URL
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                //httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                os = httpURLConnection.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                toPass = new HashMap<>();
                toPass.put("id_luogo",String.valueOf(bestDate.getInt("id_luogo")));

                writer.write(getPostDataString(toPass));
                writer.flush();
                writer.close();
                os.close();
                httpURLConnection.connect();

                responseCode = httpURLConnection.getResponseCode();
                response = new StringBuilder();
                if(responseCode == httpURLConnection.HTTP_OK){
                    InputStream inputStream = httpURLConnection.getInputStream();
                    rd = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = rd.readLine())!= null){
                        response.append(line);
                    }
                    System.out.println(response.toString());
                    returned = new JSONObject(response.toString());
                    Place toAddPlace = new PlaceImpl.Builder()
                            .setAddress(returned.getString("indirizzo"))
                            .setCity(returned.getString("citta"))
                            .setProvince(returned.getString("provincia"))
                            .setNamePlace(returned.getString("nome_luogo"))
                            .build();
                    PlacePreference toAdd = new PlacePreferenceImpl(toAddPlace,bestDate.getInt("preferenze"));
                    listPreferences.add(toAdd);
                    System.out.println(toAdd.toString());
                }


            }else {
                for ( int i = 0; i< dates.length() ; i++){
                    JSONObject bestDate = (JSONObject) dates.get(i);
                    url = new URL(Resource.BASE_URL + Resource.GET_PLACE_BY_ID); // Enter URL
                    httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                    //httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                    os = httpURLConnection.getOutputStream();
                    writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                    toPass = new HashMap<>();
                    toPass.put("id_luogo",String.valueOf(bestDate.getInt("id_luogo")));

                    writer.write(getPostDataString(toPass));
                    writer.flush();
                    writer.close();
                    os.close();
                    httpURLConnection.connect();

                    responseCode = httpURLConnection.getResponseCode();
                    response = new StringBuilder();
                    if(responseCode == httpURLConnection.HTTP_OK){
                        InputStream inputStream = httpURLConnection.getInputStream();
                        rd = new BufferedReader(new InputStreamReader(inputStream));
                        String line = "";
                        while ((line = rd.readLine())!= null){
                            response.append(line);
                        }
                        System.out.println(response.toString());
                        returned = new JSONObject(response.toString());
                        Place toAddPlace = new PlaceImpl.Builder()
                                .setAddress(returned.getString("indirizzo"))
                                .setCity(returned.getString("citta"))
                                .setProvince(returned.getString("provincia"))
                                .setNamePlace(returned.getString("nome_luogo"))
                                .build();
                        PlacePreference toAdd = new PlacePreferenceImpl(toAddPlace,bestDate.getInt("preferenze"));
                        listPreferences.add(toAdd);
                        System.out.println(toAdd.toString());
                    }

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