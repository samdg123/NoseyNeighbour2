package com.example.noseyneighbour.Activities;

import android.location.Location;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.os.Bundle;

import com.example.noseyneighbour.Classes.Crime;
import com.example.noseyneighbour.Fragments.CurrentLocMapsFragment;
import com.example.noseyneighbour.Fragments.MapMenuFragment;
import com.example.noseyneighbour.R;
import com.example.noseyneighbour.Adapters.SectionsStatePagerAdapter;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity {

    private SectionsStatePagerAdapter adapter;
    private ViewPager viewPager;
    private CurrentLocMapsFragment currentLocMapsFragment;
    private MapMenuFragment mapMenuFragment;
    private ArrayList<Crime> crimes;

    private String crimeType;
    private int year;
    private int month;
    private float radius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        viewPager = findViewById(R.id.viewPager);

        setupViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setupViewPager(ViewPager viewPager){
        adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        mapMenuFragment = new MapMenuFragment();
        currentLocMapsFragment = new CurrentLocMapsFragment();
        adapter.addFragment(mapMenuFragment, "MapMenu");
        adapter.addFragment(currentLocMapsFragment, "MapView");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNum){
        viewPager.setCurrentItem(fragmentNum);
    }

    public float getRadius() {
        return radius;
    }
    public int getMonth() {
        return month;
    }
    public int getYear() {
        return year;
    }
    public String getCrimeType() {
        return crimeType;
    }
    public ArrayList<Crime> getCrimes() {
        return crimes;
    }
    public MapMenuFragment getMapMenuFragment() {
        return mapMenuFragment;
    }
    public CurrentLocMapsFragment getCurrentLocMapsFragment() {
        return currentLocMapsFragment;
    }

    public void setYear(int year) {
        this.year = year;
    }
    public void setRadius(float radius) {
        this.radius = radius;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public void setCrimeType(String crimeType) {
        this.crimeType = crimeType;
    }
    public void setCrimes(ArrayList<Crime> crimes) {
        this.crimes = crimes;
    }
}
