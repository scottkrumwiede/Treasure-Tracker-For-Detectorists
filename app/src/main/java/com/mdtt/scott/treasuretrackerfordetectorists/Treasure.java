package com.mdtt.scott.treasuretrackerfordetectorists;

import android.graphics.Bitmap;

/**
 * Created by Scott on 1/30/2018.
 */

public class Treasure{
    final private int treasureId;
    final private String treasureType;
    final private String treasureName;
    final private int treasureYear;
    final private String treasureLocationFound;
    final private int treasureFoundYear;
    final private String treasurePhotoPath;

    public Treasure(int treasureId, String treasureType, String treasureName, int treasureYear, String treasureLocationFound, int treasureFoundYear, String treasurePhotoPath) {
        this.treasureId = treasureId;
        this.treasureType = treasureType;
        this.treasureName = treasureName;
        this.treasureYear = treasureYear;
        this.treasureLocationFound = treasureLocationFound;
        this.treasureFoundYear = treasureFoundYear;
        this.treasurePhotoPath = treasurePhotoPath;
    }

    public int getTreasureId() {
        return treasureId;
    }

    public String getTreasureType() {
        return treasureType;
    }

    public String getTreasureName() {
        return treasureName;
    }

    public int getTreasureYear() {
        return treasureYear;
    }

    public String getTreasureLocationFound() {
        return treasureLocationFound;
    }

    public int getTreasureFoundYear() {
        return treasureFoundYear;
    }

    public String getTreasurePhotoPath() { return treasurePhotoPath; }

    //public Bitmap getTreasurePhoto() {
        //return treasurePhoto;
    //}
}