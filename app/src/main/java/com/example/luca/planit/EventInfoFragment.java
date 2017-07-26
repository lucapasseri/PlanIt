package com.example.luca.planit;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EventInfoFragment extends Fragment {

    TextView nameText;
    TextView placeText;
    TextView dateText;
    TextView timeText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_event_info, container, false);

        nameText = (TextView) rootView.findViewById(R.id.name_text);
        placeText = (TextView) rootView.findViewById(R.id.place_text);
        dateText = (TextView) rootView.findViewById(R.id.date_text);
        timeText = (TextView) rootView.findViewById(R.id.time_text);

        Event selectedEvent = SelectedEvent.getSelectedEvent();

        nameText.setText(selectedEvent.getEventInfo().getNameEvent());
        placeText.setText(selectedEvent.getEventInfo().getNamePlace());
        dateText.setText(selectedEvent.getEventInfo().getDate());
        timeText.setText(selectedEvent.getEventInfo().getTime());

        return rootView;
    }
}
