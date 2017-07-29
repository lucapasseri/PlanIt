package com.example.luca.planit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

public class EventInfoFragment extends Fragment {

    private TextView nameText;
    private TextView placeNameText;
    private TextView placeProvinceText;
    private TextView placeCitytText;
    private TextView placeAddressText;
    private TextView dateText;
    private TextView timeText;

    private Dialog confirmDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView;

        Log.d("event_id", String.valueOf(SelectedEvent.getSelectedEvent().getEventInfo().getEventId()));

        if(OrganizedEvents.contains(SelectedEvent.getSelectedEvent().getEventInfo())) {

            Log.d("event_id", "id present");

            rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_event_info_organizer, container, false);

            ImageView infoSettingsImage = (ImageView) rootView.findViewById(R.id.info_settings);
            ImageView deleteEventImage = (ImageView) rootView.findViewById(R.id.delete_event);

            infoSettingsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(EventInfoFragment.this.getActivity(), PlanEventActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(getString(R.string.extra_from_info), true);
                    startActivity(intent);

                }
            });

            deleteEventImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    confirmDialog = new AlertDialog.Builder(getContext())
                            .setTitle("Delete Event")
                            .setMessage("Are you sure you want to delete event?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    DeleteEventTask deleteEventTask = new DeleteEventTask();
                                    deleteEventTask.execute(SelectedEvent.getSelectedEvent().getEventInfo().getEventId());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });



        } else {
            Log.d("event_id", "id not present");
            rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_event_info, container, false);
        }


        nameText = (TextView) rootView.findViewById(R.id.name_text);
        placeNameText = (TextView) rootView.findViewById(R.id.place_name_text);
        placeProvinceText = (TextView) rootView.findViewById(R.id.place_province_text);
        placeCitytText = (TextView) rootView.findViewById(R.id.place_city_text);
        placeAddressText = (TextView) rootView.findViewById(R.id.place_address_text);
        dateText = (TextView) rootView.findViewById(R.id.date_text);
        timeText = (TextView) rootView.findViewById(R.id.time_text);

        Event selectedEvent = SelectedEvent.getSelectedEvent();
        String nameEvent = selectedEvent.getEventInfo().getNameEvent();
        String placeName = selectedEvent.getEventInfo().getNamePlace();
        String placeProvince = selectedEvent.getEventInfo().getProvince();
        String placeCity = selectedEvent.getEventInfo().getCity();
        String placeAddress = selectedEvent.getEventInfo().getAddress();
        String date = selectedEvent.getEventInfo().getDate(DateFormatType.DD_MM_YYYY_BACKSLASH);
        String time = selectedEvent.getEventInfo().getTime();


        nameText.setText(nameEvent);
        if (placeName.isEmpty()) {
            placeNameText.setText(R.string.empty_field_message);
        } else {
            placeNameText.setText(placeName);
        }
        if (placeProvince.isEmpty()) {
            placeProvinceText.setText(R.string.empty_field_message);
        } else {
            placeProvinceText.setText(placeProvince);
        }
        if (placeCity.isEmpty()) {
            placeCitytText.setText(R.string.empty_field_message);
        } else {
            placeCitytText.setText(placeCity);
        }
        if (placeAddress.isEmpty()) {
            placeAddressText.setText(R.string.empty_field_message);
        } else {
            placeAddressText.setText(placeAddress);
        }
        if (date.isEmpty()) {
            dateText.setText(R.string.empty_field_message);
        } else {
            dateText.setText(date);
        }
        if (time.isEmpty()) {
            timeText.setText(R.string.empty_field_message);
        } else {
            timeText.setText(time);
        }


        return rootView;
    }





    public class DeleteEventTask extends AsyncTask<String,Void,RequestResult> {
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
            if(result == RequestResult.EVENT_DELETED ){
                Intent intent = new Intent(getContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }

        @Override
        protected RequestResult doInBackground(String... params) {
            try {

                URL url = new URL(Resource.BASE_URL+Resource.DELETE_EVENT_PAGE); //Enter URL here
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
                    return RequestResult.EVENT_DELETED;
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
