package com.example.noseyneighbour.Adapters;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.noseyneighbour.Classes.Crime;
import com.example.noseyneighbour.R;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CrimesRVAdapter extends RecyclerView.Adapter<CrimesRVAdapter.CrimeViewHolder> {

    private ArrayList<Crime> crimes;

    public CrimesRVAdapter(ArrayList<Crime> crimes) {
        this.crimes = crimes;
    }

    @NonNull
    @Override
    public CrimeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout_crime, viewGroup, false);
        return new CrimeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CrimeViewHolder crimeViewHolder, int i) {
        Crime crime = crimes.get(i);

        LatLng latLng = crime.getPosition();
        DecimalFormat decimalFormat = new DecimalFormat("#.00000");
        String latitude = decimalFormat.format(latLng.latitude);
        String longitude = decimalFormat.format(latLng.longitude);

        crimeViewHolder.setCategoryText(crime.getCategory());
        crimeViewHolder.setDateText("Date: " + crime.getMonth() + "-" + crime.getYear());
        crimeViewHolder.setLocationText("Location: " + latitude + ", " + longitude);
        crimeViewHolder.setOutcomeText(crime.getOutcomeStatus());
    }

    @Override
    public int getItemCount() {
        return crimes.size();
    }

    public class CrimeViewHolder extends RecyclerView.ViewHolder{

        private TextView categoryTV;
        private TextView outcomeTV;
        private TextView locationTV;
        private TextView dateTV;

        public CrimeViewHolder(@NonNull View itemView) {
            super(itemView);
            init(itemView);
        }

        private void init(View itemView){
            categoryTV = itemView.findViewById(R.id.categoryTV);
            outcomeTV = itemView.findViewById(R.id.outcomeStatusTV);
            locationTV = itemView.findViewById(R.id.locationTV);
            dateTV = itemView.findViewById(R.id.dateTV);
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
        public void setOutcomeText(String text) {
            outcomeTV.setText(text);
        }
    }
}
