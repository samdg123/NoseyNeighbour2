package com.example.noseyneighbour;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Crime implements ClusterItem {
    private LatLng position;
    private String category;
    private String outcomeStatus;

    public Crime(String category, double latitude, double longitude, String outcomeStatus){
        this.category = category;
        position = new LatLng(latitude, longitude);
        this.outcomeStatus = outcomeStatus;
    }


    public String getCategory() {
        return category;
    }

    public String getOutcomeStatus() {
        return outcomeStatus;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return category;
    }

    @Override
    public String getSnippet() {
        return outcomeStatus;
    }
}
