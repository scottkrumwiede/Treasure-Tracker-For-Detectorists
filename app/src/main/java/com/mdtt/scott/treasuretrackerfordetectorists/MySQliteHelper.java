package com.mdtt.scott.treasuretrackerfordetectorists;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Scott on 1/25/2018.
 */

public class MySQliteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 40;
    // Database Name
    private static final String DATABASE_NAME = "findsDB";
    // Treasures table name
    private static final String TABLE_TREASURE = "Treasure";
    private static final String TABLE_PHOTO = "Photo";
    // Treasure Table Columns names
    private static final String colTreasureID="TreasureID";
    private static final String colTreasureType="TreasureType";
    private static final String colTreasureName="TreasureName";
    private static final String colTreasureYear="TreasureYear";
    private static final String colTreasureLocationFound="TreasureLocationFound";
    private static final String colTreasureFoundYear="TreasureDateFound";
    private static final String colTreasurePhotoPath="TreasurePhotoPath";

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
                colTreasureName+" TEXT," +
                colTreasureYear+" INTEGER," +
                colTreasureLocationFound+" TEXT," +
                colTreasureFoundYear+" INTEGER," +
                colTreasurePhotoPath+" TEXT)";

        Log.d("myTag", createTreasureTable);
        db.execSQL(createTreasureTable);

        //prepare test image for blob storage
        //Bitmap bitmap = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.testpenny);
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        //byte[] image = stream.toByteArray();
        //Log.d("myTag", image.length+"");

        //test insert
        ContentValues values = new ContentValues();
        values.put(colTreasureType, "Coin");
        values.put(colTreasureName , "Wild Billy");
        values.put(colTreasureYear , 1943);
        //values.put(colTreasurePhoto, image);
        db.insert(TABLE_TREASURE, null, values);
        values.clear();
        values.put(colTreasureType, "Coin");
        values.put(colTreasureName , "Evolved Terror");
        values.put(colTreasureYear , 1964);
        //values.put(colTreasurePhoto, image);
        db.insert(TABLE_TREASURE, null, values);
        values.clear();
        values.put(colTreasureType, "Coin");
        values.put(colTreasureName , "U.S.Quadrinkle");
        values.put(colTreasureYear , 2001);
        //values.put(colTreasurePhoto, image);
        db.insert(TABLE_TREASURE, null, values);
        values.clear();

        for(int i=0; i<50; i++)
        {
            values.put(colTreasureType, "Coin");
            values.put(colTreasureName , "Poop Coin");
            values.put(colTreasureYear , 1776);
            //values.put(colTreasurePhoto, image);
            db.insert(TABLE_TREASURE, null, values);
            values.clear();
        }
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TREASURE);
        onCreate(db);
    }

    //query used to return id,names of a given treasure type to populate grid view.
    public ArrayList<Treasure> getTreasureNamesSortByAddDate(String type)
    {
        ArrayList<Treasure> treasureList = new ArrayList<>();
        String selectQuery = "SELECT "+colTreasureID+", "+colTreasureName+","+colTreasureYear+","+colTreasureFoundYear+","+colTreasurePhotoPath+" FROM "+TABLE_TREASURE+" WHERE "+colTreasureType+" =" + "'" + type + "'";
        Log.d("myTag", selectQuery);
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("myTag", "query retrieved.");

        if(cursor.moveToFirst()){
            do {
                //byte[] imageByte = cursor.getBlob(cursor.getColumnIndex(colTreasurePhoto));
                //if(imageByte != null)
                //{
                //   Bitmap image = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                //    treasureList.add(new Treasure(cursor.getInt(cursor.getColumnIndex(colTreasureID)), null, cursor.getString(cursor.getColumnIndex(colTreasureName)), cursor.getInt(cursor.getColumnIndex(colTreasureYear)), null, cursor.getInt(cursor.getColumnIndex(colTreasureDateFound)), image));
                //}
                //else
                //{
                    treasureList.add(new Treasure(cursor.getInt(cursor.getColumnIndex(colTreasureID)), null, cursor.getString(cursor.getColumnIndex(colTreasureName)), cursor.getInt(cursor.getColumnIndex(colTreasureYear)), null, cursor.getInt(cursor.getColumnIndex(colTreasureFoundYear)), null));
            //}
                //treasure.put(cursor.getInt(cursor.getColumnIndex(colTreasureID)), cursor.getString(cursor.getColumnIndex(colTreasureName)));
                //treasureList.add(treasure);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return treasureList;
    }

    //todo:finish this method
    public Cursor getTreasureNamesSortByTreasureYear(String type)
    {
        String selectQuery = "SELECT "+colTreasureID+", "+colTreasureName+" FROM " + TABLE_TREASURE + " WHERE "+colTreasureType+" =" + "'" + type + "'";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    //todo:finish this method
    public Cursor getTreasures(String type)
    {
        return null;
    }

    //todo:finish this method
    public Cursor getCladAll()
    {
        return null;
    }

    //todo:finish this method
    public Cursor getSummary()
    {
        return null;
    }

}
