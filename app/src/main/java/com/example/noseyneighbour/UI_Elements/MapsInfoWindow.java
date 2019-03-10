package com.example.noseyneighbour.UI_Elements;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.noseyneighbour.Activities.MapsActivity;
import com.example.noseyneighbour.Classes.Crime;
import com.example.noseyneighbour.Handlers.DBHandler;
import com.example.noseyneighbour.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MapsInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private boolean saved;
    private int id;
    private Crime crime;

    ImageView saveIV;

    public MapsInfoWindow(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        id = Integer.parseInt(marker.getTitle());
        getCrime();

        View view = ((MapsActivity)context).getLayoutInflater().inflate(R.layout.layout_marker_info_window, null);

        saveIV = view.findViewById(R.id.saveIV);
        TextView categoryTV = view.findViewById(R.id.categoryTV);
        TextView outcomeTV = view.findViewById(R.id.outcomeTV);
        TextView locDescTV = view.findViewById(R.id.locationDescTV);

        categoryTV.setText(crime.getFormattedCategory());
        outcomeTV.setText(crime.getOutcomeStatus());
        locDescTV.setText(crime.getLocationDesc());

        return view;
    }

    private void getCrime() {
        DBHandler dbHandler = new DBHandler(context);

        crime = dbHandler.getCrime(id);
    }

    private void isSaved(){
        DBHandler dbHandler = new DBHandler(context);
        saved = dbHandler.isCrimeSaved(id);
    }

    private void saveClicked(){
        DBHandler dbHandler = new DBHandler(context);

        if (saved) {
            dbHandler.removeSavedCrime(id);
            saveIV.setImageResource(android.R.drawable.star_big_off);
        } else {
            dbHandler.addSavedCrime(id);
            saveIV.setImageResource(android.R.drawable.star_big_on);
        }

        saved = !saved;
    }

}
