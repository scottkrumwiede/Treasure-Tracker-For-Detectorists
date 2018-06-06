package com.mdtt.scott.treasuretrackerfordetectorists;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AddChooseTypeActivity extends AppCompatActivity {

    Button addCoinButton, addTokenButton, addJewelryButton, addRelicButton, addCladButton;

    public AddChooseTypeActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add a new treasure:");
        setContentView(R.layout.activity_add_choose_type);
        addCoinButton = (Button) findViewById(R.id.chooseAddCoin);
        addTokenButton = (Button) findViewById(R.id.chooseAddToken);
        addJewelryButton = (Button) findViewById(R.id.chooseAddJewelry);
        addRelicButton = (Button) findViewById(R.id.chooseAddRelic);
        addCladButton = (Button) findViewById(R.id.chooseAddClad);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addCoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), AddTreasureActivity.class);
                myIntent.putExtra("type", "Coins:"); //Optional parameters
                startActivity(myIntent);
            }
        });

        addTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), AddTreasureActivity.class);
                myIntent.putExtra("type", "Tokens:"); //Optional parameters
                startActivity(myIntent);
            }
        });

        addJewelryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), AddTreasureActivity.class);
                myIntent.putExtra("type", "Jewelry:"); //Optional parameters
                startActivity(myIntent);
            }
        });

        addRelicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), AddTreasureActivity.class);
                myIntent.putExtra("type", "Relics:"); //Optional parameters
                startActivity(myIntent);
            }
        });

        addCladButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent myIntent = new Intent(getApplicationContext(), AddTreasureActivity.class);
                //myIntent.putExtra("type", "Coins:"); //Optional parameters
                //startActivity(myIntent);
            }
        });
    }
}
