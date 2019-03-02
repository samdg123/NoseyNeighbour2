package com.example.noseyneighbour.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.noseyneighbour.Activities.MapsActivity;
import com.example.noseyneighbour.R;

public class MapMenuFragment extends Fragment {

    private Button searchBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_menu, container, false);

        searchBtn = rootView.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClicked();
            }
        });



        return rootView;
    }

    private void searchClicked(){
        ((MapsActivity)getActivity()).setViewPager(0);
    }
}