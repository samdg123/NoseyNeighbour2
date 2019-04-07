package com.example.noseyneighbour.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.example.noseyneighbour.Activities.MapsActivity;
import com.example.noseyneighbour.R;
import com.manojbhadane.QButton;
import com.ramotion.fluidslider.FluidSlider;

public class MapMenuFragment extends Fragment {

    public static final int MIN_RADIUS = 0;
    public static final int MAX_RADIUS = 10;

    public static final int MIN_YEAR = 2016;
    public static final int MAX_YEAR = 2018;

    private static final int MIN_MONTH = 1;
    private static final int MAX_MONTH = 12;

    private Spinner categorySpinner;
    private QButton searchBtn;
    private FluidSlider yearSlider;
    private FluidSlider monthSlider;
    private FluidSlider radiusSlider;

    private MapsActivity mapsActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_map_menu, container, false);

        initObjects(rootView);

        return rootView;
    }

    private void initObjects(View v) {
        mapsActivity = (MapsActivity)getActivity();

        categorySpinner = v.findViewById(R.id.categorySpinner);
        searchBtn = v.findViewById(R.id.searchBtn);

        yearSlider = v.findViewById(R.id.yearSlider);
        monthSlider = v.findViewById(R.id.monthSlider);
        radiusSlider = v.findViewById(R.id.radiusSlider);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClicked();
            }
        });

        initYearSlider();
        initMonthSlider();
        initRadiusSlider();
    }

    private void initYearSlider(){
        yearSlider.setStartText("" + MIN_YEAR);
        yearSlider.setEndText("" + MAX_YEAR);

        //function to update slider bubble text on call
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

    private void initMonthSlider(){
        monthSlider.setStartText("" + MIN_MONTH);
        monthSlider.setEndText("" + MAX_MONTH);

        //function to update slider bubble text on call
        Function0<Unit> function0 = new Function0<Unit>() {
            @Override
            public Unit invoke() {
                monthSlider.setBubbleText(Integer.toString(monthSliderValue()));
                return Unit.INSTANCE;
            }
        };

        monthSlider.setBubbleText(Integer.toString(monthSliderValue()));

        monthSlider.setBeginTrackingListener(function0);
        monthSlider.setEndTrackingListener(function0);
        monthSlider.setPositionListener(new Function1<Float, Unit>() {
            @Override
            public Unit invoke(Float aFloat) {
                monthSlider.setBubbleText(Integer.toString(monthSliderValue()));
                return Unit.INSTANCE;
            }
        });
    }

    private void initRadiusSlider() {
        radiusSlider.setStartText("" + MIN_RADIUS);
        radiusSlider.setEndText("" + MAX_RADIUS);

        //function to update slider bubble text on call
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
        return sliderValue(radiusSlider, MIN_RADIUS, MAX_RADIUS);
    }

    private int yearSliderValue() {
        return sliderValue(yearSlider, MIN_YEAR, MAX_YEAR);
    }

    private int monthSliderValue() {
        return sliderValue(monthSlider, MIN_MONTH, MAX_MONTH);
    }

    public static int sliderValue(FluidSlider slider, int min, int max){
        int value;
        final int total = max - min;

        value = Math.round ((slider.getPosition() * total) + min);

        return value;
    }

    private void setCrimeSearchParameters(){
        mapsActivity.setCrimeType(categorySpinner.getSelectedItem().toString());
        mapsActivity.setRadius(radiusSliderValue());
        mapsActivity.setYear(yearSliderValue());
        mapsActivity.setMonth(monthSliderValue());
    }

    private void searchClicked(){
        setCrimeSearchParameters();
        mapsActivity.getCurrentLocMapsFragment().getMarkers();
        mapsActivity.setViewPager(1);
    }

}
