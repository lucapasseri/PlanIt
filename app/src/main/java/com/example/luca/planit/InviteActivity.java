package com.example.luca.planit;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InviteActivity extends AppCompatActivity {
    private List<ListInviteItem> dataset = new LinkedList<>();
    private ArrayAdapter<ListInviteItem> adapter;
    ListView listView;
    private InviteDownloader inviteDownloader;
    private boolean bounded;
    private ServiceConnection conn = new ServiceConnection (){
        @Override
        public void onServiceConnected (ComponentName cls , IBinder bnd ){
            inviteDownloader = (( InviteDownloader.InviteDownloaderBinder ) bnd).getService();
            bounded = true ;
            inviteDownloader.startMonitoring(InviteActivity.this);
        }
        @Override
        public void onServiceDisconnected ( ComponentName cls){
            bounded = false ;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Log.d("On_create",String.valueOf(getIntent().getStringExtra("INVITE") == null));
        if (!(getIntent().getStringExtra("INVITE") == null)){
            Toast.makeText(this, getIntent().getStringExtra("INVITE"), Toast.LENGTH_LONG).show();
        }
        listView = (ListView) findViewById(R.id.list_invites);
        adapter = new InviteListAdapter(this, R.layout.list_invites_item, dataset);
        listView.setAdapter(adapter);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            startTask();
        }else{
            final Dialog d = new AlertDialog.Builder(this).setTitle(R.string.conn_err).setMessage(R.string.conn_un).setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Intent intent = new Intent(InviteActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }).setPositiveButton(R.string.ok_check, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).create();
            d.show();
        }

        Intent intent = new Intent(this,InviteDownloader.class);
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(bounded){
            unbindService(conn);
            bounded = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(bounded){
            unbindService(conn);
            bounded = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this,InviteDownloader.class);
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }

    public void startTask(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            InviteActivity.GetInviteTask getUserGroupTask = new  InviteActivity.GetInviteTask(this);
            getUserGroupTask.execute(LoggedAccount.getLoggedAccount());
        }else{
        }

    }

    public class GetInviteTask extends AsyncTask<Account, Void, List<InviteWrapper>> {


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
        protected void onPostExecute(List<InviteWrapper> invites) {
            ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (invites.isEmpty()) {

                } else {

                    List<String> listId = new LinkedList<>();
                    List<ListInviteItem> listItemToRemove = new LinkedList<>();

                    for (InviteWrapper invite : invites) {
                        listId.add(invite.getEventId());
                    }

                    for (ListInviteItem item : dataset) {
                        if (!listId.contains(item.getInviteWrapper().getEventId())) {
                            listItemToRemove.add(item);
                        }

                    }

                    if (!listItemToRemove.isEmpty()) {
                        dataset.clear();
                    }

                    for (int i = 0; i < invites.size(); i++) {
                        ListInviteItem toAdd = new ListInviteItem(invites.get(i));

                        if (!dataset.contains(toAdd)) {
                            dataset.add(toAdd);
                        }

                        adapter.notifyDataSetChanged();
                    }


                }
            } else {
                final Dialog d = new AlertDialog.Builder(activity).setTitle(R.string.conn_err).setMessage(R.string.conn_un).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Intent intent = new Intent(InviteActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }).setPositiveButton(R.string.ok_check, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
                d.show();
            }

        }

        @Override
        protected List<InviteWrapper> doInBackground(Account... params) {
            List<InviteWrapper> listInviteWrapper = new LinkedList<>();
            try {
                URL url = new URL(Resource.BASE_URL + Resource.PART_OF_EVENT_PAGE); //Enter URL here
                JSONObject returned = null;
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(3000);
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
                    if (response.toString().isEmpty()) {
                        return listInviteWrapper;
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

                    System.out.println(eventInfo.toString());
                    List<Guest> guests = new LinkedList<>();

                    JSONArray guestsJSon = event.getJSONArray("Invitati");
                    GuestState mystate = null;
                    for (int j = 0; j < guestsJSon.length(); j++) {

                        JSONObject jsonObj = (JSONObject) guestsJSon.get(j);
                        GuestState state = jsonObj.getString("accettato").equals("0") ?
                                GuestState.NOT_CONFIRMED : jsonObj.getString("accettato").equals("1") ?
                                GuestState.CONFIRMED : GuestState.DECLINED;
                        if (params[0].getId().equals(jsonObj.getString("id_utente"))) {
                            mystate = state;
                        }
                        Guest toAdd = new GuestImpl(jsonObj.getString("nome"), jsonObj.getString("cognome"), state);
                        guests.add(toAdd);
                        System.out.println(toAdd.toString());
                    }
                    if (mystate == GuestState.NOT_CONFIRMED) {
                        listInviteWrapper.add(new InviteWrapper(eventInfo.getEventId(), params[0].getId(), mystate, eventInfo.getNameEvent()));
                    }
                }
            }catch (ConnectException eC){
                return listInviteWrapper;
            }
            catch (ProtocolException e1) {
                return listInviteWrapper;
            }catch (SocketTimeoutException et) {
                return listInviteWrapper;
            }  catch (IOException e1) {
                return listInviteWrapper;
            } catch (JSONException e1) {
                return listInviteWrapper;
            } finally {
                if (rd != null) {
                    try {
                        rd.close();
                    } catch (Exception e) {
                        return listInviteWrapper;
                    }
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return listInviteWrapper;
        }
    }

}
