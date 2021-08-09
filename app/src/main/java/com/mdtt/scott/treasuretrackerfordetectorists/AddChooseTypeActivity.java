package com.mdtt.scott.treasuretrackerfordetectorists;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class AddChooseTypeActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add a new treasure:");
        setContentView(R.layout.activity_add_choosetype);
        Button addCoinButton = findViewById(R.id.chooseAddCoin);
        Button addTokenButton = findViewById(R.id.chooseAddToken);
        Button addJewelryButton = findViewById(R.id.chooseAddJewelry);
        Button addRelicButton = findViewById(R.id.chooseAddRelic);
        Button addCladButton = findViewById(R.id.chooseAddClad);
        Button addCollectionButton = findViewById(R.id.chooseAddCollection);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        addCoinButton.setOnClickListener(view -> {
            Intent myIntent = new Intent(getApplicationContext(), AddActivity.class);
            myIntent.putExtra("type", "Coins:"); //Optional parameters
            startActivity(myIntent);
            finish();
        });

        addTokenButton.setOnClickListener(view -> {
            Intent myIntent = new Intent(getApplicationContext(), AddActivity.class);
            myIntent.putExtra("type", "Tokens:"); //Optional parameters
            startActivity(myIntent);
            finish();
        });

        addJewelryButton.setOnClickListener(view -> {
            Intent myIntent = new Intent(getApplicationContext(), AddActivity.class);
            myIntent.putExtra("type", "Jewelry:"); //Optional parameters
            startActivity(myIntent);
            finish();
        });

        addRelicButton.setOnClickListener(view -> {
            Intent myIntent = new Intent(getApplicationContext(), AddActivity.class);
            myIntent.putExtra("type", "Relics:"); //Optional parameters
            startActivity(myIntent);
            finish();
        });

        addCladButton.setOnClickListener(view -> {
            Intent myIntent = new Intent(getApplicationContext(), AddActivity.class);
            myIntent.putExtra("type", "Clad:"); //Optional parameters
            startActivity(myIntent);
            finish();
        });

        addCollectionButton.setOnClickListener(view -> {
            Intent myIntent = new Intent(getApplicationContext(), AddActivity.class);
            myIntent.putExtra("type", "Collections:"); //Optional parameters
            startActivity(myIntent);
            finish();
        });
    }
}
