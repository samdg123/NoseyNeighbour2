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
import com.google.maps.android.clustering.ClusterManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataRetrieval extends AsyncTask<Void, Void, String> {

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

    public DataRetrieval(GoogleMap googleMap, String crimeType, int year, int month, float radius, Location globalLocation, Context context) {
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

            parseJSON(urlConnection.getInputStream());

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

    private void parseJSON(InputStream inputStream) throws IOException {
        crimes = new ArrayList<>();
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

        reader.beginArray();
        reader.beginObject();

        String category = "";
        double latitude = 0;
        double longitude = 0;
        String locationDesc = "";
        String outcome = "";
        int year = 0;
        int month = 0;

        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("category")){
                category = reader.nextString();

            } else if (name.equals("location")) {
                reader.beginObject();

                while (reader.hasNext()) {
                    name = reader.nextName();
                    if (name.equals("latitude")) {
                        latitude = Double.parseDouble(reader.nextString());

                    } else if (name.equals("longitude")) {
                        longitude = Double.parseDouble(reader.nextString());
                        reader.endObject();
                        break;

                    } else if (name.equals("street")) {
                        reader.beginObject();
                        while (reader.hasNext()) {
                            name = reader.nextName();
                            if (name.equals("name")) {
                                locationDesc = reader.nextString();
                                reader.endObject();
                                break;

                            } else {
                                reader.skipValue();
                            }
                        }
                    } else {
                        reader.skipValue();
                    }
                }

            } else if (name.equals("outcome_status")) {
                if (reader.peek() == JsonToken.NULL){
                    outcome = "null";
                    reader.skipValue();
                } else {
                    outcome = reader.nextString();
                }

            } else if (name.equals("month")) {
                String monthYear = reader.nextString();
                year = Integer.parseInt(monthYear.substring(0,4));
                month = Integer.parseInt(monthYear.substring(5,7));

                Crime crime = new Crime(category, latitude, longitude, outcome, year, month);
                crimes.add(crime);
                addCrimeToDB(crime);

                reader.endObject();
                if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                    reader.beginObject();
                }

            } else {
                reader.skipValue();
            }
        }
        reader.close();
    }

    private void addCrimeToDB(Crime crime){
        DBHandler dbHandler = new DBHandler(context);

        dbHandler.addCrime(crime);
    }

}
