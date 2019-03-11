package com.example.noseyneighbour.Activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.noseyneighbour.DataRetrievalGraph;
import com.example.noseyneighbour.R;
import com.example.noseyneighbour.UI_Elements.CrimeGraph;

public class GraphActivity extends AppCompatActivity {

    private CrimeGraph crimeGraph;
    private TextView yearTV;
    private Spinner yearSpinner;
    private Spinner categorySpinner;
    private EditText radiusET;
    private ImageView searchIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        crimeGraph = findViewById(R.id.crimeGraph);
        yearSpinner = findViewById(R.id.yearSpinner);
        categorySpinner = findViewById(R.id.categorySpinner);
        radiusET = findViewById(R.id.radiusET);

        searchIV = findViewById(R.id.searchIV);
        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClicked();
            }
        });
    }

    private void searchClicked() {
        String category = categorySpinner.getSelectedItem().toString();
        int radius = Integer.parseInt( radiusET.getText().toString() );
        int year = Integer.parseInt( yearSpinner.getSelectedItem().toString() );
        crimeGraph.setYear(year);

        for (int month = 1; month <= 12; month++) {
            new DataRetrievalGraph(category, year, month, radius, getLastKnownLocation(), getApplicationContext(), this).execute();
        }
    }


    private Location getLastKnownLocation(){
        Location location = new Location("");

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            Criteria criteria = new Criteria();
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            String provider = locationManager.getBestProvider(criteria, false);

            location = locationManager.getLastKnownLocation(provider);

        } else {
        }

        return location;
    }

    public void updateCrimeGraph(){
        crimeGraph.invalidate();
    }

}
