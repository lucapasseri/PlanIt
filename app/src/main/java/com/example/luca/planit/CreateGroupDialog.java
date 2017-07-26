package com.example.luca.planit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
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

public class CreateGroupDialog extends Dialog {
    private EditText inviteEditText;
    private Button buttonInvite;

    protected CreateGroupDialog(@NonNull Context context) {
        super(context);
    }

    protected CreateGroupDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected CreateGroupDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.create_group_dialog);
        this.inviteEditText = (EditText) findViewById(R.id.create_group_text);
        this.buttonInvite = (Button) findViewById(R.id.button_create_group);
        this.buttonInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTask();
            }
        });
    }

    public void startTask() {
        CreateGroupDialog.CreateGroupTask createGroupTask = new CreateGroupDialog.CreateGroupTask(getContext());
        String insertedText = inviteEditText.getText().toString();
        CreateGroupWrapper groupWrapper = new CreateGroupWrapper(LoggedAccount.getLoggedAccount().getId(), insertedText);
        createGroupTask.execute(groupWrapper);
    }

    public class CreateGroupTask extends AsyncTask<CreateGroupWrapper, Void, RequestResult> {
        private final Context context;

        public CreateGroupTask(Context context) {
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
            if (result == RequestResult.GROUP_CREATED) {
                CreateGroupDialog.this.dismiss();
            } else {
                new AlertDialog.Builder(getContext()).setTitle("Error!").setMessage("Unable to connect to the server").setPositiveButton("Ok", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        }

        @Override
        protected RequestResult doInBackground(CreateGroupWrapper... params) {
            try {

                URL url = new URL(Resource.BASE_URL + Resource.CREATE_GROUP_PAGE); //Enter URL here
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                //httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                HashMap<String, String> toPass = new HashMap<>();

                toPass.put("id_utente", params[0].getIdUtente());
                toPass.put("nome", params[0].getNomeGruppo());

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
                    JSONObject returned = new JSONObject(response.toString());
                    System.out.println(returned.getString("result"));
                    return RequestResult.GROUP_CREATED;
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
