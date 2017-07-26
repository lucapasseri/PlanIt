package com.example.luca.planit;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EventInfoFragment extends Fragment {

    TextView nameText;
    TextView placeNameText;
    TextView placeProvinceText;
    TextView placeCitytText;
    TextView placeAddressText;
    TextView dateText;
    TextView timeText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_event_info, container, false);

        nameText = (TextView) rootView.findViewById(R.id.name_text);
        placeNameText = (TextView) rootView.findViewById(R.id.place_name_text);
        placeProvinceText = (TextView) rootView.findViewById(R.id.place_province_text);
        placeCitytText = (TextView) rootView.findViewById(R.id.place_city_text);
        placeAddressText = (TextView) rootView.findViewById(R.id.place_address_text);
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

        if(placeName.isEmpty()) {
            Log.d("place1", "ciaoo");
        }



        nameText.setText(nameEvent);
        if (placeName.equals(EventInfoImpl.EMPTY_FIELD)) {
            placeNameText.setText(R.string.empty_field_message);
        } else {
            placeNameText.setText(placeName);
        }
        if (placeProvince.equals(EventInfoImpl.EMPTY_FIELD)) {
            placeProvinceText.setText(R.string.empty_field_message);
        } else {
            placeProvinceText.setText(placeProvince);
        }
        if (placeCity.equals(EventInfoImpl.EMPTY_FIELD)) {
            placeCitytText.setText(R.string.empty_field_message);
        } else {
            placeCitytText.setText(placeCity);
        }
        if (placeAddress.equals(EventInfoImpl.EMPTY_FIELD)) {
            placeAddressText.setText(R.string.empty_field_message);
        } else {
            placeAddressText.setText(placeAddress);
        }
        if (date.equals(EventInfoImpl.EMPTY_FIELD)) {
            dateText.setText(R.string.empty_field_message);
        } else {
            dateText.setText(date);
        }
        if (time.equals(EventInfoImpl.EMPTY_FIELD)) {
            timeText.setText(R.string.empty_field_message);
        } else {
            timeText.setText(time);
        }


        return rootView;
    }
}
