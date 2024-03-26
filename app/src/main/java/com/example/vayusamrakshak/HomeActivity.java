package com.example.vayusamrakshak;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {
    Button liveGraph;
    ImageButton menu_bt, search_bt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        liveGraph=findViewById(R.id.liveData);
        liveGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this,LiveGraph.class);
                startActivity(i);
            }
        });
        menu_bt = findViewById(R.id.menu_bt);
        menu_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

    }
}