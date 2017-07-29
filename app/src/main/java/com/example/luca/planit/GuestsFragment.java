package com.example.luca.planit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GuestsFragment extends Fragment {

    private final List<GuestInEvent> dataset = new LinkedList<>();
    private ArrayAdapter<GuestInEvent> adapter;
    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_guests, container, false);


        listView = (ListView) rootView.findViewById(R.id.guest_list);
        adapter = new GuestListAdapter(getActivity(), R.layout.list_guest_item, dataset);
        listView.setAdapter(adapter);

        startTask();

        return rootView;
    }

    public void startTask() {
        GuestsFragment.GetInviteTask getUserGroupTask = new GuestsFragment.GetInviteTask(getActivity());
        getUserGroupTask.execute(LoggedAccount.getLoggedAccount());
    }


    public class GetInviteTask extends AsyncTask<Account, Void, List<GuestInEvent>> {


        private final Activity activity;

        public GetInviteTask(Activity activity) {
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
        protected void onPostExecute(List<GuestInEvent> listGuest) {

            if (listGuest.isEmpty()) {

            } else {
                dataset.clear();

                for (int i = 0; i < listGuest.size(); i++) {
                    GuestInEvent toAdd = listGuest.get(i);
                    dataset.add(toAdd);
                    adapter.notifyDataSetChanged();
                }
                    /*
                    List<String> listId = new LinkedList<>();
                    List<GuestInEvent> listItemToRemove = new LinkedList<>();

                    for (GuestInEvent guest : listGuest) {
                        listId.add(guest.getGuestId());
                    }

                    for (GuestInEvent item : dataset) {
                        if (!listId.contains(item.getGuestId())) {
                            listItemToRemove.add(item);
                        }

                    }

                    if (!listItemToRemove.isEmpty()) {
                        dataset.clear();
                    }

                    for (int i = 0; i < listGuest.size(); i++) {
                        GuestInEvent toAdd = listGuest.get(i);
                        if (!dataset.contains(toAdd)) {
                            dataset.add(toAdd);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    */
            }

        }

        @Override
        protected List<GuestInEvent> doInBackground(Account... params) {
            Log.d("task", "background");
            List<GuestInEvent> listGuest = new LinkedList<>();
            try {
                URL url = new URL(Resource.BASE_URL + Resource.PART_OF_EVENT_PAGE); //Enter URL here
                JSONObject returned = null;
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(2000);
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
                    //System.out.println(response.toString());
                    if (response.toString().isEmpty()) {
                        return listGuest;
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
                            .setOrganizer(new OrganizerImpl(eventInfoJson.getString("organizzatore_nome"), eventInfoJson.getString("organizzatore_cognome")))
                            .build();


                    List<Guest> guests = new LinkedList<>();
                    Log.d("GuestsFr",SelectedEvent.getSelectedEvent().toString());
                    Log.d("GuestsFr",eventInfo.toString());
                    if (eventInfo.getEventId().equals(SelectedEvent.getSelectedEvent().getEventInfo().getEventId())) {
                        Log.d("Dentro", eventInfo.toString());
                        JSONArray guestsJSon = event.getJSONArray("Invitati");
                        for (int j = 0; j < guestsJSon.length(); j++) {

                            JSONObject jsonObj = (JSONObject) guestsJSon.get(j);
                            GuestState state = jsonObj.getString("accettato").equals("0") ?
                                    GuestState.NOT_CONFIRMED : jsonObj.getString("accettato").equals("1") ?
                                    GuestState.CONFIRMED : GuestState.DECLINED;

                            GuestInEvent toAdd = new GuestInEvent(eventInfo.getEventId(),
                                    jsonObj.getString("id_utente"),
                                    state,
                                    eventInfo.getNameEvent(),
                                    jsonObj.getString("nome"),
                                    jsonObj.getString("cognome"),
                                    jsonObj.getString("username"));
                            System.out.println(toAdd.toString());
                            listGuest.add(toAdd);
                        }
                        return listGuest;
                    }

                }
            } catch (ConnectException eC) {
                eC.printStackTrace();
            } catch (ProtocolException e1) {
                e1.printStackTrace();
            } catch (SocketTimeoutException et) {
                et.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e2) {
                e2.printStackTrace();
            } finally {
                if (rd != null) {
                    try {
                        rd.close();
                    } catch (Exception e) {
                        return listGuest;
                    }
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return listGuest;
        }
    }
}
