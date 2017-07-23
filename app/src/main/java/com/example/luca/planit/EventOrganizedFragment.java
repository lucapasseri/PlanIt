package com.example.luca.planit;

import android.content.Context;
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

import java.util.LinkedList;
import java.util.List;

import static android.R.id.list;


public class EventOrganizedFragment extends Fragment {

    private List<String> dataset = new LinkedList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.event_organized_fragment, container, false);

        for (int i=0; i<100; i++) {
            dataset.add("Event number " + String.valueOf(i));
        }

        ListView listView = (ListView) rootView.findViewById(R.id.event_organized_list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.textView, dataset);
        listView.setAdapter(adapter);

        return rootView;
    }
}
