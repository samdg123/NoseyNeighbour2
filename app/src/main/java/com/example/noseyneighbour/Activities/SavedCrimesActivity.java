package com.example.noseyneighbour.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.noseyneighbour.Adapters.SavedCrimesRVAdapter;
import com.example.noseyneighbour.Classes.Crime;
import com.example.noseyneighbour.Handlers.DBHandler;
import com.example.noseyneighbour.R;

import java.util.ArrayList;

public class SavedCrimesActivity extends AppCompatActivity {

    private TextView numCrimesTV;

    private void init(){

        ArrayList<Crime> crimes;

        DBHandler dbHandler = new DBHandler(this);
        crimes = dbHandler.getSavedCrimes();

        numCrimesTV = findViewById(R.id.numCrimesTV);
        numCrimesTV.setText(crimes.size() + " crimes saved");

        RecyclerView recyclerView = findViewById(R.id.crimesRV);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        SavedCrimesRVAdapter crimesAdapter = new SavedCrimesRVAdapter(crimes);
        recyclerView.setAdapter(crimesAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_crimes);
        init();
    }
}
