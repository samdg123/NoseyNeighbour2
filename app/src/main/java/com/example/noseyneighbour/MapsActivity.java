package com.example.noseyneighbour;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MapsActivity extends AppCompatActivity {

    private ViewPager viewPager;

    private String crimeType = "all-crime";
    private int year = 2018;
    private int month = 6;
    private float radius = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        viewPager = findViewById(R.id.viewPager);

        setupViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 1){
            viewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MapViewFragment(), "MapFragment");
        adapter.addFragment(new MapMenuFragment(), "MapMenu");
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

}
