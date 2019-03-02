package com.example.noseyneighbour.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.noseyneighbour.Classes.Crime;
import com.example.noseyneighbour.Activities.MapsActivity;
import com.example.noseyneighbour.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
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

public class MapViewFragment extends Fragment implements OnMapReadyCallback{

    MapView mMapView;
    private GoogleMap googleMap;
    private ImageView configureBtn;
    private ClusterManager<Crime> clusterManager;
    private Location globalLocation;
    private ArrayList<Crime> crimes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_view, container, false);

        init(rootView, savedInstanceState);

        return rootView;
    }

    private void init(View rootView, Bundle savedInstanceState){
        configureBtn = rootView.findViewById(R.id.configureBtn);
        configureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configureClicked();
            }
        });

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;

        requestPermissions();

        setUpClusterer();
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    return false;
                }


            });
        } else {
        }

        googleMap.setOnMyLocationClickListener(new LocationClicked());
    }

    class LocationClicked implements GoogleMap.OnMyLocationClickListener
    {
        @Override
        public void onMyLocationClick(@NonNull Location location) {
            globalLocation = location;
            DataRetrival dataRetrival = new DataRetrival();
            dataRetrival.execute();

            displayToast(location);


        }
    }

    private void setUpClusterer(){
        clusterManager = new ClusterManager<>(getContext(), googleMap);

        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);
    }

    public void parseTheMessage(){
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

            clusterManager.addItem(crime);
            crimes.add(crime);
        }
    }

    private void displayToast(Location location){
        Toast.makeText(getActivity().getApplicationContext(), Double.toString(location.getLatitude()) + Double.toString(location.getLongitude()), Toast.LENGTH_LONG).show();
    }

    StringBuilder stringBuilder;
    LatLng latLng1;
    LatLng latLng2;
    LatLng latLng3;
    LatLng latLng4;

    class DataRetrival extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {
            double latInc = 0.014263;
            double longInc = 0.022634;
            String date = ((MapsActivity)getActivity()).getYear() + "-" + ((MapsActivity)getActivity()).getMonth();
            String crimeType = ((MapsActivity)getActivity()).getCrimeType();

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

            googleMap.addPolygon(new PolygonOptions().add(latLng1, latLng2, latLng3, latLng4));
            Log.d("marker", "added polygon");

        }

        private List<LatLng> createRectangle(double latInc, double longInc){

            int multiplyer = Math.round(((MapsActivity)getActivity()).getRadius()/2);
            latInc *= multiplyer;
            longInc *= multiplyer;

            return Arrays.asList(new LatLng(globalLocation.getLatitude() - latInc, globalLocation.getLongitude() - longInc),
                    new LatLng(globalLocation.getLatitude() + latInc, globalLocation.getLongitude() - longInc),
                    new LatLng(globalLocation.getLatitude() + latInc, globalLocation.getLongitude() + longInc),
                    new LatLng(globalLocation.getLatitude() - latInc, globalLocation.getLongitude() + longInc));

        }
    }


    private void configureClicked(){
        ((MapsActivity)getActivity()).setViewPager(1);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}