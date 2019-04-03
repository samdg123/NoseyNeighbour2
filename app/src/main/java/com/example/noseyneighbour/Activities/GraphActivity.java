package com.example.noseyneighbour.Activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.noseyneighbour.DataRetrieval.DataRetrievalGraph;
import com.example.noseyneighbour.Fragments.MapMenuFragment;
import com.example.noseyneighbour.R;
import com.example.noseyneighbour.UI_Elements.CrimeGraph;
import com.manojbhadane.QButton;
import com.ramotion.fluidslider.FluidSlider;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    private static final int MIN_RADIUS = MapMenuFragment.MIN_RADIUS;
    private static final int MAX_RADIUS = MapMenuFragment.MAX_RADIUS;

    private static final int MIN_YEAR = MapMenuFragment.MIN_YEAR;
    private static final int MAX_YEAR = MapMenuFragment.MAX_YEAR;


    private CrimeGraph crimeGraph;
    private Spinner categorySpinner;
    private QButton searchBtn;
    private ProgressBar progressBar;
    private FluidSlider yearSlider;
    private FluidSlider radiusSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        crimeGraph = findViewById(R.id.crimeGraph);
        categorySpinner = findViewById(R.id.categorySpinner);
        progressBar = findViewById(R.id.progressBarGraph);

        searchBtn = findViewById(R.id.searchGraphBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClicked();
            }
        });

        yearSlider = findViewById(R.id.graphYearSlider);
        radiusSlider = findViewById(R.id.graphRadiusSlider);

        initYearSlider();
        initRadiusSlider();
    }

    private void initYearSlider(){
        yearSlider.setStartText("" + MIN_YEAR);
        yearSlider.setEndText("" + MAX_YEAR);

        Function0<Unit> function0 = new Function0<Unit>() {
            @Override
            public Unit invoke() {
                radiusSlider.setBubbleText(Integer.toString(yearSliderValue()));
                return Unit.INSTANCE;
            }
        };

        yearSlider.setBubbleText(Integer.toString(yearSliderValue()));

        yearSlider.setBeginTrackingListener(function0);
        yearSlider.setEndTrackingListener(function0);
        yearSlider.setPositionListener(new Function1<Float, Unit>() {
            @Override
            public Unit invoke(Float aFloat) {
                yearSlider.setBubbleText(Integer.toString(yearSliderValue()));
                return Unit.INSTANCE;
            }
        });
    }

    private void initRadiusSlider() {
        radiusSlider.setStartText("" + MIN_RADIUS);
        radiusSlider.setEndText("" + MAX_RADIUS);

        Function0<Unit> function0 = new Function0<Unit>() {
            @Override
            public Unit invoke() {
                radiusSlider.setBubbleText(Integer.toString(radiusSliderValue()));
                return Unit.INSTANCE;
            }
        };

        radiusSlider.setBubbleText(Integer.toString(radiusSliderValue()));

        radiusSlider.setBeginTrackingListener(function0);
        radiusSlider.setEndTrackingListener(function0);
        radiusSlider.setPositionListener(new Function1<Float, Unit>() {
            @Override
            public Unit invoke(Float aFloat) {
                radiusSlider.setBubbleText(Integer.toString(radiusSliderValue()));
                return Unit.INSTANCE;
            }
        });
    }

    private int radiusSliderValue() {
        return MapMenuFragment.sliderValue(radiusSlider, MIN_RADIUS, MAX_RADIUS);
    }

    private int yearSliderValue() {
        return MapMenuFragment.sliderValue(yearSlider, MIN_YEAR, MAX_YEAR);
    }

    private void searchClicked() {
        crimeGraph.setNumCrimesList(new ArrayList<int[]>());
        crimeGraph.invalidate();
        progressBar.setVisibility(View.VISIBLE);
        searchBtn.setClickable(false);

        String category = categorySpinner.getSelectedItem().toString();
        int radius = radiusSliderValue();
        int year = yearSliderValue();
        crimeGraph.setYear(year);

        if (!category.equals("all-crime")) {
            crimeGraph.setCategory(category);
        }

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

    public void incrementProgressBar(){
        progressBar.setProgress(progressBar.getProgress()+1);
        if (progressBar.getProgress() >= 12) {
            updateCrimeGraph();
        }
    }
    public void addToNumCrimesList(int[] numCrimes){
        crimeGraph.addToNumCrimesList(numCrimes);
        incrementProgressBar();
    }

    public void updateCrimeGraph(){
        crimeGraph.invalidate();
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setProgress(0);
        searchBtn.setClickable(true);
    }

}
