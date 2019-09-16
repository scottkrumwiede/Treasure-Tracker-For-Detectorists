package com.mdtt.scott.treasuretrackerfordetectorists;

/**
 * Created by Scott on 8/27/2019.
 */

@SuppressWarnings("WeakerAccess")
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

}