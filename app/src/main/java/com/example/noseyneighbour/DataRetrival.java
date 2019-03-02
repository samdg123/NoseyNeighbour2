package com.example.noseyneighbour;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.example.noseyneighbour.Activities.MapsActivity;
import com.example.noseyneighbour.Classes.Crime;
import com.example.noseyneighbour.Handlers.DBHandler;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataRetrival extends AsyncTask<Void, Void, String> {

    private ArrayList<Crime> crimes;

    StringBuilder stringBuilder;
    LatLng latLng1;
    LatLng latLng2;
    LatLng latLng3;
    LatLng latLng4;

    private GoogleMap googleMap;
    private String crimeType;
    private int year;
    private int month;
    private float radius;
    private Location globalLocation;
    private Context context;

    public DataRetrival(GoogleMap googleMap, String crimeType, int year, int month, float radius, Location globalLocation, Context context) {
        this.googleMap = googleMap;
        this.crimeType = crimeType;
        this.year = year;
        this.month = month;
        this.radius = radius;
        this.globalLocation = globalLocation;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        double latInc = 0.014263;
        double longInc = 0.022634;

        String date = year + "-" + month;

        latLng1 = createRectangle(latInc, longInc).get(0);
        latLng2 = createRectangle(latInc, longInc).get(1);
        latLng3 = createRectangle(latInc, longInc).get(2);
        latLng4 = createRectangle(latInc, longInc).get(3);


        try {
            Log.d("url", "https://data.police.uk/api/crimes-street/" + crimeType + "?poly=" + latLng1.latitude + "," + latLng1.longitude + ":" + latLng2.latitude + "," + latLng2.longitude + ":" + latLng3.latitude + "," + latLng3.longitude + ":" + latLng4.latitude + "," + latLng4.longitude + "&date=" + date);
            URL url = new URL("https://data.police.uk/api/crimes-street/" + crimeType + "?poly=" + latLng1.latitude + "," + latLng1.longitude + ":" + latLng2.latitude + "," + latLng2.longitude + ":" + latLng3.latitude + "," + latLng3.longitude + ":" + latLng4.latitude + "," + latLng4.longitude + "&date=" + date);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);

                    Log.d("line", line);
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally {
                urlConnection.disconnect();
                parseTheMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("error", "ERROR in fetching");
        }

        Log.d("error", "ERROR in fetching");
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        ((MapsActivity)context).getMapViewFragment().setClusterManagerItems(crimes);

        googleMap.addPolygon(new PolygonOptions().add(latLng1, latLng2, latLng3, latLng4));
        Log.d("marker", "added polygon");
    }

    private List<LatLng> createRectangle(double latInc, double longInc){
        int multiplyer = Math.round(radius/2);
        latInc *= multiplyer;
        longInc *= multiplyer;

        return Arrays.asList(new LatLng(globalLocation.getLatitude() - latInc, globalLocation.getLongitude() - longInc),
                new LatLng(globalLocation.getLatitude() + latInc, globalLocation.getLongitude() - longInc),
                new LatLng(globalLocation.getLatitude() + latInc, globalLocation.getLongitude() + longInc),
                new LatLng(globalLocation.getLatitude() - latInc, globalLocation.getLongitude() + longInc));
    }

    public void parseTheMessage(){
        crimes = new ArrayList<>();
        String string;
        Log.d("string", stringBuilder.toString());
        string = stringBuilder.toString();


        double longitude;
        double latitude;

        String outcomeStatus;

        int index;
        int secondIndex;

        boolean firstDone = false;


        while (string.indexOf("latitude") != -1){
            String category = "";

            if (firstDone){
                index = string.indexOf(",{\"category") + 14;
            } else {
                index = string.indexOf("[{\"category") + 14;
                firstDone = true;
            }


            string = string.substring(index);
            secondIndex = string.indexOf("\"");
            category = string.substring(0, secondIndex);
            Log.d("category", category);

            index = string.indexOf("latitude") + 11;
            string = string.substring(index);
            secondIndex = string.indexOf(".");
            latitude = Double.parseDouble(string.substring(0, 7 + secondIndex));
            Log.d("latitude", Double.toString(latitude));

            index = string.indexOf("longitude") + 12;
            string = string.substring(index);
            secondIndex = string.indexOf(".");
            longitude = Double.parseDouble(string.substring(0, 7 + secondIndex));
            Log.d("longitude", Double.toString(longitude));

            index = string.indexOf("status\":") + 8;
            string = string.substring(index);
            Log.d("null", string);
            Log.d("substring", string.substring(0, 5));
            if (string.substring(0, 5).equals("{\"cat")){
                string = string.substring(12);
            }

            secondIndex = string.indexOf(",");
            outcomeStatus = string.substring(0, secondIndex);
            outcomeStatus = outcomeStatus.replace("\"", "");
            Log.d("outcomeStatus", outcomeStatus);

            Crime crime = new Crime(category, latitude, longitude, outcomeStatus);

            crimes.add(crime);
            addCrimeToDB(crime);
        }
    }

    private void addCrimeToDB(Crime crime){
        DBHandler dbHandler = new DBHandler(context);

        dbHandler.addCrime(crime);
    }

}
