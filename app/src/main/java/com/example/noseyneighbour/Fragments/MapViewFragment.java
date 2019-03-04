package com.example.noseyneighbour.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.noseyneighbour.Classes.Crime;
import com.example.noseyneighbour.Activities.MapsActivity;
import com.example.noseyneighbour.DataRetrieval;
import com.example.noseyneighbour.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

public class MapViewFragment extends Fragment implements OnMapReadyCallback{

    MapView mMapView;
    private GoogleMap googleMap;
    private ImageView configureBtn;
    private ClusterManager<Crime> clusterManager;
    private Location globalLocation;
    private ArrayList<Crime> crimes;

    private boolean needsRedraw = false;

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
            DataRetrieval dataRetrieval = new DataRetrieval(googleMap, ((MapsActivity)getActivity()).getCrimeType(), ((MapsActivity)getActivity()).getYear(), ((MapsActivity)getActivity()).getMonth(), ((MapsActivity)getActivity()).getRadius(), location, getContext());
            dataRetrieval.execute();


            dataRetrieval = new DataRetrieval(googleMap, ((MapsActivity)getActivity()).getCrimeType(), ((MapsActivity)getActivity()).getYear(), ((MapsActivity)getActivity()).getMonth()-2, ((MapsActivity)getActivity()).getRadius(), location, getContext());
            dataRetrieval.execute();
            dataRetrieval = new DataRetrieval(googleMap, ((MapsActivity)getActivity()).getCrimeType(), ((MapsActivity)getActivity()).getYear(), ((MapsActivity)getActivity()).getMonth()-1, ((MapsActivity)getActivity()).getRadius(), location, getContext());
            dataRetrieval.execute();
            dataRetrieval = new DataRetrieval(googleMap, ((MapsActivity)getActivity()).getCrimeType(), ((MapsActivity)getActivity()).getYear(), ((MapsActivity)getActivity()).getMonth()+1, ((MapsActivity)getActivity()).getRadius(), location, getContext());
            dataRetrieval.execute();
            dataRetrieval = new DataRetrieval(googleMap, ((MapsActivity)getActivity()).getCrimeType(), ((MapsActivity)getActivity()).getYear(), ((MapsActivity)getActivity()).getMonth()+2, ((MapsActivity)getActivity()).getRadius(), location, getContext());
            dataRetrieval.execute();
            dataRetrieval = new DataRetrieval(googleMap, ((MapsActivity)getActivity()).getCrimeType(), ((MapsActivity)getActivity()).getYear(), ((MapsActivity)getActivity()).getMonth()+3, ((MapsActivity)getActivity()).getRadius(), location, getContext());
            dataRetrieval.execute();
            dataRetrieval = new DataRetrieval(googleMap, ((MapsActivity)getActivity()).getCrimeType(), ((MapsActivity)getActivity()).getYear(), ((MapsActivity)getActivity()).getMonth()+4, ((MapsActivity)getActivity()).getRadius(), location, getContext());
            dataRetrieval.execute();
            dataRetrieval = new DataRetrieval(googleMap, ((MapsActivity)getActivity()).getCrimeType(), ((MapsActivity)getActivity()).getYear(), ((MapsActivity)getActivity()).getMonth()+5, ((MapsActivity)getActivity()).getRadius(), location, getContext());
            dataRetrieval.execute();

            displayToast(location);

            ((MapsActivity)getActivity()).setLocation(location);
        }
    }

    private void setUpClusterer(){
        clusterManager = new ClusterManager<>(getContext(), googleMap);

        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);
    }



    private void displayToast(Location location){
        Toast.makeText(getActivity().getApplicationContext(), Double.toString(location.getLatitude()) + Double.toString(location.getLongitude()), Toast.LENGTH_LONG).show();
    }


    public void setClusterManagerItems(ArrayList<Crime> crimes) {
        clusterManager.clearItems();
        this.clusterManager.addItems(crimes);
    }
    public void setNeedsRedraw(boolean needsRedraw) {
        this.needsRedraw = needsRedraw;
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }
    public MapView getmMapView() {
        return mMapView;
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