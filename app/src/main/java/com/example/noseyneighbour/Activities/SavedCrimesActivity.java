package com.example.noseyneighbour.Activities;

import android.app.Dialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.example.noseyneighbour.Adapters.SavedCrimesRVAdapter;
import com.example.noseyneighbour.Classes.Crime;
import com.example.noseyneighbour.Handlers.DBHandler;
import com.example.noseyneighbour.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class SavedCrimesActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView map;
    private TextView numCrimesTV;
    private Crime selectedCrime;

    private void init(){
        ArrayList<Crime> crimes;

        DBHandler dbHandler = new DBHandler(this);
        crimes = dbHandler.getCrimes();

        numCrimesTV = findViewById(R.id.numCrimesTV);
        numCrimesTV.setText(crimes.size() + " Crimes Saved");

        RecyclerView recyclerView = findViewById(R.id.crimesRV);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);

        SavedCrimesRVAdapter crimesAdapter = new SavedCrimesRVAdapter(crimes, this);
        recyclerView.setAdapter(crimesAdapter);
    }

    //show the number of saved crimes beneath heading
    public void updateNumCrimes(int num){
        numCrimesTV.setText(num + " crimes saved");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_crimes);
        init();
    }

    //setup map dialog when user selects 'show on map'
    public void showCrimeOnMap(Crime crime){
        selectedCrime = crime;

        Dialog dialog = new Dialog(this);
        dialog.setTitle(crime.getFormattedCategory());
        dialog.setContentView(R.layout.dialog_map_lite);
        dialog.show();

        map = dialog.findViewById(R.id.mapLite);
        map.onCreate(null);
        map.onResume();
        map.getMapAsync(this);
    }

    //move camera to selected crime on map dialog and add marker for selected crime
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = selectedCrime.getPosition();
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(11).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.addMarker(new MarkerOptions().position(latLng));
    }
}
