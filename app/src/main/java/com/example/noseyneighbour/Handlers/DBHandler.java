package com.example.noseyneighbour.Handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.noseyneighbour.Classes.Crime;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static DBHandler instance;

    private static String DB_NAME = "crimes.db";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 1;

    private static final String CRIME_TABLE_NAME = "Crime";
    private static final String CRIME_COLUMN_ID = "ID";
    private static final String CRIME_COLUMN_LATITUDE = "Latitude";
    private static final String CRIME_COLUMN_LONGITUDE = "Longitude";
    private static final String CRIME_COLUMN_OUTCOME = "Outcome";
    private static final String CRIME_COLUMN_MONTH = "Month";
    private static final String CRIME_COLUMN_YEAR = "Year";
    private static final String CRIME_COLUMN_CATEGORY = "Category";
    private static final String CRIME_COLUMN_LOCATION_DESC = "LocationDesc";

    //private static final String CATEGORY_TABLE_NAME = "Category";
    //private static final String CATEGORY_COLUMN_ID = "ID";
    //private static final String CATEGORY_COLUMN_STRING = "String";

    private static final String FAVORITE_TABLE_NAME = "Favorite";
    private static final String FAVORITE_COLUMN_CRIME_ID = "CrimeID";

    public int crimesTableSize(){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT count(" + CRIME_COLUMN_YEAR + ") FROM " + CRIME_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        return cursor.getInt(0);
    }

    public int countHighestCrimeMonth(){
        int highest;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT count(*) as crimes from " + CRIME_TABLE_NAME +
                " group by " + CRIME_COLUMN_YEAR + ", " + CRIME_COLUMN_MONTH +
                " order by crimes desc limit 1";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        highest = cursor.getInt(0);

        db.close();
        return highest;
    }

    public int countLowestCrimeMonth(){
        int lowest;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT count(*) as crimes from " + CRIME_TABLE_NAME +
                " group by " + CRIME_COLUMN_YEAR + ", " + CRIME_COLUMN_MONTH +
                " order by crimes asc limit 1";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        lowest = cursor.getInt(0);

        db.close();
        return lowest;
    }

    public ArrayList<int[]> countCrimesForAllMonths(){
        ArrayList<int[]> numCrimes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT COUNT(*), " + CRIME_COLUMN_YEAR + ", " + CRIME_COLUMN_MONTH + " FROM " + CRIME_TABLE_NAME +
                " GROUP BY " + CRIME_COLUMN_YEAR + ", " + CRIME_COLUMN_MONTH +
                " ORDER BY " + CRIME_COLUMN_YEAR + ", " + CRIME_COLUMN_MONTH;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            int[] value = new int[3];

            value[0] = cursor.getInt(0);
            value[1] = cursor.getInt(1);
            value[2] = cursor.getInt(2);

            numCrimes.add(value);
        }

        Log.d("DBHandler", numCrimes.size() + " number of months");
        db.close();

        return numCrimes;
    }

    public int countCrimesInMonth(int year, int month){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT count(" + CRIME_COLUMN_YEAR + ") FROM " + CRIME_TABLE_NAME +
                " WHERE " + CRIME_COLUMN_MONTH + " = " + month + " and " + CRIME_COLUMN_YEAR + " = " + year;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        return cursor.getInt(0);
    }

    public void addCrimes(ArrayList<Crime> crimes) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (Crime crime : crimes) {

            ContentValues crimeValues = new ContentValues();

            crimeValues.put(CRIME_COLUMN_LATITUDE, crime.getPosition().latitude);
            crimeValues.put(CRIME_COLUMN_LONGITUDE, crime.getPosition().longitude);
            crimeValues.put(CRIME_COLUMN_MONTH, crime.getMonth());
            crimeValues.put(CRIME_COLUMN_YEAR, crime.getYear());
            crimeValues.put(CRIME_COLUMN_OUTCOME, crime.getOutcomeStatus());
            crimeValues.put(CRIME_COLUMN_CATEGORY, crime.getCategory());
            crimeValues.put(CRIME_COLUMN_LOCATION_DESC, crime.getLocationDesc());

            db.replace(CRIME_TABLE_NAME, null, crimeValues);
            Log.d("DBHandler", "crime added, year = " + crime.getYear() + ", month = " + crime.getMonth());
            Log.d("DBHandler", "Now " + crimesTableSize() + " items in crime table");
        }

        db.close();
    }

    public void addCrime(Crime crime){
        ContentValues crimeValues = new ContentValues();

        crimeValues.put(CRIME_COLUMN_LATITUDE, crime.getPosition().latitude);
        crimeValues.put(CRIME_COLUMN_LONGITUDE, crime.getPosition().longitude);
        crimeValues.put(CRIME_COLUMN_MONTH, crime.getMonth());
        crimeValues.put(CRIME_COLUMN_YEAR, crime.getYear());
        crimeValues.put(CRIME_COLUMN_OUTCOME, crime.getOutcomeStatus());
        crimeValues.put(CRIME_COLUMN_CATEGORY, crime.getCategory());
        crimeValues.put(CRIME_COLUMN_LOCATION_DESC, crime.getLocationDesc());

        SQLiteDatabase db = this.getWritableDatabase();
        //db.insert(CRIME_TABLE_NAME, null, crimeValues);
        db.replace(CRIME_TABLE_NAME, null, crimeValues);

        Log.d("DBHandler", "crime added, year = " + crime.getYear() + ", month = " + crime.getMonth());
        db.close();
    }

    public ArrayList<Crime> getAllCrimes(){
        ArrayList<Crime> crimes = new ArrayList<Crime>();

        String query = "SELECT  " + CRIME_COLUMN_LATITUDE + ", " + CRIME_COLUMN_LONGITUDE + ", " + CRIME_COLUMN_OUTCOME + ", " + CRIME_COLUMN_MONTH + ", " + CRIME_COLUMN_YEAR + ", " + CRIME_COLUMN_CATEGORY + ", " + CRIME_COLUMN_LOCATION_DESC +
                " FROM " + CRIME_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()){
            Crime crime = new Crime();

            crime.setPosition(new LatLng(cursor.getFloat(0), cursor.getFloat(1)));
            crime.setOutcomeStatus(cursor.getString(2));
            crime.setCategory(cursor.getString(5));
            crime.setMonth(cursor.getInt(3));
            crime.setYear(cursor.getInt(4));
            crime.setLocationDesc(cursor.getString(6));

            crimes.add(crime);
        }

        cursor.close();
        db.close();



        return crimes;
    }

    //public boolean addCategory(SQLiteDatabase db, int ID, String string){
    //    ContentValues categoryValues = new ContentValues();
