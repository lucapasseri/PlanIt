package com.example.luca.planit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

        ViewGroup rootView;

        if(getActivity().getIntent().hasExtra(getString(R.string.extra_from_organized))) {
            rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_event_info_organizer, container, false);

            ImageView infoSettingsImage = (ImageView) rootView.findViewById(R.id.info_settings);

            infoSettingsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(EventInfoFragment.this.getActivity(), PlanEventActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(getString(R.string.extra_from_info), true);
                    startActivity(intent);

                }
            });



        } else {
            rootView = (ViewGroup) inflater.inflate(
                    R.layout.fragment_event_info, container, false);
        }


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


        nameText.setText(nameEvent);
        if (placeName.isEmpty()) {
            placeNameText.setText(R.string.empty_field_message);
        } else {
            placeNameText.setText(placeName);
        }
        if (placeProvince.isEmpty()) {
            placeProvinceText.setText(R.string.empty_field_message);
        } else {
            placeProvinceText.setText(placeProvince);
        }
        if (placeCity.isEmpty()) {
            placeCitytText.setText(R.string.empty_field_message);
        } else {
            placeCitytText.setText(placeCity);
        }
        if (placeAddress.isEmpty()) {
            placeAddressText.setText(R.string.empty_field_message);
        } else {
            placeAddressText.setText(placeAddress);
        }
        if (date.isEmpty()) {
            dateText.setText(R.string.empty_field_message);
        } else {
            dateText.setText(date);
        }
        if (time.isEmpty()) {
            timeText.setText(R.string.empty_field_message);
        } else {
            timeText.setText(time);
        }


        return rootView;
    }
}
