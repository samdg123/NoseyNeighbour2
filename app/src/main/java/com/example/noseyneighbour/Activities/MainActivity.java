package com.example.noseyneighbour.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.noseyneighbour.Fragments.CurrentLocMapsFragment;
import com.example.noseyneighbour.R;

public class MainActivity extends AppCompatActivity {

    private ImageView mapIV;
    private ImageView savedCrimesIV;
    private ImageView graphIV;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapIV = findViewById(R.id.mapIV);
        savedCrimesIV = findViewById(R.id.savedCrimesIV);
        graphIV = findViewById(R.id.graphIV);

        mapIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();
                intent = new Intent(getApplicationContext(), MapsActivity.class);
            }
        });
        savedCrimesIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedCrimesClicked();
            }
        });
        graphIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();
                intent = new Intent(getApplicationContext(), GraphActivity.class);
            }
        });

        mapIV.setImageDrawable( greyOutIcon(getDrawable(R.drawable.ic_map)) );
        graphIV.setImageDrawable( greyOutIcon(getDrawable(R.drawable.ic_graph)) );

        requestPermissions();
    }

    private Drawable greyOutIcon(Drawable drawable){
        drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    private void enableButtons(){
        mapIV.setImageDrawable(getDrawable(R.drawable.ic_map));
        graphIV.setImageDrawable(getDrawable(R.drawable.ic_graph));

        mapIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customLocClicked();
            }
        });
        graphIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphClicked();
            }
        });
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableButtons();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 123){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableButtons();
                if (intent != null) {
                    startActivity(intent);
                }
            }
        }

    }

    private void customLocClicked(){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("currentLoc", false);
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
