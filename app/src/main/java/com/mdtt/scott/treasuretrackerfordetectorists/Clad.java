package com.mdtt.scott.treasuretrackerfordetectorists;

import java.util.LinkedHashMap;

/**
 * Created by Scott on 8/27/2019.
 */

public class Clad{
    final private int cladId;
    final private String cladCurrency;
    final private double cladAmount;
    final private String cladLocationFound;
    final private String cladDateFound;

    public Clad(int cladId, String cladCurrency, double cladAmount, String cladLocationFound, String cladDateFound) {
        this.cladId = cladId;
        this.cladCurrency = cladCurrency;
        this.cladAmount = cladAmount;
        this.cladLocationFound = cladLocationFound;
        this.cladDateFound = cladDateFound;
    }

    public int getCladId() {
        return cladId;
    }

    public String getCladCurrency() {
        return cladCurrency;
    }

    public double getCladAmount() {
        return cladAmount;
    }

    public String getCladLocationFound() {
        return cladLocationFound;
    }

    public String getCladDateFound() {
        return cladDateFound;
    }

    public LinkedHashMap<String, String> getCladDetailed(){
        LinkedHashMap<String, String> lhashMap = new LinkedHashMap<String, String>();
        lhashMap.put("Currency: ",cladCurrency);
        lhashMap.put("Amount: ",Double.toString(cladAmount));
        lhashMap.put("Location Found: ",cladLocationFound);
        lhashMap.put("Date Found: ",cladDateFound);
        return lhashMap;
    }
}