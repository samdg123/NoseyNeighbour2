package com.example.noseyneighbour.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.noseyneighbour.Classes.Crime;
import com.example.noseyneighbour.Activities.MapsActivity;
import com.example.noseyneighbour.CrimeClusterRenderer;
import com.example.noseyneighbour.DataRetrieval.DataRetrievalMaps;
import com.example.noseyneighbour.Handlers.DBHandler;
import com.example.noseyneighbour.R;
import com.example.noseyneighbour.UI_Elements.MapsInfoWindow;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class CurrentLocMapsFragment extends Fragment implements OnMapReadyCallback {

    MapView mMapView;
    private GoogleMap googleMap;
    private ImageView configureBtn;
    private ImageView saveIV;
    private ProgressBar progressBar;

    private ClusterManager<Crime> clusterManager;
    private Location location;
    private boolean crimeSaved = false;
    private Crime currentCrime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_view_current_loc, container, false);

        init(rootView, savedInstanceState);

        return rootView;

    }

    private void init(View rootView, Bundle savedInstanceState) {
        progressBar = rootView.findViewById(R.id.progressBar);


        saveIV = rootView.findViewById(R.id.saveIV);
        saveIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClicked();
            }
        });

        configureBtn = rootView.findViewById(R.id.configureBtn);
        configureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configureClicked();
            }
        });

        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;

        getLastKnownLocation();

        //moves mapcamera to current location
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(11).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //if a marker is not selected then the heart icon will disappear
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                saveIV.setVisibility(View.INVISIBLE);
            }
        });

        setUpClusterer();
    }

    //if permissions are not granted, requests them, then gets current location
    private void getLastKnownLocation(){
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            Criteria criteria = new Criteria();
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            String provider = locationManager.getBestProvider(criteria, false);

            location = locationManager.getLastKnownLocation(provider);

            googleMap.setMyLocationEnabled(true);
            googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    return false;
                }

            });
        } else {
        }
    }

    //gets markers from the police api
    public void getMarkers(){
        progressBar.setVisibility(View.VISIBLE);
        saveIV.setVisibility(View.INVISIBLE);
        googleMap.clear();

        ((MapsActivity)getActivity()).setLocation(location);

        DataRetrievalMaps dataRetrieval = new DataRetrievalMaps(googleMap, ((MapsActivity)getActivity()).getCrimeType(), ((MapsActivity)getActivity()).getYear(), ((MapsActivity)getActivity()).getMonth(), ((MapsActivity)getActivity()).getRadius(), location, getContext());
        dataRetrieval.execute();
    }

    private void setUpClusterer(){
        googleMap.setInfoWindowAdapter(new MapsInfoWindow(getContext()));
        clusterManager = new ClusterManager<>(getContext(), googleMap);
        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<Crime>() {
            @Override
            public boolean onClusterClick(Cluster<Crime> cluster) {
                return true;
            }
        });
        clusterManager.setOnClusterItemClickListener(new MarkerOnClickListener());
        clusterManager.setRenderer(new CrimeClusterRenderer(getContext(), googleMap, clusterManager));

        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);
    }


    public void setClusterManagerItems(ArrayList<Crime> crimes) {
        ((MapsActivity) getActivity()).setCrimes(crimes);
        clusterManager.clearItems();

        if (crimes != null) {
            clusterManager.addItems(crimes);
            clusterManager.cluster();
            Toasty.success(getContext(),  crimes.size() + " crimes found", Toast.LENGTH_LONG).show();

        } else {
            Toasty.info(getContext(), "No crimes found", Toast.LENGTH_LONG).show();
        }

        progressBar.setVisibility(View.INVISIBLE);
    }

    //controls what happens when a crime marker is clicked
    public class MarkerOnClickListener implements ClusterManager.OnClusterItemClickListener{

        @Override
        public boolean onClusterItemClick(ClusterItem clusterItem) {
            saveIV.setVisibility(View.VISIBLE);

            currentCrime = (Crime) clusterItem;
            crimeSaved = isCrimeSaved(currentCrime);

            if (crimeSaved) {
                saveIV.setImageResource(R.drawable.ic_like_checked);
            } else {
                saveIV.setImageResource(R.drawable.ic_like_unchecked);
            }
            return false;
        }

        private boolean isCrimeSaved(Crime crime) {
            DBHandler dbHandler = new DBHandler(getContext());
            return dbHandler.isCrimeSaved(crime.getId());
        }
    }

    private void saveClicked(){
        crimeSaved = !crimeSaved;
        DBHandler dbHandler = new DBHandler(getContext());

        if (crimeSaved) {
            dbHandler.addCrime(currentCrime);
            saveIV.setImageResource(R.drawable.ic_like_checked);

        } else {
            dbHandler.removeCrime(currentCrime);
            saveIV.setImageResource(R.drawable.ic_like_unchecked);
        }
    }

    //if configure button pressed go to crime search parameter fragment
    private void configureClicked(){
        ((MapsActivity)getActivity()).setViewPager(0);
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