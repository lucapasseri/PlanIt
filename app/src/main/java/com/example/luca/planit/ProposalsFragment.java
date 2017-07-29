package com.example.luca.planit;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

public class ProposalsFragment extends Fragment {

    private RadioGroup radioGroup;
    private RadioButton radioPlace;
    private RadioButton radioDate;
    private RadioButton radioTime;
    private ListView listView;

    private final List<PlacePreference> placeDataset = new LinkedList<>();
    private final List<DatePreference> dateDataset = new LinkedList<>();
    private final List<TimePreference> timeDataset = new LinkedList<>();

    private PlaceSuggestionAdapter placeSuggestionAdapter;
    private DateSuggestionAdapter dateSuggestionAdapter;
    private TimeSuggestionAdapter timeSuggestionAdapter;


    private SelectedRadio selectedRadio = SelectedRadio.NONE;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_proposals, container, false);

        radioGroup = (RadioGroup) rootView.findViewById(R.id.proposal_radio_group);
        radioPlace = (RadioButton) rootView.findViewById(R.id.radio_place);
        radioDate = (RadioButton) rootView.findViewById(R.id.radio_date);
        radioTime = (RadioButton) rootView.findViewById(R.id.radio_time);
        listView = (ListView) rootView.findViewById(R.id.proposal_list_view);

        placeSuggestionAdapter = new PlaceSuggestionAdapter(getActivity(), R.layout.list_place_item, placeDataset);
        dateSuggestionAdapter = new DateSuggestionAdapter(getActivity(), R.layout.list_date_item, dateDataset);
        timeSuggestionAdapter = new TimeSuggestionAdapter(getActivity(), R.layout.list_time_item, timeDataset);


        radioPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean clear = computeClear(radioPlace);

                if (!clear) {
                    listView.setAdapter(placeSuggestionAdapter);
                    listView.setVisibility(View.VISIBLE);
                    selectedRadio = SelectedRadio.PLACES;


                }
                startTask(SelectedRadio.PLACES);
            }
        });

        radioDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean clear = computeClear(radioDate);

                if (!clear) {
                    listView.setAdapter(dateSuggestionAdapter);
                    listView.setVisibility(View.VISIBLE);
                    selectedRadio = SelectedRadio.DATES;
                }
                startTask(SelectedRadio.DATES);
            }
        });

        radioTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean clear = computeClear(radioTime);

                if (!clear) {
                    listView.setAdapter(timeSuggestionAdapter);
                    listView.setVisibility(View.VISIBLE);
                    selectedRadio = SelectedRadio.TIME;
                }
                startTask(SelectedRadio.TIME);
            }
        });


        return rootView;
    }

    private boolean computeClear (RadioButton radioButton) {
        if (radioButton.getId() == selectedRadio.getId()) {
            radioGroup.clearCheck();
            listView.setVisibility(View.GONE);
            selectedRadio = SelectedRadio.NONE;
            return true;
        } else {
            return false;
        }
    }

    public void startTask(SelectedRadio selectedRadio) {
        switch (selectedRadio) {
            case PLACES:
                ProposalsFragment.BestPlaceTask getPlaceProposalsGroupTask = new ProposalsFragment.BestPlaceTask();
                getPlaceProposalsGroupTask.execute(SelectedEvent.getSelectedEvent().getEventInfo().getEventId());
                break;
            case DATES:
                ProposalsFragment.BestDateTask bestDateTask = new ProposalsFragment.BestDateTask();
                bestDateTask.execute(SelectedEvent.getSelectedEvent().getEventInfo().getEventId());
                break;
            case TIME:
                ProposalsFragment.BestTimeTask bestTimeTask = new ProposalsFragment.BestTimeTask();
                bestTimeTask.execute(SelectedEvent.getSelectedEvent().getEventInfo().getEventId());
                break;
            default:
                return;
        }

    }

    private enum SelectedRadio {
        PLACES(R.id.radio_place), DATES(R.id.radio_date), TIME(R.id.radio_time), NONE(0);

        private final int id;

        SelectedRadio(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }






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


        List<PlacePreference> toReturn = new LinkedList<>() ;
        HttpURLConnection httpURLConnection = null;
        StringBuilder response = new StringBuilder();
        BufferedReader rd = null;

        @Override
        protected void onPostExecute(List<PlacePreference> result) {
            if(!result.isEmpty() ){

                Log.d("Proposal",result.toString());
                List<String> listId = new LinkedList<>();
                List<PlacePreference> listItemToRemove = new LinkedList<>();
                for(PlacePreference placePreference : result){
                    listId.add(placePreference.getidPlace());
                }
                for(PlacePreference item : placeDataset){
                    if(!listId.contains(item.getidPlace())){
                        listItemToRemove.add(item);
                    }
                }

                if (!listItemToRemove.isEmpty()) {
                    placeDataset.clear();
                }

                for (PlacePreference placePreference : result) {
                    if(!placeDataset.contains(placePreference)) {
                        placeDataset.add(placePreference);
                    }
                }

                placeSuggestionAdapter.notifyDataSetChanged();

            }else{
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
                Log.d("Prop","Task do inback");
                int responseCode = httpURLConnection.getResponseCode();
                Log.d("Prop",String.valueOf(responseCode));
                if(responseCode == httpURLConnection.HTTP_OK){
                    InputStream inputStream = httpURLConnection.getInputStream();
                    rd = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = rd.readLine())!= null){
                        response.append(line);
                    }
                    Log.d("Prop",response.toString());

                }
                if(!response.toString().isEmpty()){
                    returned = new JSONObject(response.toString());
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
                            PlacePreference toAdd = new PlacePreferenceImpl(toAddPlace,bestDate.getInt("preferenze"),String.valueOf(bestDate.getInt("id_luogo")));
                            toReturn.add(toAdd);
                            Log.d("Proposal",toAdd.toString());
                        }


                    }else {
                        for ( int i = 0; i< dates.length() ; i++) {
                            JSONObject bestDate = (JSONObject) dates.get(i);
                            url = new URL(Resource.BASE_URL + Resource.GET_PLACE_BY_ID); // Enter URL
                            httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setUseCaches(false);
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.setDoInput(true);
                            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                            //httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                            os = httpURLConnection.getOutputStream();
                            writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                            toPass = new HashMap<>();
                            toPass.put("id_luogo", String.valueOf(bestDate.getInt("id_luogo")));

                            writer.write(getPostDataString(toPass));
                            writer.flush();
                            writer.close();
                            os.close();
                            httpURLConnection.connect();

                            responseCode = httpURLConnection.getResponseCode();
                            response = new StringBuilder();
                            if (responseCode == httpURLConnection.HTTP_OK) {
                                InputStream inputStream = httpURLConnection.getInputStream();
                                rd = new BufferedReader(new InputStreamReader(inputStream));
                                String line = "";
                                while ((line = rd.readLine()) != null) {
                                    response.append(line);
                                }
                                Log.d("Proposal", response.toString());
                                returned = new JSONObject(response.toString());
                                Place toAddPlace = new PlaceImpl.Builder()
                                        .setAddress(returned.getString("indirizzo"))
                                        .setCity(returned.getString("citta"))
                                        .setProvince(returned.getString("provincia"))
                                        .setNamePlace(returned.getString("nome_luogo"))
                                        .build();
                                PlacePreference toAdd = new PlacePreferenceImpl(toAddPlace, bestDate.getInt("preferenze"), String.valueOf(bestDate.getInt("id_luogo")));
                                toReturn.add(toAdd);
                                Log.d("Proposal", toAdd.toString());
                            }
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














    public class BestTimeTask extends AsyncTask<String,Void,List<TimePreference>> {
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


        List<TimePreference> toReturn = new LinkedList<>() ;
        HttpURLConnection httpURLConnection = null;
        StringBuilder response = new StringBuilder();
        BufferedReader rd = null;

        @Override
        protected void onPostExecute(List<TimePreference> result) {
            if(!result.isEmpty() ){
                Log.d("Proposal",result.toString());
                List<String> listId = new LinkedList<>();
                List<TimePreference> listItemToRemove = new LinkedList<>();
                for(TimePreference timePreference : result){
                    listId.add(timePreference.getTime());
                }
                for(TimePreference item : timeDataset){
                    if(!listId.contains(item.getTime())){
                        listItemToRemove.add(item);
                    }
                }

                if (!listItemToRemove.isEmpty()) {
                    timeDataset.clear();
                }

                for (TimePreference timePreference : result) {
                    if(!timeDataset.contains(timePreference)) {
                        timeDataset.add(timePreference);
                    }
                }

                timeSuggestionAdapter.notifyDataSetChanged();
            }else{
                //listener.onUnsuccessfulLogin();
            }
        }

        @Override
        protected List<TimePreference> doInBackground(String... params) {
            try {
                URL url = new URL(Resource.BASE_URL+Resource.GET_BEST_TIME_PAGE); //Enter URL here
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
                    //.d("Proposal",response.toString());
                    returned = new JSONObject(response.toString());
                }

                JSONArray dates = returned.optJSONArray("Ore_Possibili");
                if (dates == null){
                    JSONObject bestDate = returned.optJSONObject("Miglior_Ora");
                    if(bestDate != null){
                        String time = bestDate.getString("ora");
                        String hour = time.substring(0,2);
                        String minutes = time.substring(2,4);
                        time = (hour+":"+minutes);
                        TimePreference toAdd = new TimePreferenceImpl(time,bestDate.getInt("preferenze"));
                        toReturn.add(toAdd);
                    }
                    //Log.d("Proposal",toAdd.toString());
                }else {
                    for ( int i = 0; i< dates.length() ; i++){
                        JSONObject bestDate = (JSONObject) dates.get(i);
                        String time = bestDate.getString("ora");
                        String hour = time.substring(0,2);
                        String minutes = time.substring(2,4);
                        time = (hour+":"+minutes);
                        TimePreference toAdd = new TimePreferenceImpl(time,bestDate.getInt("preferenze"));
                        toReturn.add(toAdd);
                        Log.d("Proposal",toAdd.toString());
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




    public class BestDateTask extends AsyncTask<String,Void,List<DatePreference>> {
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


        List<DatePreference> toReturn = new LinkedList<>() ;
        HttpURLConnection httpURLConnection = null;
        StringBuilder response = new StringBuilder();
        BufferedReader rd = null;

        @Override
        protected void onPostExecute(List<DatePreference> result) {
            if(!result.isEmpty() ){
                //Log.d("Proposal",result.toString());
                List<String> listId = new LinkedList<>();
                List<DatePreference> listItemToRemove = new LinkedList<>();
                for(DatePreference datePreference : result){
                    listId.add(datePreference.getDate());
                }
                for(DatePreference item : dateDataset){
                    if(!listId.contains(item.getDate())){
                        listItemToRemove.add(item);
                    }
                }

                if (!listItemToRemove.isEmpty()) {
                    dateDataset.clear();
                }

                for (DatePreference datePreference : result) {
                    if(!dateDataset.contains(datePreference)) {
                        dateDataset.add(datePreference);
                    }
                }

                dateSuggestionAdapter.notifyDataSetChanged();
            }else{
                //listener.onUnsuccessfulLogin();
            }
        }

        @Override
        protected List<DatePreference> doInBackground(String... params) {
            try {
                URL url = new URL(Resource.BASE_URL+Resource.GET_BEST_DATE_PAGE); //Enter URL here
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
                    //Log.d("Proposal",response.toString());

                }
                if(!response.toString().isEmpty()){
                    returned = new JSONObject(response.toString());
                    toReturn = new LinkedList<>();

                    JSONArray dates = returned.optJSONArray("Date_Possibili");
                    if (dates == null){
                        JSONObject bestDate = returned.getJSONObject("Miglior_Data");
                        DatePreferenceImpl toAdd = new DatePreferenceImpl(bestDate.getString("data"),bestDate.getInt("preferenze"));
                        toReturn.add(toAdd);
                        //Log.d("Proposal",toAdd.toString());
                    }else {
                        for ( int i = 0; i< dates.length() ; i++){
                            JSONObject bestDate = (JSONObject) dates.get(i);
                            DatePreferenceImpl toAdd = new DatePreferenceImpl(bestDate.getString("data"),bestDate.getInt("preferenze"));
                            toReturn.add(toAdd);
                            //Log.d("Proposal",toAdd.toString());
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
}
