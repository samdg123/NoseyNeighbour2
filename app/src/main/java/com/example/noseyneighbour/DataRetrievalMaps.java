package com.example.noseyneighbour;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.example.noseyneighbour.Activities.MapsActivity;
import com.example.noseyneighbour.Classes.Crime;
import com.example.noseyneighbour.Handlers.DBHandler;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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
