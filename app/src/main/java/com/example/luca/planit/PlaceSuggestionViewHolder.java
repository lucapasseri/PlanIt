package com.example.luca.planit;

import android.widget.TextView;

/**
 * Created by Luca on 24/07/2017.
 */

public class PlaceSuggestionViewHolder {
    TextView placeNameText;
    TextView placeProvinceText;
    TextView placeCityText;
    TextView placeAddressText;
    TextView suggestionNumberText;

    public PlaceSuggestionViewHolder(TextView placeNameText, TextView placeProvinceText, TextView placeCityText,
                                     TextView placeAddressText, TextView suggestionNumberText) {
        this.placeNameText = placeNameText;
        this.placeProvinceText = placeProvinceText;
        this.placeCityText = placeCityText;
        this.placeAddressText = placeAddressText;
        this.suggestionNumberText = suggestionNumberText;
    }

    public TextView getPlaceNameText() {
        return placeNameText;
    }

    public TextView getPlaceProvinceText() {
        return placeProvinceText;
    }

    public TextView getPlaceCityText() {
        return placeCityText;
    }

    public TextView getPlaceAddressText() {
        return placeAddressText;
    }

    public TextView getSuggestionNumberText() {
        return suggestionNumberText;
    }
}
