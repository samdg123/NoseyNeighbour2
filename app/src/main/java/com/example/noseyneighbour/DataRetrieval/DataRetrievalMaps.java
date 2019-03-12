package com.example.noseyneighbour.DataRetrieval;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.example.noseyneighbour.Activities.MapsActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.PolygonOptions;

public class DataRetrievalMaps extends DataRetrievalParent {

    private GoogleMap googleMap;

    public DataRetrievalMaps(GoogleMap googleMap, String crimeType, int year, int month, float radius, Location globalLocation, Context context) {
        super(crimeType, year, month, radius, globalLocation, context);
        this.googleMap = googleMap;
    }

    @Override
    protected void onPostExecute(String response) {
        ((MapsActivity)context).getCurrentLocMapsFragment().setClusterManagerItems(crimes);

        googleMap.addPolygon(new PolygonOptions().add(northEast, southEast, southWest, northWest));
        Log.d("marker", "added polygon");
    }
}
