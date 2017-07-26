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
        String nameEvent = selectedEvent.getEventInfo().getNameEvent();
        String placeName = selectedEvent.getEventInfo().getNamePlace();
        String placeProvince = selectedEvent.getEventInfo().getProvince();
        String placeCity = selectedEvent.getEventInfo().getCity();
        String placeAddress = selectedEvent.getEventInfo().getAddress();
        String date = selectedEvent.getEventInfo().getDate(DateFormatType.DD_MM_YYYY_BACKSLASH);
        String time = selectedEvent.getEventInfo().getTime();

        nameText.setText(nameEvent);
        if (placeName.equals(EventInfoImpl.EMPTY_FIELD)) {
            placeText.setText(placeName);
        } else {
            placeText.setText(R.string.empty_field_message);
        }
        if (placeName.equals(EventInfoImpl.EMPTY_FIELD)) {
            placeText.setText(placeName);
        } else {
            placeText.setText(R.string.empty_field_message);
        }
        if (placeName.equals(EventInfoImpl.EMPTY_FIELD)) {
            placeText.setText(placeName);
        } else {
            placeText.setText(R.string.empty_field_message);
        }
        dateText.setText(date);
        timeText.setText(time);


        return rootView;
    }
}
