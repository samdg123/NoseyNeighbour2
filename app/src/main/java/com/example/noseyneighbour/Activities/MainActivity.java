package com.example.noseyneighbour.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.noseyneighbour.R;

public class MainActivity extends AppCompatActivity {

    private Button currentLocBtn;
    private Button customLocBtn;
    private Button savedCrimesBtn;
    private Button graphBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentLocBtn = findViewById(R.id.currentLocBtn);
        customLocBtn = findViewById(R.id.customLocBtn);
        savedCrimesBtn = findViewById(R.id.savedCrimesBtn);
        graphBtn = findViewById(R.id.graphBtn);

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
        graphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphClicked();
            }
        });

        requestPermissions();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            currentLocBtn.setEnabled(true);
            graphBtn.setEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 123){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                currentLocBtn.setEnabled(true);
                graphBtn.setEnabled(true);
            }
        }

    }

    private void customLocClicked(){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("currentLoc", false);
        startActivity(intent);
    }

    private void currentLocClicked(){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("currentLoc", true);
        startActivity(intent);
    }

    private void savedCrimesClicked(){
        Intent intent = new Intent(this, SavedCrimesActivity.class);
        startActivity(intent);
    }

    private void graphClicked() {
        Intent intent = new Intent(this, GraphActivity.class);
        startActivity(intent);
    }
}
