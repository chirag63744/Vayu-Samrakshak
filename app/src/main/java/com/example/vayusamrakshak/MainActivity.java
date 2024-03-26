package com.example.vayusamrakshak;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                SharedPreferences settings = getApplicationContext().getSharedPreferences("UserData", 0);
                String mail = settings.getString("email", "");

                if (mail.equals("")) {
                    Intent i;
                    i = new Intent(MainActivity.this, Login.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i;
                    i = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 2500);
    }
}
