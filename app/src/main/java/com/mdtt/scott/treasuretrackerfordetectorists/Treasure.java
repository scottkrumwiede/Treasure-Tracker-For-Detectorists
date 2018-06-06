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
    final private Bitmap treasurePhoto;

    public Treasure(int treasureId, String treasureType, String treasureName, int treasureYear, String treasureLocationFound, int treasureFoundYear, Bitmap treasurePhoto) {
        this.treasureId = treasureId;
        this.treasureType = treasureType;
        this.treasureName = treasureName;
        this.treasureYear = treasureYear;
        this.treasureLocationFound = treasureLocationFound;
        this.treasureFoundYear = treasureFoundYear;
        this.treasurePhoto = treasurePhoto;
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

    public Bitmap getTreasurePhoto() {
        return treasurePhoto;
    }
}