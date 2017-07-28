package com.example.luca.planit;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
 * Created by diego on 26/07/2017.
 */

public class InviteToEventDialog extends Dialog {
    private EditText inviteEditText;
    private Button buttonInvite;

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
        this.setContentView(R.layout.custom_dialog);
        this.inviteEditText = (EditText) findViewById(R.id.invite_person_text);
        this.buttonInvite = (Button) findViewById(R.id.button_invite);
        this.buttonInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteEditText.setError(null);
                startTask();
            }
        });
    }
    public void startTask(){
        InviteToEventDialog.InviteToGroupTask inviteToGroupTask = new InviteToEventDialog.InviteToGroupTask(getContext());
        String insertedText = inviteEditText.getText().toString();
        if (insertedText.contains("@")) {
            InviteToGroupWrapper inviteToGroupWrapper = InviteToGroupWrapper.getGroupWrapperInstanceByEmail(
                    insertedText,
                    SelectedGroup.getSelectedGroup().getNameGroup(),
                    SelectedGroup.getSelectedGroup().getGroupId());
            inviteToGroupTask.execute(inviteToGroupWrapper);
        } else {
            InviteToGroupWrapper inviteToGroupWrapper = InviteToGroupWrapper.getGroupWrapperInstanceByUsername(
                    insertedText,
                    SelectedGroup.getSelectedGroup().getNameGroup(),
                    SelectedGroup.getSelectedGroup().getGroupId());
            inviteToGroupTask.execute(inviteToGroupWrapper);
        }
    }

    public class InviteToGroupTask extends AsyncTask<InviteToGroupWrapper, Void, RequestResult> {
        private final Context context;

        public InviteToGroupTask(Context context) {
            this.context = context;
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
        RequestResult toReturn = null;
        HttpURLConnection httpURLConnection = null;
        StringBuilder response = new StringBuilder();
        BufferedReader rd = null;

        @Override
        protected void onPostExecute(RequestResult result) {
            if (result == RequestResult.INVITE_SENDED) {
                InviteToEventDialog.this.dismiss();
            } else if (result == RequestResult.NOT_EXISTING_USERNAME) {
                inviteEditText.setError("This username doesn't exist");
                inviteEditText.requestFocus();
            } else {
                inviteEditText.setError("This email doesn't exist");
                inviteEditText.requestFocus();
            }
            //listener.onUnsuccessfulLogin();
        }

        @Override
        protected RequestResult doInBackground(InviteToGroupWrapper... params) {
            try {

                URL url = new URL(Resource.BASE_URL + Resource.INVITE_TO_GROUP_PAGE); //Enter URL here
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                //httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                HashMap<String, String> toPass = new HashMap<>();

                if (params[0].isMailGroupWrapper()) {
                    toPass.put("email", params[0].getEmail());
                } else {
                    toPass.put("username", params[0].getUsername());
                }

                toPass.put("id_gruppo", params[0].getGroupId());
                toPass.put("nome", params[0].getNameGroup());

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
                    String returnedResult = new JSONObject(response.toString()).getString("result");
                    if (returnedResult.equals(RequestResult.NOT_EXISTING_MAIL.toString())) {
                        toReturn = RequestResult.NOT_EXISTING_MAIL;
                    }
                    if (returnedResult.equals(RequestResult.NOT_EXISTING_USERNAME.toString())) {
                        toReturn = RequestResult.NOT_EXISTING_USERNAME;
                    }
                    if (returnedResult.equals(RequestResult.INVITE_SENDED.toString())) {
                        toReturn = RequestResult.INVITE_SENDED;
                        SelectedInvite.setSelectedInvite(params[0]);
                    }

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
}
