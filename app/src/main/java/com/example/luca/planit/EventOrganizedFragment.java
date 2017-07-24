package com.example.luca.planit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;


public class EventOrganizedFragment extends Fragment {

    private List<ListViewItem> dataset = new LinkedList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.event_organized_fragment, container, false);

        for (int i=0; i<100; i++) {
            if (i%2==0) {
                dataset.add(new ListViewItem("Event number " + String.valueOf(i), EventOrganizedListAdapter.TYPE_LEFT));
            } else {
                dataset.add(new ListViewItem("Event number " + String.valueOf(i), EventOrganizedListAdapter.TYPE_RIGHT));
            }

        }

        ListView listView = (ListView) rootView.findViewById(R.id.event_organized_list_view);

        ArrayAdapter<ListViewItem> adapter = new EventOrganizedListAdapter(getActivity(), R.layout.list_item, R.id.textView, dataset);
        listView.setAdapter(adapter);

        return rootView;
    }
}
