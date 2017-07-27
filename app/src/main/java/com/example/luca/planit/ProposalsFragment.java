package com.example.luca.planit;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ProposalsFragment extends Fragment {

    private RadioGroup radioGroup;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    private final List<PlacePreference> placeDataset = new LinkedList<>();
    private final List<Preference> dateDataset = new LinkedList<>();
    private final List<TimePreference> timeDataset = new LinkedList<>();


    private SelectedRadio selectedRadio = SelectedRadio.NONE;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_proposals, container, false);

        radioGroup = (RadioGroup) rootView.findViewById(R.id.proposal_radio);
        listView = (ListView) rootView.findViewById(R.id.proposal_list_view);


        placeDataset.add(new PlacePreferenceImpl(new PlaceImpl.Builder()
                .setNamePlace("casadeis")
                .setProvince("FC")
                .setCity("Cesena")
                .setAddress("via Boscone 715")
                .build(), 2, "0"));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (selectedRadio.getId() == checkedId) {
                  //  radioGroup.clearCheck();
                    listView.setVisibility(View.GONE);
                } else {

                    switch (checkedId) {
                        case R.id.radio_place:
                            selectedRadio = SelectedRadio.PLACES;
                            listView.setAdapter(new PlaceSuggestionAdapter(getActivity(), R.layout.list_place_item, placeDataset));
                            break;
                        case R.id.radio_date:
                            selectedRadio = SelectedRadio.DATES;
                            break;
                        case R.id.radio_hour:
                            selectedRadio = SelectedRadio.HOURS;
                            break;
                    }
                    listView.setVisibility(View.VISIBLE);

                }
            }
        });


        return rootView;
    }

    private enum SelectedRadio {
        PLACES(R.id.radio_date), DATES(R.id.radio_place), HOURS(R.id.radio_hour), NONE(0);

        private final int id;

        SelectedRadio(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
