package com.example.vayusamrakshak;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;

public class UserAccountCreationActivity extends AppCompatActivity {

    LottieAnimationView animationView, load_ani;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_creation);
        animationView = findViewById(R.id.accdoneani);
        load_ani = findViewById(R.id.loadani10);
        animationView.loop(false);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                Intent i = new Intent(UserAccountCreationActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        }, 5000);
    }
}