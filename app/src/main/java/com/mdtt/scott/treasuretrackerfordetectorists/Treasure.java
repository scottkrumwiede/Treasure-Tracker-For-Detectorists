package com.mdtt.scott.treasuretrackerfordetectorists;

import java.util.LinkedHashMap;

/**
 * Created by Scott on 1/30/2018.
 */

public class Treasure{
    final private int treasureId;
    final private String treasureType;
    final private String treasureCountry;
    final private String treasureDenomination;
    final private String treasureSeries;
    final private String treasureName;
    final private String treasureYear;
    final private String treasureMint;
    final private String treasureMaterial;
    final private String treasureWeight;
    final private String treasureLocationFound;
    final private String treasureDateFound;
    final private String treasureInfo;
    final private String treasurePhotoPath;

    public Treasure(int treasureId, String treasureType, String treasureCountry, String treasureDenomination, String treasureSeries, String treasureName, String treasureYear, String treasureMint, String treasureMaterial, String treasureWeight, String treasureLocationFound, String treasureDateFound, String treasureInfo, String treasurePhotoPath) {
        this.treasureId = treasureId;
        this.treasureType = treasureType;
        this.treasureCountry = treasureCountry;
        this.treasureDenomination = treasureDenomination;
        this.treasureSeries = treasureSeries;
        this.treasureName = treasureName;
        this.treasureYear = treasureYear;
        this.treasureMint = treasureMint;
        this.treasureMaterial = treasureMaterial;
        this.treasureWeight = treasureWeight;
        this.treasureLocationFound = treasureLocationFound;
        this.treasureDateFound = treasureDateFound;
        this.treasureInfo = treasureInfo;
        this.treasurePhotoPath = treasurePhotoPath;
    }

    public int getTreasureId() {
        return treasureId;
    }

    public String getTreasureType() {
        return treasureType;
    }

    public String getTreasureCountry() {
        return treasureCountry;
    }

    public String getTreasureDenomination() {
        return treasureDenomination;
    }

    public String getTreasureSeries() {
        return treasureSeries;
    }

    public String getTreasureName() {
        return treasureName;
    }

    public String getTreasureYear() {
        return treasureYear;
    }

    public String getTreasureMint() {
        return treasureMint;
    }

    public String getTreasureMaterial() {
        return treasureMaterial;
    }

    public String getTreasureWeight() {
        return treasureWeight;
    }

    public String getTreasureLocationFound() {
        return treasureLocationFound;
    }

    public String getTreasureDateFound() {
        return treasureDateFound;
    }

    public String getTreasurePhotoPath() { return treasurePhotoPath; }

    public String getTreasureInfo() {
        return treasureInfo;
    }
    public LinkedHashMap<String, String> getTreasureDetailed(){
        LinkedHashMap<String, String> lhashMap = new LinkedHashMap<String, String>();
        lhashMap.put("Country: ",treasureCountry);
        lhashMap.put("Denomination: ",treasureDenomination);
        lhashMap.put("Name: ",treasureName);
        lhashMap.put("Series: ",treasureSeries);
        lhashMap.put("Year: ",treasureYear);
        lhashMap.put("Mint: ",treasureMint);
        lhashMap.put("Material: ",treasureMaterial);
        lhashMap.put("Weight: ",treasureWeight);
        lhashMap.put("Location Found: ",treasureLocationFound);
        lhashMap.put("Date Found: ",treasureDateFound);
        lhashMap.put("Additional Info: ",treasureInfo);
        return lhashMap;
    }
}