//
    //    categoryValues.put(CATEGORY_COLUMN_ID, ID);
    //    categoryValues.put(CATEGORY_COLUMN_STRING, string);
//
    //    boolean returnVal = db.insert(CATEGORY_TABLE_NAME, null, categoryValues) != -1;
//
    //    return db.insert(CATEGORY_TABLE_NAME, null, categoryValues) != -1;
    //}

    private void createTables(SQLiteDatabase db){
        String query = "CREATE TABLE " + CRIME_TABLE_NAME + "( " +
                CRIME_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CRIME_COLUMN_LATITUDE + " NUMERIC NOT NULL, " +
                CRIME_COLUMN_LONGITUDE + " NUMERIC NOT NULL, " +
                CRIME_COLUMN_OUTCOME + " TEXT, " +
                CRIME_COLUMN_MONTH + " INTEGER NOT NULL, " +
                CRIME_COLUMN_YEAR + " INTEGER NOT NULL, " +
                CRIME_COLUMN_CATEGORY + " TEXT NOT NULL, " +
                CRIME_COLUMN_LOCATION_DESC + " TEXT, " +
                "UNIQUE (" + CRIME_COLUMN_LATITUDE + ", " + CRIME_COLUMN_LONGITUDE + ", " + CRIME_COLUMN_OUTCOME + ", " + CRIME_COLUMN_MONTH + ", " + CRIME_COLUMN_YEAR + ", " + CRIME_COLUMN_CATEGORY + ")" +
                ")";
        db.execSQL(query);

        //query = "CREATE TABLE " + CATEGORY_TABLE_NAME + "( " +
        //        CATEGORY_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
        //        CATEGORY_COLUMN_STRING + " TEXT NOT NULL UNIQUE )";
        //db.execSQL(query);

        query = "CREATE TABLE " + FAVORITE_TABLE_NAME + "( " +
                FAVORITE_COLUMN_CRIME_ID + " INTEGER NOT NULL UNIQUE, PRIMARY KEY ( " + FAVORITE_COLUMN_CRIME_ID + " ))";
        db.execSQL(query);
    }

    //private void populateCategories(SQLiteDatabase db){
    //    addCategory(db, 1, "all-crime");
    //    addCategory(db, 2, "anti-social-behaviour");
    //    addCategory(db, 3, "bicycle-theft");
    //    addCategory(db, 4, "burglary");
    //    addCategory(db, 5, "criminal-damage-arson");
    //    addCategory(db, 6, "drugs");
    //    addCategory(db, 7, "other-theft");
    //    addCategory(db, 8, "possession-of-weapons");
    //    addCategory(db, 9, "public-order");
    //    addCategory(db, 10, "robbery");
    //    addCategory(db, 11, "shoplifting");
    //    addCategory(db, 12, "theft-from-the-person");
    //    addCategory(db, 13, "vehicle-crime");
    //    addCategory(db, 14, "violent-crime");
    //    addCategory(db, 15, "other-crime");
    //}

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
