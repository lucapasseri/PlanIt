package com.example.luca.planit.future;

import android.os.AsyncTask;

import com.example.luca.planit.InsertTaskWrapper;
import com.example.luca.planit.Resource;
import com.example.luca.planit.TaskRequestWrapper;

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

public class GetTaskTask extends AsyncTask<TaskRequestWrapper,Void,List<InsertTaskWrapper>> {
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


    List<InsertTaskWrapper> listTask = new LinkedList<>();
    HttpURLConnection httpURLConnection = null;
    StringBuilder response = new StringBuilder();
    BufferedReader rd = null;

    @Override
    protected void onPostExecute(List<InsertTaskWrapper> result) {
        if(!result.isEmpty() ){
            //listener.onSuccessfulLogin(response.toString(),checkBox.isChecked());
        }else{
            //listener.onUnsuccessfulLogin();
        }
    }

    @Override
    protected List<InsertTaskWrapper> doInBackground(TaskRequestWrapper... params) {
        try {
            URL url = new URL(Resource.BASE_URL+Resource.GET_TASK_PAGE); //Enter URL here
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
            toPass.put("id_evento",params[0].getEventId());
            toPass.put("username",params[0].getUserName());


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
                if(response.toString().isEmpty()){
                    return listTask;
                }
                returned = new JSONObject(response.toString());
            }
            List<InsertTaskWrapper> listTask = new LinkedList<>();

            JSONArray tasks = returned.optJSONArray("Compiti");
            for ( int i = 0; i< tasks.length() ; i++){
                JSONObject task = (JSONObject) tasks.get(i);
                InsertTaskWrapper toAdd = new InsertTaskWrapper(params[0].getUserName(),task.getString("Compito"),params[0].getEventId());
                listTask.add(toAdd);
                System.out.println(toAdd.toString());
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

        return listTask;
    }
}