package com.example.vayusamrakshak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class NoInternetActivity extends AppCompatActivity {

    ImageButton retry;
    LottieAnimationView load_ani, no_net_ani;

    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        retry = findViewById(R.id.retry_bt2);
        txt = findViewById(R.id.no_net_txt2);
        no_net_ani = findViewById(R.id.no_net_ani2);
        load_ani = findViewById(R.id.loadani3);
        load_ani.setVisibility(View.GONE);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt.setVisibility(View.GONE);
                no_net_ani.setVisibility(View.GONE);
                retry.setVisibility(View.GONE);
                load_ani.setVisibility(View.VISIBLE);
                boolean Connection = haveNetworkConnection();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!Connection){
                            txt.setVisibility(View.VISIBLE);
                            no_net_ani.setVisibility(View.VISIBLE);
                            retry.setVisibility(View.VISIBLE);
                            load_ani.setVisibility(View.GONE);
                        }else {
                            finish();
                        }

                    }
                }, 3000);
            }
        });
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}