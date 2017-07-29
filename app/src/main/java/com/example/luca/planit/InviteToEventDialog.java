package com.example.luca.planit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

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
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by diego on 26/07/2017.
 */

public class InviteToEventDialog extends Dialog {

    private List<Group> userGroups = new LinkedList<>();

    private TextInputLayout textInputLayout;
    private Spinner groupSpinner;
    private EditText inviteEditText;
    private Button buttonInvite;
    private RadioGroup guestRadio;

    private ArrayAdapter<Group> spinnerAdapter;

    protected InviteToEventDialog(@NonNull Context context) {
        super(context);
    }

    protected InviteToEventDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected InviteToEventDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.invite_to_event_dialog);

        textInputLayout = (TextInputLayout) findViewById(R.id.text_input_layout);
        groupSpinner = (Spinner) findViewById(R.id.spinner_group);
        guestRadio = (RadioGroup) findViewById(R.id.guest_radio_group);
        this.inviteEditText = (EditText) findViewById(R.id.invite_person_text);
        this.buttonInvite = (Button) findViewById(R.id.button_invite);

        InviteToEventDialog.GetUserGroupTask getUserGroupTask = new InviteToEventDialog.GetUserGroupTask();
        getUserGroupTask.execute(LoggedAccount.getLoggedAccount().getId());

        spinnerAdapter = new ArrayAdapter<Group>(getContext(), android.R.layout.simple_spinner_dropdown_item, userGroups);
        groupSpinner.setAdapter(spinnerAdapter);

        this.buttonInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteEditText.setError(null);
                if(guestRadio.getCheckedRadioButtonId() == R.id.radio_user) {
                    startInviteUserTask();
                } else if (guestRadio.getCheckedRadioButtonId() == R.id.radio_groups) {
                    startInviteGroupTask();
                }

            }
        });

        guestRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.radio_user) {
                    groupSpinner.setVisibility(View.GONE);
                    textInputLayout.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.radio_groups) {
                    groupSpinner.setVisibility(View.VISIBLE);
                    textInputLayout.setVisibility(View.GONE);
                }
            }
        });
    }


    public void startInviteUserTask(){
        InviteToEventDialog.InviteToEventTask inviteToEventTask = new InviteToEventDialog.InviteToEventTask();
        String guest = inviteEditText.getText().toString();
        if (guest.contains("@")) {
            EventInvite eventInvite = EventInviteImpl.getInviteByMail(
                    SelectedEvent.getSelectedEvent().getEventInfo().getEventId(), guest);
            inviteToEventTask.execute(eventInvite);
        } else {
            EventInvite inviteToGroupWrapper = EventInviteImpl.getInviteByUserName(
                    SelectedEvent.getSelectedEvent().getEventInfo().getEventId(), guest);
            inviteToEventTask.execute(inviteToGroupWrapper);
        }
    }

    public void startInviteGroupTask(){
        InviteToEventDialog.InviteGroupsToEventTask inviteGroupsToEventTask = new InviteToEventDialog.InviteGroupsToEventTask();
        String idGuest = spinnerAdapter.getItem(groupSpinner.getSelectedItemPosition()).getGroupId();

        inviteGroupsToEventTask.execute(new LinkedList<String>(Arrays.asList(idGuest)));
    }




    public class InviteToEventTask extends AsyncTask<EventInvite,Void,RequestResult> {
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
            if (result == RequestResult.INVITE_SENDED) {
                InviteToEventDialog.this.dismiss();
            } else if (result == RequestResult.NOT_EXISTING_USERNAME) {
                inviteEditText.setError(getContext().getString(R.string.user_not_e));
                inviteEditText.requestFocus();
            } else {
                inviteEditText.setError(getContext().getString(R.string.email_not_e));
                inviteEditText.requestFocus();
            }
            //listener.onUnsuccessfulLogin();
        }

        @Override
        protected RequestResult doInBackground(EventInvite... params) {
            try {

                URL url = new URL(Resource.BASE_URL+Resource.INVITE_TO_EVENT_PAGE); //Enter URL here
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
                if(params[0].isMailInvite()){
                    toPass.put("email", params[0].getGuestMail());
                }else {
                    toPass.put("username", params[0].getGuestUsername());
                }

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
                    String returnedResult = new JSONObject(response.toString()).getString("result");
                    if( returnedResult.equals(RequestResult.NOT_EXISTING_MAIL.toString())){
                        toReturn = RequestResult.NOT_EXISTING_MAIL;
                    }
                    if( returnedResult.equals(RequestResult.NOT_EXISTING_USERNAME.toString())){
                        toReturn = RequestResult.NOT_EXISTING_USERNAME;
                    }
                    if( returnedResult.equals(RequestResult.INVITE_SENDED.toString())){
                        toReturn = RequestResult.INVITE_SENDED;
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





    public class InviteGroupsToEventTask extends AsyncTask<List<String>, Void, RequestResult> {
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
        RequestResult toReturn = null;
        HttpURLConnection httpURLConnection = null;
        StringBuilder response = new StringBuilder();
        BufferedReader rd = null;

        @Override
        protected void onPostExecute(RequestResult result) {
            if (result == RequestResult.INVITE_SENDED) {
                InviteToEventDialog.this.dismiss();
            } else {
                inviteEditText.setError("This username doesn't exist");
                inviteEditText.requestFocus();
            }
        }

        @Override
        protected RequestResult doInBackground(List<String>... params) {
            List<String> idGroups = params[0];
            List<Group> listGroups = new LinkedList<>();
            try {
                URL url = new URL(Resource.BASE_URL + Resource.GET_GROUP_PAGE); // Enter
                // URL
                // here
                JSONObject returned = null;
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling
                // that it is a POST
                // request, which can be
                // changed into "PUT",
                // "GET", "DELETE" etc.
                // httpURLConnection.setRequestProperty("Content-Type",
                // "application/json"); // here you are setting the `Content-Type`
                // for the data you are sending which is `application/json`
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                HashMap<String, String> toPass = new HashMap<>();
                toPass.put("id_utente", LoggedAccount.getLoggedAccount().getId());
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
                    if (response.toString().isEmpty()) {
                    }
                    System.out.println(response.toString());
                    returned = new JSONObject(response.toString());
                }
                JSONArray groups = returned.getJSONArray("Gruppi");
                for (int i = 0; i < groups.length(); i++) {

                    JSONObject infoGroup = groups.getJSONObject(i);
                    JSONObject info = infoGroup.getJSONObject("Info");
                    if (idGroups.contains(info.getString("id_gruppo"))) {
                        final String groupName = info.getString("nome_gruppo");

                        List<Person> peopleInGroup = new LinkedList<>();

                        JSONArray peopleJSon = infoGroup.getJSONArray("Partecipanti");

                        for (int j = 0; j < peopleJSon.length(); j++) {
                            JSONObject person = (JSONObject) peopleJSon.get(j);

                            if (String.valueOf(person.getString("id")).equals(LoggedAccount.getLoggedAccount().getId())) {

                            } else {
                                httpURLConnection = null;
                                response = new StringBuilder();
                                rd = null;
                                url = new URL(Resource.BASE_URL + Resource.INVITE_TO_EVENT_PAGE); // Enter
                                // URL
                                returned = null;
                                httpURLConnection = (HttpURLConnection) url.openConnection();
                                httpURLConnection.setUseCaches(false);
                                httpURLConnection.setDoOutput(true);
                                httpURLConnection.setDoInput(true);
                                httpURLConnection.setRequestMethod("POST"); // here you are
                                // telling that
                                // it is a POST
                                // request,
                                // which can be
                                // changed into
                                // "PUT", "GET",
                                // "DELETE" etc.
                                // httpURLConnection.setRequestProperty("Content-Type",
                                // "application/json"); // here you are setting the
                                // `Content-Type` for the data you are sending which is
                                // `application/json`
                                os = httpURLConnection.getOutputStream();
                                toPass = new HashMap<>();
                                writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                                EventInvite invite = EventInviteImpl.getInviteByUserName(SelectedEvent.getSelectedEvent().getEventInfo().getEventId(), person.getString("username"));

                                toPass.put("id_evento", invite.getEventId());
                                if (invite.isMailInvite()) {
                                    toPass.put("email", invite.getGuestMail());
                                } else {
                                    toPass.put("username", invite.getGuestUsername());
                                }

                                writer.write(getPostDataString(toPass));
                                writer.flush();
                                writer.close();
                                os.close();
                                httpURLConnection.connect();
                                responseCode = httpURLConnection.getResponseCode();
                                if (responseCode == httpURLConnection.HTTP_OK) {
                                    InputStream inputStream = httpURLConnection.getInputStream();
                                    rd = new BufferedReader(new InputStreamReader(inputStream));
                                    String line = "";
                                    while ((line = rd.readLine()) != null) {
                                        response.append(line);
                                    }
                                    System.out.println(response.toString());
                                    returned = new JSONObject(response.toString());
                                    toReturn = RequestResult.INVITE_SENDED;
                                }

                                System.out.println(returned.getString("result"));
                            }


                        }
                    }


//				listGroups.add(new GroupImpl(peopleInGroup, groupName, info.getString("id_gruppo")));
//				System.out.println(listGroups.toString());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
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

            return toReturn;
        }
    }




    public class GetUserGroupTask extends AsyncTask<String, Void, List<Group>> {

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
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                userGroups.clear();
                userGroups.addAll(groups);
                spinnerAdapter.notifyDataSetChanged();
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
                e1.printStackTrace();
            }catch (SocketTimeoutException et) {
                return listGroups;
            } catch (IOException e1) {
                return listGroups;
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

            return listGroups;
        }
    }
}
