package com.example.luca.planit;

import android.os.AsyncTask;

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

public class UpdateInviteTask extends AsyncTask<InviteWrapper,Void,RequestResult> {
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
        if(result !=null ){
            //listener.onSuccessfulLogin(response.toString(),checkBox.isChecked());
        }else{
            //listener.onUnsuccessfulLogin();
        }
    }

    @Override
    protected RequestResult doInBackground(InviteWrapper... params) {
        try {

            URL url = new URL(Resource.BASE_URL+Resource.ACCEPT_INVITE_PAGE); //Enter URL here
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            //httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            HashMap<String,String> toPass = new HashMap<>();


            toPass.put("id_evento",params[0].getEventId());
            toPass.put("id_utente",params[0].getGuestId());
            toPass.put("choice",params[0].getGuestState().getCode());

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
                String returnedString = new JSONObject(response.toString()).getString("response");
                if( returnedString.equals(RequestResult.INVITE_UPDATED.toString())){
                    toReturn = RequestResult.INVITE_UPDATED;
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