package com.example.luca.planit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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


public class EventOrganizedFragment extends Fragment {

    private final List<ListViewItem> dataset = new LinkedList<>();
    private ArrayAdapter<ListViewItem> adapter;

    ListView listView;
    TextView noEventsTextView;
    TextView noConnectionTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_event_organized, container, false);

        listView = (ListView) rootView.findViewById(R.id.event_organized_list_view);
        noEventsTextView = (TextView) rootView.findViewById(R.id.no_organized_event_text);
        noConnectionTextView = (TextView) rootView.findViewById(R.id.no_connection_text);

        adapter = new EventListAdapter(getActivity(), R.layout.list_item, dataset);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventOrganizedFragment.this.getActivity(), EventManagementActivity.class);
                startActivity(intent);
            }
        });

        this.startTask();


        return rootView;
    }

    public void startTask(){
        OrganizedEventTask organizedEventTask = new OrganizedEventTask(getActivity());
        organizedEventTask.execute(LoggedAccount.getLoggedAccount());
    }

    private enum ItemType {
        LIST_VIEW, NO_EVENT, NO_CONNECTION
    }

    public class OrganizedEventTask extends AsyncTask<Account, Void, List<Event>> {

        private final Activity activity;
        private final Map<ItemType, View> items = new HashMap<>();

        public OrganizedEventTask(Activity activity) {
            this.activity = activity;

            items.put(ItemType.LIST_VIEW, listView);
            items.put(ItemType.NO_EVENT, noEventsTextView);
            items.put(ItemType.NO_CONNECTION, noConnectionTextView);
        }

        private void showItem(ItemType itemType) {
            for (Map.Entry<ItemType, View> e : items.entrySet()) {
                if (e.getKey() == itemType) {
                    if(items.get(e.getKey()).getVisibility() == View.GONE) {
                        items.get(e.getKey()).setVisibility(View.VISIBLE);
                    }
                } else {
                    if(items.get(e.getKey()).getVisibility() == View.VISIBLE) {
                        items.get(e.getKey()).setVisibility(View.GONE);
                    }
                }
            }
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
                ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    showItem(ItemType.NO_EVENT);
                } else {
                    showItem(ItemType.NO_CONNECTION);
                }
            } else {

                showItem(ItemType.LIST_VIEW);

                List<String> listId = new LinkedList<>();
                List<ListViewItem> listItemToRemove = new LinkedList<>();
                for(Event event : events){
                    listId.add(event.getEventInfo().getEventId());
                }
                for(ListViewItem item : dataset){
                    if(!listId.contains(item.getEventId())){
                        listItemToRemove.add(item);
                    }

                }

                if (!listItemToRemove.isEmpty()) {
                    dataset.clear();
                }

                for (int i=0; i<events.size(); i++) {
                    if (i%2==0) {
                        ListViewItem toAdd =  new ListViewItem(events.get(i).getEventInfo().getNameEvent(),
                                events.get(i).getEventInfo().getOrganizer().getOrganizerName(),
                                events.get(i).getEventInfo().getOrganizer().getOrganizerSurname(),
                                EventListAdapter.TYPE_LEFT,
                                events.get(i).getEventInfo().getEventId(),
                                LoggedAccount.getColor());
                        if(!dataset.contains(toAdd)){
                            dataset.add(toAdd);
                        }
                    } else {
                        ListViewItem toAdd =  new ListViewItem(events.get(i).getEventInfo().getNameEvent(),
                                events.get(i).getEventInfo().getOrganizer().getOrganizerName(),
                                events.get(i).getEventInfo().getOrganizer().getOrganizerSurname(),
                                EventListAdapter.TYPE_RIGHT,
                                events.get(i).getEventInfo().getEventId(),
                                LoggedAccount.getColor());
                        if(!dataset.contains(toAdd)){
                            dataset.add(toAdd);
                        }
                    }
                }

                adapter.notifyDataSetChanged();
            }
        }

        @Override
        protected List<Event> doInBackground(Account... params) {
            Log.d("event","Do in backGround");
            List<Event> listEvent = new LinkedList<>();
            try {
                URL url = new URL(Resource.BASE_URL + Resource.ORGANIZED_EVENT_PAGE); //Enter URL here
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
                toPass.put("id_org", params[0].getId());
                writer.write(getPostDataString(toPass));
                writer.flush();
                writer.close();
                os.close();
                httpURLConnection.connect();
                int responseCode = httpURLConnection.getResponseCode();
                Log.d("event",String.valueOf(responseCode));
                if (responseCode == httpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    rd = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println(response.toString());
                    Log.d("event",response.toString());
                    Log.d("event",params[0].toString());
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
                            .setOrganizer(new OrganizerImpl(params[0].getName(),params[0].getSurname()))
                            .build();

                    System.out.println(eventInfo.toString());
                    List<Guest> guests = new LinkedList<>();

                    JSONArray guestsJSon = event.getJSONArray("Invitati");

                    for (int j = 0; j < guestsJSon.length(); j++) {

                        JSONObject jsonObj = (JSONObject) guestsJSon.get(j);
                        GuestState state = jsonObj.getString("accettato").equals("0")?
                                GuestState.NOT_CONFIRMED:jsonObj.getString("accettato").equals("1")?
                                GuestState.CONFIRMED:GuestState.DECLINED;
                        Guest toAdd = new GuestImpl(jsonObj.getString("nome"),jsonObj.getString("cognome"),state);
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
