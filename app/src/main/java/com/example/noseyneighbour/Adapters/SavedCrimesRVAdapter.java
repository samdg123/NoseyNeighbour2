package com.example.noseyneighbour.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.noseyneighbour.Activities.SavedCrimesActivity;
import com.example.noseyneighbour.Classes.Crime;
import com.example.noseyneighbour.Handlers.DBHandler;
import com.example.noseyneighbour.R;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SavedCrimesRVAdapter extends RecyclerView.Adapter<SavedCrimesRVAdapter.CrimeViewHolder> {

    private ArrayList<Crime> crimes;
    private Context context;
    private SavedCrimesActivity activity;


    public SavedCrimesRVAdapter(ArrayList<Crime> crimes, SavedCrimesActivity activity) {
        this.crimes = crimes;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CrimeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_crime, viewGroup, false);
        return new CrimeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CrimeViewHolder crimeViewHolder, int i) {
        final Crime crime = crimes.get(i);

        LatLng latLng = crime.getPosition();
        DecimalFormat decimalFormat = new DecimalFormat("#.00000");
        String latitude = decimalFormat.format(latLng.latitude);
        String longitude = decimalFormat.format(latLng.longitude);

        crimeViewHolder.setCategoryText(crime.getFormattedCategory());
        crimeViewHolder.setDateText("Date: " + crime.getMonth() + "-" + crime.getYear());
        crimeViewHolder.setLocationText("Location: " + latitude + ", " + longitude);
        crimeViewHolder.setLocationDescText(crime.getLocationDesc());
        crimeViewHolder.setOutcomeText(crime.getOutcomeStatus());

        crimeViewHolder.itemView.setClickable(true);
        crimeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] options = {"Show on map", "Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            showOnMap();
                        } else {
                            removeFromFavorites();
                        }
                    }

                    private void removeFromFavorites(){
                        DBHandler dbHandler = new DBHandler(context);
                        dbHandler.removeSavedCrime(crime.getId());

                        crimes.remove(crime);
                        notifyDataSetChanged();
                        activity.updateNumCrimes(crimes.size());
                    }

                    private void showOnMap(){
                        ((SavedCrimesActivity) context).showCrimeOnMap(crime);
                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return crimes.size();
    }

    public class CrimeViewHolder extends RecyclerView.ViewHolder{

        private TextView categoryTV;
        private TextView outcomeTV;
        private TextView locationTV;
        private TextView locationDescTV;
        private TextView dateTV;

        public CrimeViewHolder(@NonNull View itemView) {
            super(itemView);
            init(itemView);
        }

        private void init(View itemView){
            categoryTV = itemView.findViewById(R.id.categoryTV);
            outcomeTV = itemView.findViewById(R.id.outcomeStatusTV);
            locationTV = itemView.findViewById(R.id.locationTV);
            locationDescTV = itemView.findViewById(R.id.locationDescTV);
            dateTV = itemView.findViewById(R.id.dateTV);

            context = itemView.getContext();
        }

        public void setCategoryText(String text) {
            categoryTV.setText(text);
        }
        public void setDateText(String text) {
            dateTV.setText(text);
        }
        public void setLocationText(String text) {
            locationTV.setText(text);
        }
        public void setLocationDescText(String text) {
            locationDescTV.setText(text);
        }
        public void setOutcomeText(String text) {
            outcomeTV.setText(text);
        }
    }
}
