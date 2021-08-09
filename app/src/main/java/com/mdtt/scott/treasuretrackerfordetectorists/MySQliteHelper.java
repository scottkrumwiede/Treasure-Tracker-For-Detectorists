package com.mdtt.scott.treasuretrackerfordetectorists;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Scott on 1/25/2018.
 */

@SuppressWarnings("WeakerAccess")
public class MySQliteHelper extends SQLiteOpenHelper {

    // Database Version
    //58 is live version so don't change this without migration in place
    private static final int DATABASE_VERSION = 65;
    // Database Name
    private static final String DATABASE_NAME = "findsDB";
    // Treasures table name
    private static final String TABLE_TREASURE = "Treasure";

    // Treasure Table Columns names
    private static final String COL_TREASURE_ID ="TreasureID";
    private static final String COL_TREASURE_TYPE ="TreasureType";
    private static final String COL_TREASURE_COUNTRY ="TreasureCountry";
    private static final String COL_TREASURE_DENOMINATION ="TreasureDenomination";
    private static final String COL_TREASURE_SERIES ="TreasureSeries";
    private static final String COL_TREASURE_NAME ="TreasureName";
    private static final String COL_TREASURE_YEAR ="TreasureYear";
    private static final String COL_TREASURE_MINT ="TreasureMint";
    private static final String COL_TREASURE_MATERIAL ="TreasureMaterial";
    private static final String COL_TREASURE_WEIGHT ="TreasureWeight";
    private static final String COL_TREASURE_LOCATION_FOUND ="TreasureLocationFound";
    private static final String COL_TREASURE_DATE_FOUND ="TreasureDateFound";
    private static final String COL_TREASURE_INFO ="TreasureInfo";
    private static final String COL_TREASURE_PHOTO_PATH ="TreasurePhotoPath";

    private static final String COL_TREASURE_WEIGHT_UNIT ="TreasureWeightUnit";

    // Clad table name
    private static final String TABLE_CLAD = "Clad";

    // Clad Table Columns names
    private static final String COL_CLAD_ID ="CladID";
    private static final String COL_CLAD_CURRENCY ="CladCurrency";
    private static final String COL_CLAD_AMOUNT ="CladAmount";
    private static final String COL_CLAD_LOCATION_FOUND ="CladLocationFound";
    private static final String COL_CLAD_DATE_FOUND ="CladDateFound";

    //treasure table
    private static final String CREATE_TREASURE_TABLE = "CREATE TABLE "+TABLE_TREASURE +
            " ( "+ COL_TREASURE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            COL_TREASURE_TYPE +" TEXT NOT NULL," +
            COL_TREASURE_COUNTRY +" TEXT," +
            COL_TREASURE_DENOMINATION +" TEXT," +
            COL_TREASURE_SERIES +" TEXT," +
            COL_TREASURE_NAME +" TEXT," +
            COL_TREASURE_YEAR +" TEXT," +
            COL_TREASURE_MINT +" TEXT," +
            COL_TREASURE_MATERIAL +" TEXT," +
            COL_TREASURE_WEIGHT +" TEXT," +
            COL_TREASURE_LOCATION_FOUND +" TEXT," +
            COL_TREASURE_DATE_FOUND +" TEXT," +
            COL_TREASURE_INFO +" TEXT," +
            COL_TREASURE_PHOTO_PATH +" TEXT," +
            COL_TREASURE_WEIGHT_UNIT +" TEXT NOT NULL DEFAULT 'Grams (g)')";

    //clad table
    private static final String CREATE_CLAD_TABLE = "CREATE TABLE "+TABLE_CLAD +
            " ( "+ COL_CLAD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            COL_CLAD_CURRENCY +" TEXT NOT NULL," +
            COL_CLAD_AMOUNT +" DOUBLE NOT NULL," +
            COL_CLAD_LOCATION_FOUND +" TEXT," +
            COL_CLAD_DATE_FOUND +" TEXT)";

    //alter to treasure table (database version 62)
    private static final String ALTER_TREASURE_TABLE_1 = "ALTER TABLE " + TABLE_TREASURE + " ADD COLUMN " + COL_TREASURE_WEIGHT_UNIT + " string NOT NULL DEFAULT 'Grams (g)';";

