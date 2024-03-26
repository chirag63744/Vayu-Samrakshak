package com.example.vayusamrakshak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerficationActivity extends AppCompatActivity {

    Button mail_verify;
    LottieAnimationView load_ani;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verfication);
        mail_verify = findViewById(R.id.mail_verify_chk_bt);
        load_ani = findViewById(R.id.loadani8);
        load_ani.setVisibility(View.GONE);

        mail_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load_ani.setVisibility(View.VISIBLE);
                mail_verify.setVisibility(View.GONE);
                final boolean verify = false;
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                user.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        boolean verify = user.isEmailVerified();
                        if(verify){
                            SharedPreferences settings = getApplicationContext().getSharedPreferences("UserData", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("email" , user.getEmail());
                            editor.apply();
                            Intent i = new Intent(EmailVerficationActivity.this, UserAccountCreationActivity.class);
                            startActivity(i);
                            finish();
                        }else{
                            load_ani.setVisibility(View.GONE);
                            mail_verify.setVisibility(View.VISIBLE);
                            Toast.makeText(EmailVerficationActivity.this, "Please verify to continue", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}