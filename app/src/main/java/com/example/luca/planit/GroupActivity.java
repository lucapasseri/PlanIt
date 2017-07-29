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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GroupActivity extends AppCompatActivity {
    private List<ListGroupItem> dataset = new LinkedList<>();
    private ArrayAdapter<ListGroupItem> adapter;
    ListView listView;
    private GroupDownloader groupDownloader;
    private boolean bounded;
    private ServiceConnection conn = new ServiceConnection (){
        @Override
        public void onServiceConnected (ComponentName cls , IBinder bnd ){
            groupDownloader = (( GroupDownloader.GroupDownloaderBinder ) bnd).getService();
            bounded = true ;
            groupDownloader.startMonitoring(GroupActivity.this);
        }
        @Override
        public void onServiceDisconnected ( ComponentName cls){
            bounded = false ;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group);
        if (!(getIntent().getStringExtra("TASK") == null)){
            Toast.makeText(this, getIntent().getStringExtra("TASK"), Toast.LENGTH_LONG).show();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        listView = (ListView) findViewById(R.id.list_group);
        adapter = new GroupListAdapter(this, R.layout.list_group_item,dataset);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectedGroup.setSelectedGroup(adapter.getItem(position).getGroup());
                Intent intent = new Intent(GroupActivity.this, GroupInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            startTask();
        }else{
            final Dialog d = new AlertDialog.Builder(this).setTitle(R.string.conn_err).setMessage(R.string.conn_un).setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Intent intent = new Intent(GroupActivity.this, HomeActivity.class);
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

        Intent intent = new Intent(this,GroupDownloader.class);
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CreateGroupDialog(GroupActivity.this).show();
            }
        });
    }
    public void startTask(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            GroupActivity.GetUserGroupTask getUserGroupTask = new GroupActivity.GetUserGroupTask(this);
            getUserGroupTask.execute(LoggedAccount.getLoggedAccount().getId());
        }else{

        }

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
        Intent intent = new Intent(this,GroupDownloader.class);
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class GetUserGroupTask extends AsyncTask<String, Void, List<Group>> {
        private final Activity activity;

        public GetUserGroupTask(Activity activity) {
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
        List<Group> listGroups = new LinkedList<>();
        HttpURLConnection httpURLConnection = null;
        StringBuilder response = new StringBuilder();
        BufferedReader rd = null;

        @Override
        protected void onPostExecute(List<Group> groups) {
            ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (groups.isEmpty()) {
                    //V
                } else {

                    List<String> listId = new LinkedList<>();
                    List<ListGroupItem> listItemToRemove = new LinkedList<>();

                    for(Group group : groups){
                        listId.add(group.getGroupId());
                    }
                    for(ListGroupItem item : dataset){
                        if(!listId.contains(item.getGroupId())){
                            listItemToRemove.add(item);
                        }

                    }

                    if (!listItemToRemove.isEmpty()) {
                        Log.d("Removing",listItemToRemove.toString());
                        dataset.clear();
                    }

                    for (Group group : groups){
                        ListGroupItem toAdd = new ListGroupItem(group.getNameGroup(),group.getGroupId(),group);
                        if(!dataset.contains(toAdd)){
                            dataset.add(toAdd);
                        }
                    }
                    Log.d("GruppiPost",dataset.toString());
                    adapter.notifyDataSetChanged();
                }
            }else{
                final Dialog d = new AlertDialog.Builder(activity).setTitle(R.string.conn_err).setMessage(R.string.conn_un).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Intent intent = new Intent(GroupActivity.this, HomeActivity.class);
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
        protected List<Group> doInBackground(String... params) {
            try {
                URL url = new URL(Resource.BASE_URL + Resource.GET_GROUP_PAGE); //Enter URL here
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

                    listGroups.add(new GroupImpl(peopleInGroup,groupName,info.getString("id_gruppo")));
                    Log.d("Gruppi",listGroups.toString());
                }
            } catch (ProtocolException e1) {
                return listGroups;
            }catch (SocketTimeoutException et) {
                return listGroups;
            } catch (IOException e1) {
                return listGroups;
            } catch (JSONException e1) {
                return listGroups;
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
}
