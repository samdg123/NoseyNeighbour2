package com.example.noseyneighbour.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.noseyneighbour.R;
import com.example.noseyneighbour.UI_Elements.CrimeGraph;

public class GraphActivity extends AppCompatActivity {

    private CrimeGraph crimeGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        crimeGraph = findViewById(R.id.crimeGraph);
    }

    public void updateCrimeGraph(){
        crimeGraph.invalidate();
    }
}
