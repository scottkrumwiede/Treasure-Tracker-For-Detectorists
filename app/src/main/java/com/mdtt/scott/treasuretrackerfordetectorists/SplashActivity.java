package com.mdtt.scott.treasuretrackerfordetectorists;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;


public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start home activity

        MobileAds.initialize(this, "@string/admob_app_id");

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        // close splash activity
        finish();
    }
}