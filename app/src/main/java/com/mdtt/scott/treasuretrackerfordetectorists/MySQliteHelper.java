package com.mdtt.scott.treasuretrackerfordetectorists;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Scott on 1/25/2018.
 */

public class MySQliteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 58;
    // Database Name
    private static final String DATABASE_NAME = "findsDB";
    // Treasures table name
    private static final String TABLE_TREASURE = "Treasure";

    // Treasure Table Columns names
    private static final String colTreasureID="TreasureID";
    private static final String colTreasureType="TreasureType";
    private static final String colTreasureCountry="TreasureCountry";
    private static final String colTreasureDenomination="TreasureDenomination";
    private static final String colTreasureSeries="TreasureSeries";
    private static final String colTreasureName="TreasureName";
    private static final String colTreasureYear="TreasureYear";
    private static final String colTreasureMint="TreasureMint";
    private static final String colTreasureMaterial="TreasureMaterial";
    private static final String colTreasureWeight="TreasureWeight";
    private static final String colTreasureLocationFound="TreasureLocationFound";
    private static final String colTreasureDateFound="TreasureDateFound";
    private static final String colTreasureInfo="TreasureInfo";
    private static final String colTreasurePhotoPath="TreasurePhotoPath";

    // Clad table name
    private static final String TABLE_CLAD = "Clad";

    // Clad Table Columns names
    private static final String colCladID="CladID";
    private static final String colCladCurrency="CladCurrency";
    private static final String colCladAmount="CladAmount";
    private static final String colCladLocationFound="CladLocationFound";
    private static final String colCladDateFound="CladDateFound";


    private Context mcontext;

    public MySQliteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mcontext = context;
    }

    @Override public void onCreate(SQLiteDatabase db) {
        Log.d("myTag", "WE RECREATED DATABASE TABLES!");

        //creates treasure table
        String createTreasureTable = "CREATE TABLE "+TABLE_TREASURE +
                " ( "+colTreasureID+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                colTreasureType+" TEXT NOT NULL," +
                colTreasureCountry+" TEXT," +
                colTreasureDenomination+" TEXT," +
                colTreasureSeries+" TEXT," +
                colTreasureName+" TEXT," +
                colTreasureYear+" TEXT," +
                colTreasureMint+" TEXT," +
                colTreasureMaterial+" TEXT," +
                colTreasureWeight+" TEXT," +
                colTreasureLocationFound+" TEXT," +
                colTreasureDateFound+" TEXT," +
                colTreasureInfo+" TEXT," +
                colTreasurePhotoPath+" TEXT)";

        Log.d("myTag", createTreasureTable);
        db.execSQL(createTreasureTable);

        //creates treasure table
        String createCladTable = "CREATE TABLE "+TABLE_CLAD +
                " ( "+colCladID+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                colCladCurrency+" TEXT NOT NULL," +
                colCladAmount+" DOUBLE NOT NULL," +
                colCladLocationFound+" TEXT," +
                colCladDateFound+" TEXT)";

        Log.d("myTag", createCladTable);
        db.execSQL(createCladTable);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TREASURE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CLAD);
        onCreate(db);
    }

    //query used to return id,year,denomination,foundyear,photopath of all coins to populate grid view.
    //paramater determines sorting: AddDate, TreasureYear, TreasureFoundYear
    public ArrayList<Treasure> getAllCoins(String sortType)
    {
        ArrayList<Treasure> treasureList = new ArrayList<>();
        String selectQuery;
        if(sortType.equals("TreasureYear") || sortType.equals("TreasureLocationFound"))
        {
            selectQuery = "SELECT "+colTreasureID+", "+colTreasureYear+","+colTreasureSeries+","+colTreasureDateFound+","
                    +colTreasurePhotoPath+" FROM "+TABLE_TREASURE+" WHERE "+colTreasureType+"='coin' ORDER BY (CASE WHEN "+sortType+" IS \"\" THEN 1 ELSE 0 END), "+sortType;
        }
        else if(sortType.equals("TreasureCountry"))
        {
            selectQuery = "SELECT "+colTreasureID+", "+colTreasureYear+","+colTreasureSeries+","+colTreasureDateFound+","
                    +colTreasurePhotoPath+" FROM "+TABLE_TREASURE+" WHERE "+colTreasureType+"='coin' ORDER BY "+sortType+" ASC";
        }
        //treasure date found, most recently added
        else
        {
            selectQuery = "SELECT "+colTreasureID+", "+colTreasureYear+","+colTreasureSeries+","+colTreasureDateFound+","
                    +colTreasurePhotoPath+" FROM "+TABLE_TREASURE+" WHERE "+colTreasureType+"='coin' ORDER BY "+sortType+" DESC";
        }

        Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                treasureList.add(new Treasure(cursor.getInt(cursor.getColumnIndex(colTreasureID)), null, null, null, cursor.getString(cursor.getColumnIndex(colTreasureSeries)), null, cursor.getString(cursor.getColumnIndex(colTreasureYear)), null, null,null ,null, cursor.getString(cursor.getColumnIndex(colTreasureDateFound)), null, cursor.getString(cursor.getColumnIndex(colTreasurePhotoPath))));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return treasureList;
    }

    //query used to return id,year,name,foundyear,photopath of all tokens to populate grid view.
    //paramater determines sorting
    public ArrayList<Treasure> getAllTokens(String sortType)
    {
        ArrayList<Treasure> treasureList = new ArrayList<>();
        String selectQuery;
        if(sortType.equals("TreasureYear") || sortType.equals("TreasureLocationFound") || sortType.equals("TreasureName"))
        {
            selectQuery = "SELECT "+colTreasureID+", "+colTreasureYear+","+colTreasureName+","+colTreasureDateFound+","
                    +colTreasurePhotoPath+" FROM "+TABLE_TREASURE+" WHERE "+colTreasureType+"='token' ORDER BY (CASE WHEN "+sortType+" IS \"\" THEN 1 ELSE 0 END), "+sortType;
        }
        //treasure date found, most recently added
        else
        {
            selectQuery = "SELECT "+colTreasureID+", "+colTreasureYear+","+colTreasureName+","+colTreasureDateFound+","
                    +colTreasurePhotoPath+" FROM "+TABLE_TREASURE+" WHERE "+colTreasureType+"='token' ORDER BY "+sortType+" DESC";
        }

        Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                treasureList.add(new Treasure(cursor.getInt(cursor.getColumnIndex(colTreasureID)), null, null, null, null, cursor.getString(cursor.getColumnIndex(colTreasureName)), cursor.getString(cursor.getColumnIndex(colTreasureYear)), null, null,null ,null, cursor.getString(cursor.getColumnIndex(colTreasureDateFound)), null, cursor.getString(cursor.getColumnIndex(colTreasurePhotoPath))));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return treasureList;
    }

    //query used to return id,name,material,foundyear,photopath of all jewelry to populate grid view.
    //paramater determines sorting
    public ArrayList<Treasure> getAllJewelry(String sortType)
    {
        ArrayList<Treasure> treasureList = new ArrayList<>();
        String selectQuery;

        if(sortType.equals("TreasureMaterial"))
        {
            selectQuery = "SELECT "+colTreasureID+", "+colTreasureMaterial+","+colTreasureName+","+colTreasureDateFound+","
                +colTreasurePhotoPath+" FROM "+TABLE_TREASURE+" WHERE "+colTreasureType+"='jewelry' ORDER BY "+sortType+" ASC";
        }
        else if(sortType.equals("TreasureWeight") || sortType.equals("TreasureYear") || sortType.equals("TreasureLocationFound"))
        {
            selectQuery = "SELECT "+colTreasureID+", "+colTreasureMaterial+","+colTreasureName+","+colTreasureDateFound+","
                    +colTreasurePhotoPath+" FROM "+TABLE_TREASURE+" WHERE "+colTreasureType+"='jewelry' ORDER BY (CASE WHEN "+sortType+" IS \"\" THEN 1 ELSE 0 END), "+sortType;
        }
        //treasure date found, most recently added
        else
        {
            selectQuery = "SELECT "+colTreasureID+", "+colTreasureMaterial+","+colTreasureName+","+colTreasureDateFound+","
                    +colTreasurePhotoPath+" FROM "+TABLE_TREASURE+" WHERE "+colTreasureType+"='jewelry' ORDER BY "+sortType+" DESC";
        }

        Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                treasureList.add(new Treasure(cursor.getInt(cursor.getColumnIndex(colTreasureID)), null, null, null, null, cursor.getString(cursor.getColumnIndex(colTreasureName)), null, null, cursor.getString(cursor.getColumnIndex(colTreasureMaterial)),null ,null, cursor.getString(cursor.getColumnIndex(colTreasureDateFound)), null, cursor.getString(cursor.getColumnIndex(colTreasurePhotoPath))));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return treasureList;
    }

    //query used to return id,name,material,foundyear,photopath of all relics to populate grid view.
    //paramater determines sorting
    public ArrayList<Treasure> getAllRelics(String sortType)
    {
        ArrayList<Treasure> treasureList = new ArrayList<>();
        String selectQuery;

        if(sortType.equals("TreasureYear") || sortType.equals("TreasureLocationFound"))
        {
            selectQuery = "SELECT "+colTreasureID+", "+colTreasureName+","+colTreasureYear+","+colTreasureDateFound+","
                +colTreasurePhotoPath+" FROM "+TABLE_TREASURE+" WHERE "+colTreasureType+"='relic' ORDER BY (CASE WHEN "+sortType+" IS \"\" THEN 1 ELSE 0 END), "+sortType;
        }
        //treasure date found, most recently added
        else
        {
            selectQuery = "SELECT "+colTreasureID+", "+colTreasureName+","+colTreasureYear+","+colTreasureDateFound+","
                    +colTreasurePhotoPath+" FROM "+TABLE_TREASURE+" WHERE "+colTreasureType+"='relic' ORDER BY "+sortType+" DESC";
        }

        Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                treasureList.add(new Treasure(cursor.getInt(cursor.getColumnIndex(colTreasureID)), null, null, null, null, cursor.getString(cursor.getColumnIndex(colTreasureName)), cursor.getString(cursor.getColumnIndex(colTreasureYear)), null, null,null ,null, cursor.getString(cursor.getColumnIndex(colTreasureDateFound)), null, cursor.getString(cursor.getColumnIndex(colTreasurePhotoPath))));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return treasureList;
    }

    public ArrayList<Treasure> getAllCollections(String sortType)
    {
        ArrayList<Treasure> treasureList = new ArrayList<>();
        String selectQuery;

        if(sortType.equals("TreasureLocationFound"))
        {
            selectQuery = "SELECT "+colTreasureID+", "+colTreasureName+","+colTreasureDateFound+","
                    +colTreasurePhotoPath+" FROM "+TABLE_TREASURE+" WHERE "+colTreasureType+"='collection' ORDER BY (CASE WHEN "+sortType+" IS \"\" THEN 1 ELSE 0 END), "+sortType;
        }
        //treasure date found, most recently added
        else
        {
            selectQuery = "SELECT "+colTreasureID+", "+colTreasureName+","+colTreasureDateFound+","
                    +colTreasurePhotoPath+" FROM "+TABLE_TREASURE+" WHERE "+colTreasureType+"='collection' ORDER BY "+sortType+" DESC";
        }

        Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                treasureList.add(new Treasure(cursor.getInt(cursor.getColumnIndex(colTreasureID)), null, null, null, null, cursor.getString(cursor.getColumnIndex(colTreasureName)), null, null, null,null,null, cursor.getString(cursor.getColumnIndex(colTreasureDateFound)), null, cursor.getString(cursor.getColumnIndex(colTreasurePhotoPath))));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        Log.d("myTag", ""+treasureList.size());

        return treasureList;
    }

    public Treasure getDetailedTreasure(int treasureId)
    {
        Treasure treasure;
        String selectQuery = "SELECT * FROM "+TABLE_TREASURE+" WHERE "+colTreasureID+"="+treasureId;
        Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("myTag", "query retrieved.");
        if(cursor.moveToFirst()){
            treasure = new Treasure(cursor.getInt(cursor.getColumnIndex(colTreasureID)), cursor.getString(cursor.getColumnIndex(colTreasureType)), cursor.getString(cursor.getColumnIndex(colTreasureCountry)), cursor.getString(cursor.getColumnIndex(colTreasureDenomination)), cursor.getString(cursor.getColumnIndex(colTreasureSeries)), cursor.getString(cursor.getColumnIndex(colTreasureName)), cursor.getString(cursor.getColumnIndex(colTreasureYear)), cursor.getString(cursor.getColumnIndex(colTreasureMint)), cursor.getString(cursor.getColumnIndex(colTreasureMaterial)),cursor.getString(cursor.getColumnIndex(colTreasureWeight)) ,cursor.getString(cursor.getColumnIndex(colTreasureLocationFound)), cursor.getString(cursor.getColumnIndex(colTreasureDateFound)), cursor.getString(cursor.getColumnIndex(colTreasureInfo)), cursor.getString(cursor.getColumnIndex(colTreasurePhotoPath)));
            cursor.close();
            db.close();
            return treasure;
        }
        else
        {
            cursor.close();
            db.close();
            return null;
        }
    }


    public ArrayList<Clad> getAllClad(String sortType)
    {
        ArrayList<Clad> cladList = new ArrayList<>();
        String selectQuery;

        if(sortType.equals("CladLocationFound"))
        {
            selectQuery = "SELECT "+colCladID+","+colCladCurrency+","+colCladAmount+","+colCladLocationFound+","
                    +colCladDateFound+" FROM "+TABLE_CLAD+" ORDER BY (CASE WHEN "+sortType+" IS \"\" THEN 1 ELSE 0 END), "+sortType;
        }
        //treasure date found, most recently added
        else
        {
            selectQuery = selectQuery = "SELECT "+colCladID+","+colCladCurrency+","+colCladAmount+","+colCladLocationFound+","
                    +colCladDateFound+" FROM "+TABLE_CLAD+" ORDER BY "+sortType+" DESC";
        }

        Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                cladList.add(new Clad(cursor.getInt(cursor.getColumnIndex(colCladID)), cursor.getString(cursor.getColumnIndex(colCladCurrency)), cursor.getDouble(cursor.getColumnIndex(colCladAmount)), cursor.getString(cursor.getColumnIndex(colCladLocationFound)), cursor.getString(cursor.getColumnIndex(colCladDateFound))));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return cladList;
    }

    public HashMap<String, Integer> getSummaryTreasure()
    {
        HashMap<String, Integer> summaryList = new HashMap<>();
        String selectQuery = "SELECT "+colTreasureType+", COUNT("+colTreasureType+") FROM "+TABLE_TREASURE+" GROUP BY "+colTreasureType;
        Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                Log.d("myTag", "cursor entry...");
                Log.d("myTag", cursor.getString(0)+": "+cursor.getInt(1));
                summaryList.put(cursor.getString(0), cursor.getInt(1));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return summaryList;
    }

    public LinkedHashMap<String, Double> getSummaryClad()
    {
        LinkedHashMap<String, Double> summaryList = new LinkedHashMap<>();
        String selectQuery = "SELECT "+colCladCurrency+", SUM("+colCladAmount+") FROM "+TABLE_CLAD+" GROUP BY "+colCladCurrency+ " ORDER BY SUM("+colCladAmount+") DESC";
        Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                Log.d("myTag", "cursor entry...");
                Log.d("myTag", cursor.getString(0)+": "+cursor.getDouble(1));
                summaryList.put(cursor.getString(0), cursor.getDouble(1));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return summaryList;
    }

    public long addTreasure(Treasure treasure)
    {
        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(colTreasureCountry, treasure.getTreasureCountry()); // get country
        values.put(colTreasureName, treasure.getTreasureName()); // get name
        values.put(colTreasureType, treasure.getTreasureType()); // get type
        values.put(colTreasureDenomination, treasure.getTreasureDenomination()); // get denomination
        values.put(colTreasureSeries, treasure.getTreasureSeries()); // get series
        values.put(colTreasureYear, treasure.getTreasureYear()); // get year
        values.put(colTreasureMint, treasure.getTreasureMint()); // get mint
        values.put(colTreasureMaterial, treasure.getTreasureMaterial()); // get material
        values.put(colTreasureWeight, treasure.getTreasureWeight()); // get weight
        values.put(colTreasureDateFound, treasure.getTreasureDateFound()); // get datefound
        values.put(colTreasureLocationFound, treasure.getTreasureLocationFound()); // get locationfound
        values.put(colTreasureInfo, treasure.getTreasureInfo()); // get info
        values.put(colTreasurePhotoPath, treasure.getTreasurePhotoPath()); // get photopath

        //result will contain the row ID of the newly inserted row, or -1 if an error occurred.
        long result = db.insert(TABLE_TREASURE, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        //close
        db.close();
        return result;
    }

    public long addClad(Clad clad)
    {
        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(colCladCurrency, clad.getCladCurrency()); // get currency
        values.put(colCladAmount, clad.getCladAmount()); // get amount
        values.put(colCladLocationFound, clad.getCladLocationFound()); // get location found
        values.put(colCladDateFound, clad.getCladDateFound()); // get date found

        //result will contain the row ID of the newly inserted row, or -1 if an error occurred.
        long result = db.insert(TABLE_CLAD, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        //close
        db.close();
        return result;
    }

    public int deleteTreasure(String treasureID)
    {
        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_TREASURE, colTreasureID+"=?",new String[]{treasureID});
        db.close();
        return result;
    }

    public int deleteClad(String cladID)
    {
        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_CLAD, colCladID+"=?",new String[]{cladID});
        db.close();
        return result;
    }
}
