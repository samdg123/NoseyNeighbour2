package com.example.noseyneighbour.Handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.noseyneighbour.Classes.Crime;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static DBHandler instance;

    private static String DB_NAME = "Crimes.db";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 1;

    private static final String CRIME_TABLE_NAME = "Crime";
    private static final String CRIME_COLUMN_ID = "ID";
    private static final String CRIME_COLUMN_LATITUDE = "Latitude";
    private static final String CRIME_COLUMN_LONGITUDE = "Longitude";
    private static final String CRIME_COLUMN_OUTCOME = "Outcome";
    private static final String CRIME_COLUMN_MONTH = "Month";
    private static final String CRIME_COLUMN_YEAR = "Year";
    private static final String CRIME_COLUMN_CATEGORY_ID = "CategoryID";

    private static final String CATEGORY_TABLE_NAME = "Category";
    private static final String CATEGORY_COLUMN_ID = "ID";
    private static final String CATEGORY_COLUMN_STRING = "String";

    public int countCrimesInMonth(int year, int month){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT count(" + CRIME_COLUMN_ID + ") FROM " + CRIME_TABLE_NAME +
                "WHERE " + CRIME_COLUMN_MONTH + " = " + month + " and " + CRIME_COLUMN_YEAR + " = " + year;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        return cursor.getInt(0);
    }

    public void addCrime(Crime crime){
        ContentValues crimeValues = new ContentValues();

        crimeValues.put(CRIME_COLUMN_LATITUDE, crime.getPosition().latitude);
        crimeValues.put(CRIME_COLUMN_LONGITUDE, crime.getPosition().longitude);
        crimeValues.put(CRIME_COLUMN_MONTH, crime.getMonth());
        crimeValues.put(CRIME_COLUMN_YEAR, crime.getYear());
        crimeValues.put(CRIME_COLUMN_OUTCOME, crime.getOutcomeStatus());
        crimeValues.put(CRIME_COLUMN_CATEGORY_ID, 1);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(CRIME_TABLE_NAME, null, crimeValues);

        db.close();
    }

    public ArrayList<Crime> getAllCrimes(){
        ArrayList<Crime> crimes = new ArrayList<Crime>();

        String query = "SELECT * FROM " + CRIME_TABLE_NAME + ", " + CATEGORY_TABLE_NAME +
                " WHERE " + CATEGORY_TABLE_NAME + "." + CATEGORY_COLUMN_ID + " = " + CRIME_TABLE_NAME + "." + CRIME_COLUMN_ID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()){
            Crime crime = new Crime();

            crime.setPosition(new LatLng(cursor.getFloat(1), cursor.getFloat(2)));
            crime.setOutcomeStatus(cursor.getString(3));
            crime.setCategory(cursor.getString(7));
            crime.setMonth(cursor.getInt(4));
            crime.setYear(cursor.getInt(5));

            crimes.add(crime);
        }

        cursor.close();
        db.close();

        return crimes;
    }

    public boolean addCategory(int ID, String string){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues categoryValues = new ContentValues();

        categoryValues.put(CATEGORY_COLUMN_ID, ID);
        categoryValues.put(CATEGORY_COLUMN_STRING, string);

        boolean returnVal = db.insert(CATEGORY_TABLE_NAME, null, categoryValues) != -1;

        db.close();

        return db.insert(CATEGORY_TABLE_NAME, null, categoryValues) != -1;
    }

    private void createTables(SQLiteDatabase db){
        String query = "CREATE TABLE " + CRIME_TABLE_NAME + "( " +
                CRIME_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CRIME_COLUMN_LATITUDE + " NUMERIC NOT NULL, " +
                CRIME_COLUMN_LONGITUDE + " NUMERIC NOT NULL, " +
                CRIME_COLUMN_OUTCOME + " TEXT, " +
                CRIME_COLUMN_MONTH + " INTEGER NOT NULL, " +
                CRIME_COLUMN_YEAR + " INTEGER NOT NULL )";
        db.execSQL(query);

        query = "CREATE TABLE " + CATEGORY_TABLE_NAME + "( " +
                CATEGORY_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                CATEGORY_COLUMN_STRING + " TEXT NOT NULL UNIQUE )";
        db.execSQL(query);
    }

    private void populateCategories(SQLiteDatabase db){
        addCategory(1, "all-crime");
        addCategory(2, "anti-social-behaviour");
        addCategory(3, "bicycle-theft");
        addCategory(4, "burglary");
        addCategory(5, "criminal-damage-arson");
        addCategory(6, "drugs");
        addCategory(7, "other-theft");
        addCategory(8, "possession-of-weapons");
        addCategory(9, "public-order");
        addCategory(10, "robbery");
        addCategory(11, "shoplifting");
        addCategory(12, "theft-from-the-person");
        addCategory(13, "vehicle-crime");
        addCategory(14, "violent-crime");
        addCategory(15, "other-crime");
    }

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
        populateCategories(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}