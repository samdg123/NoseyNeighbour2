package com.example.noseyneighbour;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.noseyneighbour.Activities.GraphActivity;
import com.google.android.gms.maps.GoogleMap;

public class DataRetrievalGraph extends DataRetrievalParent {

    private Context context;
    private Activity activity;

    public DataRetrievalGraph(String crimeType, int year, int month, float radius, Location globalLocation, Context context, Activity activity) {
        super(crimeType, year, month, radius, globalLocation, context);
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (month == 12) {
            ((GraphActivity) activity).updateCrimeGraph();
        } else {
            ((GraphActivity) activity).incrementProgressBar();
        }
    }
}
