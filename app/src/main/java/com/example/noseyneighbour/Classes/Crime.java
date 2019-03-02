package com.example.noseyneighbour.Classes;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Crime implements ClusterItem {

    private int year;
    private int month;
    private LatLng position;
    private String category;
    private String outcomeStatus;

    public Crime(){}
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
    public int getYear() {
        return year;
    }
    public int getMonth() {
        return month;
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

    public void setPosition(LatLng position) {
        this.position = position;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setOutcomeStatus(String outcomeStatus) {
        this.outcomeStatus = outcomeStatus;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setMonth(int month) {
        this.month = month;
    }
}
