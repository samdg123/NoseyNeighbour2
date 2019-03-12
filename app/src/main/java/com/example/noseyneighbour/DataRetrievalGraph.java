package com.example.noseyneighbour;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.JsonReader;
import android.util.JsonToken;

import com.example.noseyneighbour.Activities.GraphActivity;
import com.example.noseyneighbour.Classes.Crime;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataRetrievalGraph extends DataRetrievalParent {

    private Activity activity;

    //int[] in format of int[0] = number of crimes, int[1] = year of crimes, int[2] = month of crimes
    private int[] numCrimes;

    public DataRetrievalGraph(String crimeType, int year, int month, float radius, Location globalLocation, Context context, Activity activity) {
        super(crimeType, year, month, radius, globalLocation, context);
        this.activity = activity;

        numCrimes = new int[3];

        numCrimes[0] = 0;
        numCrimes[1] = year;
        numCrimes[2] = month;
    }

    @Override
    public void parseJSON(InputStream inputStream) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

        reader.beginArray();
        if (reader.peek() == JsonToken.END_ARRAY) {
            return;
        }


        while (reader.hasNext()) {
            numCrimes[0] ++;
            reader.skipValue();
        }

        reader.close();
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        ((GraphActivity) activity).addToNumCrimesList(numCrimes);
    }
}
