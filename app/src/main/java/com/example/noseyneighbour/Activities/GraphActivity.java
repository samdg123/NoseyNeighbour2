package com.example.noseyneighbour.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.noseyneighbour.R;
import com.example.noseyneighbour.UI_Elements.CrimeGraph;

public class GraphActivity extends AppCompatActivity {

    private CrimeGraph crimeGraph;
    private TextView yearTV;
    private Spinner yearSpinner;
    private ImageView searchIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        crimeGraph = findViewById(R.id.crimeGraph);
        yearSpinner = findViewById(R.id.yearSpinner);

        searchIV = findViewById(R.id.searchIV);
        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClicked();
            }
        });
    }

    private void searchClicked() {
        int year = Integer.parseInt( yearSpinner.getSelectedItem().toString() );

        crimeGraph = new CrimeGraph(this, year);
    }

    public void updateCrimeGraph(){
        crimeGraph.invalidate();
    }
}
