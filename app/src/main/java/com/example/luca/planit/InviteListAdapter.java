package com.example.luca.planit;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.List;
import java.util.Map;

/**
 * Created by Luca on 23/07/2017.
 */

public class InviteListAdapter extends ArrayAdapter<ListInviteItem> {

    private final List<ListInviteItem> dataset;

    public InviteListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ListInviteItem> objects) {
        super(context, resource, objects);

        dataset = objects;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        GroupViewHolder viewHolder = null;
        ListInviteItem listViewItem = dataset.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_invites_item, parent, false);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        TextView textView = (TextView) convertView.findViewById(R.id.invite_text_view);
        ImageView imageConfirm = (ImageView) convertView.findViewById(R.id.confirm_img);
        ImageView imageDenied = (ImageView) convertView.findViewById(R.id.denied_img);
        viewHolder = new GroupViewHolder(textView);
        textView.setText(listViewItem.getInviteWrapper().getEventName());
        imageConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InviteListAdapter.UpdateInviteTask getUserGroupTask = new  InviteListAdapter.UpdateInviteTask(getContext());
                getUserGroupTask.execute(new InviteWrapper(
                        dataset.get(position).getInviteWrapper().getEventId(),
                        dataset.get(position).getInviteWrapper().getGuestId(),
                        GuestState.CONFIRMED,
                        dataset.get(position).getInviteWrapper().getEventName()
                ));
            }
        });
        imageDenied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InviteListAdapter.UpdateInviteTask getUserGroupTask = new  InviteListAdapter.UpdateInviteTask(getContext());
                getUserGroupTask.execute(new InviteWrapper(
                        dataset.get(position).getInviteWrapper().getEventId(),
                        dataset.get(position).getInviteWrapper().getGuestId(),
                        GuestState.DECLINED,
                        dataset.get(position).getInviteWrapper().getEventName()
                ));
            }
        });

        return convertView;
    }



    public class UpdateInviteTask extends AsyncTask<InviteWrapper,Void,String> {
        private final Context context;

        public UpdateInviteTask(Context context) {
            this.context = context;
        }

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
        String toReturn  = "";
        HttpURLConnection httpURLConnection = null;
        StringBuilder response = new StringBuilder();
        BufferedReader rd = null;

        @Override
        protected void onPostExecute(String result) {
            Log.d("Adapter",result);
            Intent intent = new Intent(context, InviteActivity.class);
            if(result!= null){
                intent.putExtra("INVITE",result);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            context.startActivity(intent);
        }

        @Override
        protected String doInBackground(InviteWrapper... params) {
            try {

                URL url = new URL(Resource.BASE_URL+Resource.ACCEPT_INVITE_PAGE); //Enter URL here
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
                toPass.put("id_utente",params[0].getGuestId());
                toPass.put("choice",params[0].getGuestState().getCode());

                writer.write(getPostDataString(toPass));
                writer.flush();
                writer.close();
                os.close();
                String state = params[0].getGuestState()==GuestState.CONFIRMED?" confirmed":" denied";
                toReturn = "Invite for \""+params[0].getEventName()+"\""+ state;
                Log.d("Return",toReturn);
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
                    String returnedString = new JSONObject(response.toString()).getString("response");
                    if( returnedString.equals(RequestResult.INVITE_UPDATED.toString())){
                        toReturn = params[0].getEventName();
                    }


                    return toReturn;
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
