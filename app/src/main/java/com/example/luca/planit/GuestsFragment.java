package com.example.luca.planit;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

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

        dataset.add(new GuestInEvent("0", "1", GuestState.NOT_CONFIRMED, "carcere", "murf", "mellow", "muuuuurf"));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                dataset.clear();
                dataset.add(new GuestInEvent("0", "1", GuestState.CONFIRMED, "carcere", "murf", "mellow", "muuuuurf"));

                adapter.notifyDataSetChanged();
            }
        });


        return rootView;
    }
}
