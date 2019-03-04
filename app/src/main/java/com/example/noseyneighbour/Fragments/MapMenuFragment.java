package com.example.noseyneighbour.Fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.noseyneighbour.Activities.GraphActivity;
import com.example.noseyneighbour.Activities.MapsActivity;
import com.example.noseyneighbour.R;

public class MapMenuFragment extends Fragment {

    private Spinner categorySpinner;
    private Spinner yearSpinner;
    private Spinner monthSpinner;
    private EditText radiusET;
    private Button searchBtn;
    private Button graphBtn;

    private MapsActivity mapsActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_map_menu, container, false);

        mapsActivity = (MapsActivity)getActivity();

        categorySpinner = rootView.findViewById(R.id.categorySpinner);
        yearSpinner = rootView.findViewById(R.id.yearSpinner);
        monthSpinner = rootView.findViewById(R.id.monthSpinner);
        radiusET = rootView.findViewById(R.id.radiusET);
        searchBtn = rootView.findViewById(R.id.searchBtn);
        graphBtn = rootView.findViewById(R.id.graphBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClicked();
            }
        });
        graphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphClicked();
            }
        });

        yearSpinner.setSelection(1);
        monthSpinner.setSelection(5);

        return rootView;
    }

    private void setCrimeSearchParameters(){
        mapsActivity.setCrimeType(categorySpinner.getSelectedItem().toString());
        mapsActivity.setRadius(Integer.parseInt(radiusET.getText().toString()));
        mapsActivity.setYear(Integer.parseInt(yearSpinner.getSelectedItem().toString()));
        mapsActivity.setMonth(Integer.parseInt(monthSpinner.getSelectedItem().toString()));
    }

    private void searchClicked(){
        setCrimeSearchParameters();
        mapsActivity.setViewPager(0);
    }
    private void graphClicked() {
        setCrimeSearchParameters();
        Intent intent = new Intent(getContext(), GraphActivity.class);
        startActivity(intent);
    }
}
