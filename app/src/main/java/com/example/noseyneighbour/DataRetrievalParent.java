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

public class DataRetrievalParent extends AsyncTask<Void, Void, String> {

    ArrayList<Crime> crimes;

    LatLng northEast;
    LatLng southEast;
    LatLng southWest;
    LatLng northWest;

    private String crimeType;
    private int year;
    int month;
    private float radius;
    private LatLng currentLatLng;
    Context context;

    public DataRetrievalParent(String crimeType, int year, int month, float radius, Location globalLocation, Context context) {
        this.crimeType = crimeType;
        this.year = year;
        this.month = month;
        this.radius = radius;
        this.context = context;
        currentLatLng = new LatLng(globalLocation.getLatitude(), globalLocation.getLongitude());
    }

    @Override
    protected String doInBackground(Void... voids) {
        ArrayList<LatLng> corners = getLatLngCorners(currentLatLng, radius);

        String date = year + "-" + month;

        northEast = corners.get(0);
        southEast = corners.get(1);
        southWest = corners.get(2);
        northWest = corners.get(3);

        try {
            Log.d("url", "https://data.police.uk/api/crimes-street/" + crimeType + "?poly=" + northEast.latitude + "," + northEast.longitude + ":" + southEast.latitude + "," + southEast.longitude + ":" + southWest.latitude + "," + southWest.longitude + ":" + northWest.latitude + "," + northWest.longitude + "&date=" + date);
            URL url = new URL("https://data.police.uk/api/crimes-street/" + crimeType + "?poly=" + northEast.latitude + "," + northEast.longitude + ":" + southEast.latitude + "," + southEast.longitude + ":" + southWest.latitude + "," + southWest.longitude + ":" + northWest.latitude + "," + northWest.longitude + "&date=" + date);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            crimes = parseJSON( urlConnection.getInputStream() );

            DBHandler dbHandler = new DBHandler(context);
            dbHandler.addCrimes(crimes);

            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("error", "ERROR in fetching... " + e.getMessage());

        } finally {
        }

        return null;
    }


    private ArrayList<LatLng> getLatLngCorners(LatLng center, double radiusMiles){
        ArrayList<LatLng> corners = new ArrayList<>();

        //needed as the radius to corner will be bigger than middle of an edge. it is also represented in meters
        double radiusToCorner = (radiusMiles*1609) * Math.sqrt(2);

        LatLng northEast = SphericalUtil.computeOffset(center, radiusToCorner, 45);
        LatLng southEast = SphericalUtil.computeOffset(center, radiusToCorner, 135);
        LatLng southWest = SphericalUtil.computeOffset(center, radiusToCorner, 225);
        LatLng northWest = SphericalUtil.computeOffset(center, radiusToCorner, 315);

        corners.add(northEast);
        corners.add(southEast);
        corners.add(southWest);
        corners.add(northWest);

        return corners;
    }

    private ArrayList<Crime> parseJSON(InputStream inputStream) throws IOException {
        ArrayList<Crime> crimes = new ArrayList<>();
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

        reader.beginArray();
        if (reader.peek() == JsonToken.END_ARRAY) {
            return crimes;
        }
        reader.beginObject();

        int id = 0;
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
                        latitude = reader.nextDouble();

                    } else if (name.equals("longitude")) {
                        longitude = reader.nextDouble();
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
                if (reader.peek() == JsonToken.NULL) {
                    outcome = "null";
                    reader.skipValue();

                } else if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        name = reader.nextName();
                        if (name.equals("category")) outcome = reader.nextString();
                        else {
                            reader.skipValue();
                            reader.endObject();
                            break;
                        }
                    }

                } else {
                    outcome = reader.nextString();
                }

            } else if (name.equals("id")) {
                id = reader.nextInt();

            } else if (name.equals("month")) {
                String monthYear = reader.nextString();
                year = Integer.parseInt(monthYear.substring(0,4));
                month = Integer.parseInt(monthYear.substring(5,7));

                Crime crime = new Crime(id, category, latitude, longitude, outcome, year, month, locationDesc);
                crimes.add(crime);

                id = 0;
                category = "";
                latitude = 0;
                longitude = 0;
                outcome = "";
                locationDesc = "";

                reader.endObject();
                if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                    reader.beginObject();
                }

            } else {
                reader.skipValue();
            }
        }
        reader.close();

        return crimes;
    }


    private void addCrimeToDB(Crime crime){
        DBHandler dbHandler = new DBHandler(context);

        dbHandler.addCrime(crime);
    }

}
