package com.example.luca.planit;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Luca on 23/07/2017.
 */

public class PlaceSuggestionAdapter extends ArrayAdapter<PlacePreference> {

    private final List<PlacePreference> dataset;

    public PlaceSuggestionAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<PlacePreference> objects) {
        super(context, resource, objects);

        dataset = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PlaceSuggestionViewHolder viewHolder = null;
        PlacePreference place = dataset.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_place_item,parent,false);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (PlaceSuggestionViewHolder) convertView.getTag();
        }

        TextView placeNameText = (TextView) convertView.findViewById(R.id.place_name_text);
        TextView placeProvinceText = (TextView) convertView.findViewById(R.id.place_province_text);
        TextView placeCityText = (TextView) convertView.findViewById(R.id.place_city_text);
        TextView placeAddressText = (TextView) convertView.findViewById(R.id.place_address_text);
        TextView numberProposalText = (TextView) convertView.findViewById(R.id.number_proposal);

        viewHolder = new PlaceSuggestionViewHolder(placeNameText, placeProvinceText, placeCityText,
                placeAddressText, numberProposalText);

        String placeName = dataset.get(position).getPlace().getNamePlace();
        String placeProvince = dataset.get(position).getPlace().getProvince();
        String placeCity = dataset.get(position).getPlace().getCity();
        String placeAddress = dataset.get(position).getPlace().getAddress();
        int numberProposal = dataset.get(position).getNumPreferences();


        viewHolder.getPlaceNameText().setText(placeName);

        if (placeProvince != null) {
            viewHolder.getPlaceProvinceText().setText(placeProvince);
        }
        if (placeCity != null) {
            viewHolder.getPlaceCityText().setText(placeCity);
        }
        if (placeAddress != null) {
            viewHolder.getPlaceAddressText().setText(placeAddress);
        }

        viewHolder.getSuggestionNumberText().setText(String.valueOf(numberProposal));

        return convertView;
    }

}