    //potential future alter to treasure table (database version 63)
    //private static final String ALTER_TREASURE_TABLE_2 = "ALTER TABLE " + TABLE_TREASURE + " ADD COLUMN " + COL_TREASURE_WEIGHT_UNIT + " string;";

    public MySQliteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TREASURE_TABLE);
        db.execSQL(CREATE_CLAD_TABLE);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion < 65)
        {
            //Log.d("myTag", "updating to version 65");
            db.execSQL(ALTER_TREASURE_TABLE_1);
        }
        //potential future upgrade
        //if(oldVersion < 66)
        //{
            //db.execSQL(ALTER_TREASURE_TABLE_2);
        //}
    }

    //query used to return photopath of treasure table for purpose of fast clear.
    public ArrayList<String> getPhotopathAllTreasure(String type)
    {
        ArrayList<String> photoPathList = new ArrayList<>();
        String selectQuery;

        //get photopaths of all types
        if(type.equals("all"))
        {
            selectQuery = "SELECT "+ COL_TREASURE_PHOTO_PATH +" FROM "+TABLE_TREASURE;
        }
        else
        {
            //get photopaths of type specified
            selectQuery = "SELECT "+ COL_TREASURE_PHOTO_PATH +" FROM "+TABLE_TREASURE+ " WHERE "+COL_TREASURE_TYPE+"= '"+type+"'";
        }

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                photoPathList.add(cursor.getString(cursor.getColumnIndex(COL_TREASURE_PHOTO_PATH)));
            } while(cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return photoPathList;
    }

    //query used to return id,year,denomination,foundyear,photopath of all coins to populate grid view.
    //paramater determines sorting: AddDate, TreasureYear, TreasureFoundYear
    public ArrayList<Treasure> getAllCoins(String sortType)
    {
        ArrayList<Treasure> treasureList = new ArrayList<>();
        String selectQuery;
        if(sortType.equals("TreasureYear"))
        {
            selectQuery = "SELECT "+ COL_TREASURE_ID +", "+ COL_TREASURE_YEAR +","+ COL_TREASURE_SERIES +","+ COL_TREASURE_DATE_FOUND +","
                    + COL_TREASURE_PHOTO_PATH +","+ COL_TREASURE_LOCATION_FOUND +","+ COL_TREASURE_COUNTRY +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_TYPE +"='coin' ORDER BY (CASE WHEN "+sortType+" IS \"\" THEN 1 ELSE 0 END), "+sortType;
        }
        else if(sortType.equals("TreasureLocationFound"))
        {
            selectQuery = "SELECT "+ COL_TREASURE_ID +", "+ COL_TREASURE_YEAR +","+ COL_TREASURE_SERIES +","+ COL_TREASURE_DATE_FOUND +","
                    + COL_TREASURE_PHOTO_PATH +","+ COL_TREASURE_LOCATION_FOUND +","+ COL_TREASURE_COUNTRY +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_TYPE +"='coin' ORDER BY (CASE WHEN "+sortType+" IS \"\" THEN 1 ELSE 0 END), "+"lower("+sortType+")";
        }
        else if(sortType.equals("TreasureCountry"))
        {
            selectQuery = "SELECT "+ COL_TREASURE_ID +", "+ COL_TREASURE_YEAR +","+ COL_TREASURE_SERIES +","+ COL_TREASURE_DATE_FOUND +","
                    + COL_TREASURE_PHOTO_PATH +","+ COL_TREASURE_LOCATION_FOUND +","+ COL_TREASURE_COUNTRY +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_TYPE +"='coin' ORDER BY "+"lower("+sortType+") ASC";
        }
        else if(sortType.equals("TreasureDateFound"))
        {
            selectQuery = "SELECT "+ COL_TREASURE_ID +", "+ COL_TREASURE_YEAR +","+ COL_TREASURE_SERIES +","+ COL_TREASURE_DATE_FOUND +","
                    + COL_TREASURE_PHOTO_PATH +","+ COL_TREASURE_LOCATION_FOUND +","+ COL_TREASURE_COUNTRY +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_TYPE +"='coin' ORDER BY "+sortType+" DESC";
        }
        //most recently added
        else
        {
            selectQuery = "SELECT "+ COL_TREASURE_ID +", "+ COL_TREASURE_YEAR +","+ COL_TREASURE_SERIES +","+ COL_TREASURE_DATE_FOUND +","
                    + COL_TREASURE_PHOTO_PATH +","+ COL_TREASURE_LOCATION_FOUND +","+ COL_TREASURE_COUNTRY +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_TYPE +"='coin' ORDER BY "+sortType+" DESC";
        }

        //Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                treasureList.add(new Treasure(cursor.getInt(cursor.getColumnIndex(COL_TREASURE_ID)), null, cursor.getString(cursor.getColumnIndex(COL_TREASURE_COUNTRY)), null, cursor.getString(cursor.getColumnIndex(COL_TREASURE_SERIES)), null, cursor.getString(cursor.getColumnIndex(COL_TREASURE_YEAR)), null, null,null ,cursor.getString(cursor.getColumnIndex(COL_TREASURE_LOCATION_FOUND)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_DATE_FOUND)), null, cursor.getString(cursor.getColumnIndex(COL_TREASURE_PHOTO_PATH)), null));
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
        if(sortType.equals("TreasureYear"))
        {
            selectQuery = "SELECT "+ COL_TREASURE_ID +", "+ COL_TREASURE_YEAR +","+ COL_TREASURE_NAME +","+ COL_TREASURE_DATE_FOUND +","
                    + COL_TREASURE_PHOTO_PATH +","+ COL_TREASURE_LOCATION_FOUND +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_TYPE +"='token' ORDER BY (CASE WHEN "+sortType+" IS \"\" THEN 1 ELSE 0 END), "+sortType;
        }
        else if(sortType.equals("TreasureName"))
        {
            selectQuery = "SELECT "+ COL_TREASURE_ID +", "+ COL_TREASURE_YEAR +","+ COL_TREASURE_NAME +","+ COL_TREASURE_DATE_FOUND +","
                    + COL_TREASURE_PHOTO_PATH +","+ COL_TREASURE_LOCATION_FOUND +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_TYPE +"='token' ORDER BY (CASE WHEN "+sortType+" IS \"\" THEN 1 ELSE 0 END), "+"lower("+sortType+")";
        }
        else if(sortType.equals("TreasureLocationFound"))
        {
            selectQuery = "SELECT "+ COL_TREASURE_ID +", "+ COL_TREASURE_YEAR +","+ COL_TREASURE_NAME +","+ COL_TREASURE_DATE_FOUND +","
                    + COL_TREASURE_PHOTO_PATH +","+ COL_TREASURE_LOCATION_FOUND +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_TYPE +"='token' ORDER BY (CASE WHEN "+sortType+" IS \"\" THEN 1 ELSE 0 END), "+"lower("+sortType+")";
        }
        //treasure date found, most recently added
        else
        {
            selectQuery = "SELECT "+ COL_TREASURE_ID +", "+ COL_TREASURE_YEAR +","+ COL_TREASURE_NAME +","+ COL_TREASURE_DATE_FOUND +","
                    + COL_TREASURE_PHOTO_PATH +","+ COL_TREASURE_LOCATION_FOUND +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_TYPE +"='token' ORDER BY "+sortType+" DESC";
        }

        //Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                treasureList.add(new Treasure(cursor.getInt(cursor.getColumnIndex(COL_TREASURE_ID)), null, null, null, null, cursor.getString(cursor.getColumnIndex(COL_TREASURE_NAME)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_YEAR)), null, null,null ,cursor.getString(cursor.getColumnIndex(COL_TREASURE_LOCATION_FOUND)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_DATE_FOUND)), null, cursor.getString(cursor.getColumnIndex(COL_TREASURE_PHOTO_PATH)), null));
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
            selectQuery = "SELECT "+ COL_TREASURE_ID +", "+ COL_TREASURE_MATERIAL +","+ COL_TREASURE_NAME +","+ COL_TREASURE_DATE_FOUND +","
                    + COL_TREASURE_PHOTO_PATH +","+ COL_TREASURE_LOCATION_FOUND +","+ COL_TREASURE_WEIGHT +","+ COL_TREASURE_YEAR +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_TYPE +"='jewelry' ORDER BY lower("+sortType+") ASC";
        }
        else if(sortType.equals("TreasureWeight"))
        {
            selectQuery = "SELECT "+ COL_TREASURE_ID +", "+ COL_TREASURE_MATERIAL +","+ COL_TREASURE_NAME +","+ COL_TREASURE_DATE_FOUND +","
                    + COL_TREASURE_PHOTO_PATH +","+ COL_TREASURE_LOCATION_FOUND +","+ COL_TREASURE_WEIGHT +","+ COL_TREASURE_YEAR +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_TYPE +"='jewelry' ORDER BY (CASE WHEN "+sortType+" IS \"\" THEN 1 ELSE 0 END), "+sortType;
        }
        else if(sortType.equals("TreasureYear"))
        {
            selectQuery = "SELECT "+ COL_TREASURE_ID +", "+ COL_TREASURE_MATERIAL +","+ COL_TREASURE_NAME +","+ COL_TREASURE_DATE_FOUND +","
                    + COL_TREASURE_PHOTO_PATH +","+ COL_TREASURE_LOCATION_FOUND +","+ COL_TREASURE_WEIGHT +","+ COL_TREASURE_YEAR +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_TYPE +"='jewelry' ORDER BY (CASE WHEN "+sortType+" IS \"\" THEN 1 ELSE 0 END), "+sortType;
        }
        else if(sortType.equals("TreasureLocationFound"))
        {
            selectQuery = "SELECT "+ COL_TREASURE_ID +", "+ COL_TREASURE_MATERIAL +","+ COL_TREASURE_NAME +","+ COL_TREASURE_DATE_FOUND +","
                    + COL_TREASURE_PHOTO_PATH +","+ COL_TREASURE_LOCATION_FOUND +","+ COL_TREASURE_WEIGHT +","+ COL_TREASURE_YEAR +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_TYPE +"='jewelry' ORDER BY (CASE WHEN "+sortType+" IS \"\" THEN 1 ELSE 0 END), lower("+sortType+")";
        }
        //treasure date found, most recently added
        else
        {
            selectQuery = "SELECT "+ COL_TREASURE_ID +", "+ COL_TREASURE_MATERIAL +","+ COL_TREASURE_NAME +","+ COL_TREASURE_DATE_FOUND +","
                    + COL_TREASURE_PHOTO_PATH +","+ COL_TREASURE_LOCATION_FOUND +","+ COL_TREASURE_WEIGHT +","+ COL_TREASURE_YEAR +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_TYPE +"='jewelry' ORDER BY "+sortType+" DESC";
        }

        //Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                treasureList.add(new Treasure(cursor.getInt(cursor.getColumnIndex(COL_TREASURE_ID)), null, null, null, null, cursor.getString(cursor.getColumnIndex(COL_TREASURE_NAME)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_YEAR)), null, cursor.getString(cursor.getColumnIndex(COL_TREASURE_MATERIAL)),cursor.getString(cursor.getColumnIndex(COL_TREASURE_WEIGHT)) ,cursor.getString(cursor.getColumnIndex(COL_TREASURE_LOCATION_FOUND)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_DATE_FOUND)), null, cursor.getString(cursor.getColumnIndex(COL_TREASURE_PHOTO_PATH)), null));
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
            selectQuery = "SELECT "+ COL_TREASURE_ID +", "+ COL_TREASURE_NAME +","+ COL_TREASURE_YEAR +","+ COL_TREASURE_DATE_FOUND +","
                + COL_TREASURE_PHOTO_PATH +","+ COL_TREASURE_LOCATION_FOUND +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_TYPE +"='relic' ORDER BY (CASE WHEN "+sortType+" IS \"\" THEN 1 ELSE 0 END), "+"lower("+sortType+")";
        }
        //treasure date found, most recently added
        else
        {
            selectQuery = "SELECT "+ COL_TREASURE_ID +", "+ COL_TREASURE_NAME +","+ COL_TREASURE_YEAR +","+ COL_TREASURE_DATE_FOUND +","
                    + COL_TREASURE_PHOTO_PATH +","+ COL_TREASURE_LOCATION_FOUND +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_TYPE +"='relic' ORDER BY "+sortType+" DESC";
        }

        //Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                treasureList.add(new Treasure(cursor.getInt(cursor.getColumnIndex(COL_TREASURE_ID)), null, null, null, null, cursor.getString(cursor.getColumnIndex(COL_TREASURE_NAME)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_YEAR)), null, null,null ,cursor.getString(cursor.getColumnIndex(COL_TREASURE_LOCATION_FOUND)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_DATE_FOUND)), null, cursor.getString(cursor.getColumnIndex(COL_TREASURE_PHOTO_PATH)), null));
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
            selectQuery = "SELECT "+ COL_TREASURE_ID +", "+ COL_TREASURE_NAME +","+ COL_TREASURE_DATE_FOUND +","
                    + COL_TREASURE_PHOTO_PATH +","+ COL_TREASURE_LOCATION_FOUND +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_TYPE +"='collection' ORDER BY (CASE WHEN "+sortType+" IS \"\" THEN 1 ELSE 0 END), "+"lower("+sortType+")";
        }
        //treasure date found, most recently added
        else
        {
            selectQuery = "SELECT "+ COL_TREASURE_ID +", "+ COL_TREASURE_NAME +","+ COL_TREASURE_DATE_FOUND +","
                    + COL_TREASURE_PHOTO_PATH +","+ COL_TREASURE_LOCATION_FOUND +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_TYPE +"='collection' ORDER BY "+sortType+" DESC";
        }

        //Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                treasureList.add(new Treasure(cursor.getInt(cursor.getColumnIndex(COL_TREASURE_ID)), null, null, null, null, cursor.getString(cursor.getColumnIndex(COL_TREASURE_NAME)), null, null, null,null,cursor.getString(cursor.getColumnIndex(COL_TREASURE_LOCATION_FOUND)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_DATE_FOUND)), null, cursor.getString(cursor.getColumnIndex(COL_TREASURE_PHOTO_PATH)), null));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        //Log.d("myTag", ""+treasureList.size());

        return treasureList;
    }

    public Treasure getDetailedTreasure(int treasureId)
    {
        Treasure treasure;
        String selectQuery = "SELECT * FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_ID +"="+treasureId;
        //Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //Log.d("myTag", "query retrieved.");
        if(cursor.moveToFirst()){
            treasure = new Treasure(cursor.getInt(cursor.getColumnIndex(COL_TREASURE_ID)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_TYPE)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_COUNTRY)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_DENOMINATION)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_SERIES)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_NAME)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_YEAR)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_MINT)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_MATERIAL)),cursor.getString(cursor.getColumnIndex(COL_TREASURE_WEIGHT)) ,cursor.getString(cursor.getColumnIndex(COL_TREASURE_LOCATION_FOUND)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_DATE_FOUND)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_INFO)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_PHOTO_PATH)), cursor.getString(cursor.getColumnIndex(COL_TREASURE_WEIGHT_UNIT)));
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
            selectQuery = "SELECT "+ COL_CLAD_ID +","+ COL_CLAD_CURRENCY +","+ COL_CLAD_AMOUNT +","+ COL_CLAD_LOCATION_FOUND +","
                    + COL_CLAD_DATE_FOUND +" FROM "+TABLE_CLAD+" ORDER BY (CASE WHEN "+sortType+" IS \"\" THEN 1 ELSE 0 END), "+sortType;
        }
        //treasure date found, most recently added
        else
        {
            selectQuery = "SELECT "+ COL_CLAD_ID +","+ COL_CLAD_CURRENCY +","+ COL_CLAD_AMOUNT +","+ COL_CLAD_LOCATION_FOUND +","
                    + COL_CLAD_DATE_FOUND +" FROM "+TABLE_CLAD+" ORDER BY "+sortType+" DESC";
        }

        //Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                cladList.add(new Clad(cursor.getInt(cursor.getColumnIndex(COL_CLAD_ID)), cursor.getString(cursor.getColumnIndex(COL_CLAD_CURRENCY)), cursor.getDouble(cursor.getColumnIndex(COL_CLAD_AMOUNT)), cursor.getString(cursor.getColumnIndex(COL_CLAD_LOCATION_FOUND)), cursor.getString(cursor.getColumnIndex(COL_CLAD_DATE_FOUND))));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return cladList;
    }

    public HashMap<String, Integer> getSummaryTreasure()
    {
        HashMap<String, Integer> summaryList = new HashMap<>();
        String selectQuery = "SELECT "+ COL_TREASURE_TYPE +", COUNT("+ COL_TREASURE_TYPE +") FROM "+TABLE_TREASURE+" GROUP BY "+ COL_TREASURE_TYPE;
        //Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                //Log.d("myTag", "cursor entry...");
                //Log.d("myTag", cursor.getString(0)+": "+cursor.getInt(1));
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
        String selectQuery = "SELECT "+ COL_CLAD_CURRENCY +", SUM("+ COL_CLAD_AMOUNT +") FROM "+TABLE_CLAD+" GROUP BY "+ COL_CLAD_CURRENCY + " ORDER BY SUM("+ COL_CLAD_AMOUNT +") DESC";
        //Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                //Log.d("myTag", "cursor entry...");
                //Log.d("myTag", cursor.getString(0)+": "+cursor.getDouble(1));
                summaryList.put(cursor.getString(0), cursor.getDouble(1));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return summaryList;
    }

    public ArrayList<Treasure> getYearlySummaryTreasure()
    {
        ArrayList<Treasure> treasureList = new ArrayList<>();
        String selectQuery;

            selectQuery = "SELECT "+ COL_TREASURE_TYPE +","+ COL_TREASURE_DATE_FOUND +" FROM "+TABLE_TREASURE;

        //Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                treasureList.add(new Treasure(0, cursor.getString(cursor.getColumnIndex(COL_TREASURE_TYPE)), null, null, null, null, null, null, null, null, null, cursor.getString(cursor.getColumnIndex(COL_TREASURE_DATE_FOUND)), null, null, null));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return treasureList;
    }

    public ArrayList<Clad> getYearlySummaryClad()
    {
        ArrayList<Clad> cladList = new ArrayList<>();
        String selectQuery;

        selectQuery = "SELECT "+ COL_CLAD_CURRENCY +","+ COL_CLAD_AMOUNT +","+ COL_CLAD_DATE_FOUND +" FROM "+TABLE_CLAD;

        //Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                cladList.add(new Clad(0, cursor.getString(cursor.getColumnIndex(COL_CLAD_CURRENCY)), cursor.getDouble(cursor.getColumnIndex(COL_CLAD_AMOUNT)), null, cursor.getString(cursor.getColumnIndex(COL_CLAD_DATE_FOUND))));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return cladList;
    }

    public long addTreasure(Treasure treasure)
    {
        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(COL_TREASURE_COUNTRY, treasure.getTreasureCountry()); // get country
        values.put(COL_TREASURE_NAME, treasure.getTreasureName()); // get name
        values.put(COL_TREASURE_TYPE, treasure.getTreasureType()); // get type
        values.put(COL_TREASURE_DENOMINATION, treasure.getTreasureDenomination()); // get denomination
        values.put(COL_TREASURE_SERIES, treasure.getTreasureSeries()); // get series
        values.put(COL_TREASURE_YEAR, treasure.getTreasureYear()); // get year
        values.put(COL_TREASURE_MINT, treasure.getTreasureMint()); // get mint
        values.put(COL_TREASURE_MATERIAL, treasure.getTreasureMaterial()); // get material
        values.put(COL_TREASURE_WEIGHT, treasure.getTreasureWeight()); // get weight
        values.put(COL_TREASURE_DATE_FOUND, treasure.getTreasureDateFound()); // get datefound
        values.put(COL_TREASURE_LOCATION_FOUND, treasure.getTreasureLocationFound()); // get locationfound
        values.put(COL_TREASURE_INFO, treasure.getTreasureInfo()); // get info
        values.put(COL_TREASURE_PHOTO_PATH, treasure.getTreasurePhotoPath()); // get photopath
        values.put(COL_TREASURE_WEIGHT_UNIT, treasure.getTreasureWeightUnit()); // get weight unit

        //result will contain the row ID of the newly inserted row, or -1 if an error occurred.
        long result = db.insert(TABLE_TREASURE, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        //close
        db.close();
        return result;
    }

    public long editTreasure(Treasure treasure)
    {
        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COL_TREASURE_ID + " = ?";

        //create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(COL_TREASURE_COUNTRY, treasure.getTreasureCountry()); // get country
        values.put(COL_TREASURE_NAME, treasure.getTreasureName()); // get name
        values.put(COL_TREASURE_TYPE, treasure.getTreasureType()); // get type
        values.put(COL_TREASURE_DENOMINATION, treasure.getTreasureDenomination()); // get denomination
        values.put(COL_TREASURE_SERIES, treasure.getTreasureSeries()); // get series
        values.put(COL_TREASURE_YEAR, treasure.getTreasureYear()); // get year
        values.put(COL_TREASURE_MINT, treasure.getTreasureMint()); // get mint
        values.put(COL_TREASURE_MATERIAL, treasure.getTreasureMaterial()); // get material
        values.put(COL_TREASURE_WEIGHT, treasure.getTreasureWeight()); // get weight
        values.put(COL_TREASURE_DATE_FOUND, treasure.getTreasureDateFound()); // get datefound
        values.put(COL_TREASURE_LOCATION_FOUND, treasure.getTreasureLocationFound()); // get locationfound
        values.put(COL_TREASURE_INFO, treasure.getTreasureInfo()); // get info
        values.put(COL_TREASURE_WEIGHT_UNIT, treasure.getTreasureWeightUnit()); // get weight unit

        //result will contain the row ID of the newly inserted row, or -1 if an error occurred.
        long result = db.update(TABLE_TREASURE, values, whereClause, new String[]{Integer.toString(treasure.getTreasureId())});

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
        values.put(COL_CLAD_CURRENCY, clad.getCladCurrency()); // get currency
        values.put(COL_CLAD_AMOUNT, clad.getCladAmount()); // get amount
        values.put(COL_CLAD_LOCATION_FOUND, clad.getCladLocationFound()); // get location found
        values.put(COL_CLAD_DATE_FOUND, clad.getCladDateFound()); // get date found

        //result will contain the row ID of the newly inserted row, or -1 if an error occurred.
        long result = db.insert(TABLE_CLAD, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        //close
        db.close();
        return result;
    }

    public void deleteTreasure(String treasureID)
    {
        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_TREASURE, COL_TREASURE_ID +"=?",new String[]{treasureID});
        db.close();
    }

    public void deleteClad(String cladID)
    {
        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CLAD, COL_CLAD_ID +"=?",new String[]{cladID});
        db.close();
    }

    //function used for fetching data for exporting database
    public Cursor raw(String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        if(table.equals("treasure"))
        {
            return db.rawQuery("SELECT "+ COL_TREASURE_TYPE +","+ COL_TREASURE_DENOMINATION +","+ COL_TREASURE_SERIES +","+ COL_TREASURE_NAME +","+ COL_TREASURE_YEAR +","+ COL_TREASURE_MINT +","+ COL_TREASURE_MATERIAL +","+ COL_TREASURE_WEIGHT +","+ COL_TREASURE_LOCATION_FOUND +","+ COL_TREASURE_DATE_FOUND +","+ COL_TREASURE_INFO +" FROM " + TABLE_TREASURE , new String[]{});
        }
        else if(table.equals("clad"))
        {
            return db.rawQuery("SELECT "+ COL_CLAD_AMOUNT +","+ COL_CLAD_CURRENCY +","+ COL_CLAD_DATE_FOUND +","+ COL_CLAD_LOCATION_FOUND+" FROM " + TABLE_CLAD , new String[]{});
        }
        return null;
    }

    public void updateOldDates() {

        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //get all rows that are still using old date formats
        String selectQuery = "SELECT "+ COL_TREASURE_ID +","+ COL_TREASURE_DATE_FOUND +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_DATE_FOUND +" LIKE '%/%/____'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(COL_TREASURE_ID));
                String oldDate = cursor.getString(cursor.getColumnIndex(COL_TREASURE_DATE_FOUND));
                String[] splitDate = oldDate.split("/");

                String newDate = splitDate[2]+"/"+splitDate[0]+"/"+splitDate[1];

                //now update the date to proper yyyy/mm/dd format so it can be sorted correctly
                ContentValues cv = new ContentValues();
                cv.put(COL_TREASURE_DATE_FOUND,newDate);

                db.update(TABLE_TREASURE, cv, COL_TREASURE_ID +"="+id, null);

            } while(cursor.moveToNext());
        }

        //get all rows that are still using old date formats
        selectQuery = "SELECT "+ COL_CLAD_ID +","+ COL_CLAD_DATE_FOUND +" FROM "+TABLE_CLAD+" WHERE "+ COL_CLAD_DATE_FOUND +" LIKE '%/%/____'";
        cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(COL_CLAD_ID));
                String oldDate = cursor.getString(cursor.getColumnIndex(COL_CLAD_DATE_FOUND));
                String[] splitDate = oldDate.split("/");

                String newDate = splitDate[2]+"/"+splitDate[0]+"/"+splitDate[1];

                //now update the date to proper yyyy/mm/dd format so it can be sorted correctly
                ContentValues cv = new ContentValues();
                cv.put(COL_CLAD_DATE_FOUND,newDate);

                db.update(TABLE_CLAD, cv, COL_CLAD_ID +"="+id, null);

            } while(cursor.moveToNext());
        }

        //find out if user has any old treasure or clad rows using treasureDateFound with missing zero in front of month or day. i.e. 2019/1/9.
        // Update to 2019/01/09 to allow for proper sorting
        selectQuery = "SELECT "+ COL_TREASURE_ID +","+ COL_TREASURE_DATE_FOUND +" FROM "+TABLE_TREASURE+" WHERE "+ COL_TREASURE_DATE_FOUND +" LIKE '____/_/%' OR "+ COL_TREASURE_DATE_FOUND +" LIKE '____/%/_'";

        cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(COL_TREASURE_ID));
                String oldDate = cursor.getString(cursor.getColumnIndex(COL_TREASURE_DATE_FOUND));
                String[] splitDate = oldDate.split("/");

                for(int i=0; i<=2; i++)
                {
                    if(splitDate[i].length() == 1)
                    {
                        splitDate[i] = "0"+splitDate[i];
                    }
                }

                String newDate = splitDate[0]+"/"+splitDate[1]+"/"+splitDate[2];

                //now update the date to proper yyyy/mm/dd format so it can be sorted correctly
                ContentValues cv = new ContentValues();
                cv.put(COL_TREASURE_DATE_FOUND,newDate);

                db.update(TABLE_TREASURE, cv, COL_TREASURE_ID +"="+id, null);

            } while(cursor.moveToNext());
        }

        selectQuery = "SELECT "+ COL_CLAD_ID +","+ COL_CLAD_DATE_FOUND +" FROM "+TABLE_CLAD+" WHERE "+ COL_CLAD_DATE_FOUND +" LIKE '____/_/%' OR "+ COL_CLAD_DATE_FOUND +" LIKE '____/%/_'";

        cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(COL_CLAD_ID));
                String oldDate = cursor.getString(cursor.getColumnIndex(COL_CLAD_DATE_FOUND));
                String[] splitDate = oldDate.split("/");

                for(int i=0; i<=2; i++)
                {
                    if(splitDate[i].length() == 1)
                    {
                        splitDate[i] = "0"+splitDate[i];
                    }
                }

                String newDate = splitDate[0]+"/"+splitDate[1]+"/"+splitDate[2];

                //now update the date to proper yyyy/mm/dd format so it can be sorted correctly
                ContentValues cv = new ContentValues();
                cv.put(COL_CLAD_DATE_FOUND,newDate);

                db.update(TABLE_CLAD, cv, COL_CLAD_ID +"="+id, null);

            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    //removes all rows of specified type from db and returns arraylist of strings containing photopath of removed rows
    public ArrayList<String> deleteAllOfType(String type) {
        //get reference to writable DB
        SQLiteDatabase db;
        ArrayList<String> photoPathsOfRemovedList;

        if(type.endsWith("s"))
        {
            //remove s
            type = type.substring(0, type.length()-1);
        }
        //make lowercase
        type = type.toLowerCase();

        //user has selected to remove only clad
        if(type.equals("clad"))
        {
            //clad has no photos so does not need to return a photpathlist
            photoPathsOfRemovedList = null;
            db = this.getWritableDatabase();
            //remove clad rows from db
            db.delete(TABLE_CLAD, "1", null);
        }
        //if type was all, remove treasure and clad rows
        else if(type.equals("all"))
        {
            //get treasure photopaths of specified type
            photoPathsOfRemovedList = getPhotopathAllTreasure(type);
            db = this.getWritableDatabase();
            //remove treasure rows of all types from db
            db.delete(TABLE_TREASURE, "1", null);
            //remove clad rows from db
            db.delete(TABLE_CLAD, "1", null);
        }
        //remove a specified treasure type
        else
        {
            //get treasure photopaths of specified type
            photoPathsOfRemovedList = getPhotopathAllTreasure(type);
            db = this.getWritableDatabase();
            //remove treasure rows of specified type (or all) from db
            db.delete(TABLE_TREASURE, COL_TREASURE_TYPE+"=?", new String[]{type});
        }

        db.close();
        return photoPathsOfRemovedList;
    }
}
