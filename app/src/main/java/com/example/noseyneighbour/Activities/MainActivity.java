package com.example.noseyneighbour.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.noseyneighbour.R;

public class MainActivity extends AppCompatActivity {

    private Button currentLocBtn;
    private Button customLocBtn;
    private Button savedCrimesBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentLocBtn = findViewById(R.id.currentLocBtn);
        customLocBtn = findViewById(R.id.customLocBtn);
        savedCrimesBtn = findViewById(R.id.savedCrimesBtn);

        currentLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLocClicked();
            }
        });
        customLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customLocClicked();
            }
        });
        savedCrimesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedCrimesClicked();
            }
        });
    }

    private void customLocClicked(){

    }

    private void currentLocClicked(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void savedCrimesClicked(){

    }
}
