package com.example.noseyneighbour.Classes;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Crime implements ClusterItem {

    private int year;
    private int month;
    private LatLng position;
    private String category;
    private String outcomeStatus;
    private String locationDesc;


    public Crime(){}
    public Crime(String category, double latitude, double longitude, String outcomeStatus, int year, int month, String locationDesc){
        this.category = category;
        position = new LatLng(latitude, longitude);
        this.outcomeStatus = outcomeStatus;
        this.year = year;
        this.month = month;
        this.locationDesc = locationDesc;
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
    public String getLocationDesc() {
        return locationDesc;
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
    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
    }
}
