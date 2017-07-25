package com.example.luca.planit;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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


public class EventTakePartFragment extends Fragment {

    private final List<ListViewItem> dataset = new LinkedList<>();
    private ArrayAdapter<ListViewItem> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.event_take_part_fragment, container, false);


        ListView listView = (ListView) rootView.findViewById(R.id.event_take_part_list_view);
        listView.setTextFilterEnabled(true);

        adapter = new EventTakePartListAdapter(getActivity(), R.layout.list_item, R.id.textView, dataset);
        listView.setAdapter(adapter);

        adapter = new EventTakePartListAdapter(getActivity(), R.layout.list_item, R.id.textView, dataset);
        listView.setAdapter(adapter);

        startTask();

        return rootView;
    }

    public void startTask(){
        TakePartEventTask takePartEventTask = new TakePartEventTask(getActivity());
        takePartEventTask.execute(LoggedAccount.getLoggedAccount());
    }

    public class TakePartEventTask extends AsyncTask<Account, Void, List<Event>> {


        private final Activity activity;
        private boolean eventListVisible = false;

        public TakePartEventTask(Activity activity) {
            this.activity = activity;
        }

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

        HttpURLConnection httpURLConnection = null;
        StringBuilder response = new StringBuilder();
        BufferedReader rd = null;

        @Override
        protected void onPostExecute(List<Event> events) {
            if (events.isEmpty()) {
                if(eventListVisible) {
                    ListView listView = (ListView) activity.findViewById(R.id.event_take_part_list_view);
                    TextView textView = (TextView) activity.findViewById(R.id.no_take_part_event_text);

                    listView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);

                    eventListVisible = false;
                }
            } else {

                if(!eventListVisible) {
                    ListView listView = (ListView) activity.findViewById(R.id.event_take_part_list_view);
                    TextView textView = (TextView) activity.findViewById(R.id.no_take_part_event_text);

                    listView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);

                    eventListVisible = true;
                }

                for (int i=0; i<events.size(); i++) {
                    if (i%2==0) {
                        ListViewItem toAdd =  new ListViewItem(events.get(i).getEventInfo().getNameEvent(),
                                EventOrganizedListAdapter.TYPE_LEFT,
                                events.get(i).getEventInfo().getEventId());
                        if(!dataset.contains(toAdd)){
                            dataset.add(toAdd);
                        }
                    } else {
                        ListViewItem toAdd =  new ListViewItem(events.get(i).getEventInfo().getNameEvent(),
                                EventOrganizedListAdapter.TYPE_RIGHT,
                                events.get(i).getEventInfo().getEventId());
                        if(!dataset.contains(toAdd)){
                            dataset.add(toAdd);
                        }
                    }

                    adapter.notifyDataSetChanged();
                }

            }
        }

        @Override
        protected List<Event> doInBackground(Account... params) {
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
                toPass.put("id_utente", params[0].getId());
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
                    if(response.toString().isEmpty()){
                        return listEvent;
                    }
                    returned = new JSONObject(response.toString());
                }

                JSONArray events = returned.getJSONArray("Eventi");
                for (int i = 0; i < events.length(); i++) {

                    JSONObject event = events.getJSONObject(i);

                    JSONObject eventInfoJson = (event.getJSONObject("Info"));

                    EventInfo eventInfo = new EventInfoImpl.Builder()
                            .setAddress(eventInfoJson.optString("indirizzo"))
                            .setCity(eventInfoJson.optString("citta"))
                            .setProvince(eventInfoJson.optString("provincia"))
                            .setNamePlace(eventInfoJson.optString("nome_luogo"))
                            .setCity(eventInfoJson.optString("citta"))
                            .setData(eventInfoJson.optString("data"))
                            .setNameEvent(eventInfoJson.getString("nome_evento"))
                            .setTime(eventInfoJson.optString("time"))
                            .setEventId(eventInfoJson.getString("id_evento"))
                            .setOrganizer(new OrganizerImpl(eventInfoJson.getString("organizzatore_nome"),eventInfoJson.getString("organizzatore_cognome")))
                            .build();

                    System.out.println(eventInfo.toString());
                    List<Guest> guests = new LinkedList<>();

                    JSONArray guestsJSon = event.getJSONArray("Invitati");

                    for (int j = 0; j < guestsJSon.length(); j++) {

                        JSONObject jsonObj = (JSONObject) guestsJSon.get(j);
                        boolean confirmed = jsonObj.getString("accettato").equals("1") ? true : false;
                        Guest toAdd = new GuestImpl(jsonObj.getString("nome"), jsonObj.getString("cognome"), confirmed);
                        guests.add(toAdd);
                        System.out.println(toAdd.toString());
                    }

                    listEvent.add(new EventImpl(eventInfo, guests));
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

            return listEvent;
        }
    }
}
