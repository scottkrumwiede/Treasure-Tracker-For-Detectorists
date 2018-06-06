package com.mdtt.scott.treasuretrackerfordetectorists;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start home activity
        //SystemClock.sleep(5000);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        // close splash activity
        finish();
    }
}