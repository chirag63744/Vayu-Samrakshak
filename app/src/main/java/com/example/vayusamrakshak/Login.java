package com.example.vayusamrakshak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {

    TextView forgot_password, sign_up;
    Button login_button;
    FirebaseAuth mAuth;
    EditText email, password;
    LottieAnimationView load_ani;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase
        //FirebaseApp.initializeApp(this);

        forgot_password = findViewById(R.id.forgot_password_txt_view);
        sign_up = findViewById(R.id.sign_up_txt_view);
        login_button = findViewById(R.id.Login_bt);
        email = findViewById(R.id.email_edit_text);
        password = findViewById(R.id.email_password_edit_text);
        load_ani = findViewById(R.id.loadani4);
        firebaseDatabase = FirebaseDatabase.getInstance();

        load_ani.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        String temp = "<font color=#FF000000>Forgot</font> <font color=#2196F3>Password</font> <font color=#FF000000>?</font>";
        forgot_password.setText(Html.fromHtml(temp));

        temp = "<font color=#FF000000>Don't have an account ?</font> <font color=#2196F3>Sign up</font>";
        sign_up.setText(Html.fromHtml(temp));

        //Alert Dialog Box
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

        builder.setMessage("Please fill all the Details");

        builder.setTitle("Info");

        builder.setCancelable(false);
        //builder.setView(R.layout.customalert);

        builder.setPositiveButton("ok", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();

        boolean Connection = haveNetworkConnection();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!Connection){
                    Intent i =new Intent(Login.this, NoInternetActivity.class);
                    startActivity(i);
                }
            }
        }, 2000);


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load_ani.setVisibility(View.VISIBLE);
                login_button.setVisibility(View.GONE);
                String mail = email.getText().toString();
                String pass = password.getText().toString();

                if (mail.equals("") || pass.equals("")) {
                    alertDialog.show();
                    load_ani.setVisibility(View.GONE);
                    login_button.setVisibility(View.VISIBLE);
                    //Toast.makeText(Login_Activity.this, "Please fill all values", Toast.LENGTH_SHORT).show();
                } else if (!mail.matches(emailPattern)) {
                    load_ani.setVisibility(View.GONE);
                    login_button.setVisibility(View.VISIBLE);
                    Toast.makeText(Login.this, "Not a valid mail address", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(mail, pass)
                            .addOnCompleteListener(
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                boolean verify = user.isEmailVerified();

                                                if (!verify) {
                                                    Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).sendEmailVerification();
                                                    Intent i = new Intent(Login.this, EmailVerficationActivity.class);
                                                    startActivity(i);
                                                } else {

                                                    Toast.makeText(getApplicationContext(), "Login successful!!", Toast.LENGTH_LONG).show();

                                                    String Userid = mAuth.getUid();
                                                    String ref = "userDetails/" + Userid;

                                                    databaseReference = firebaseDatabase.getReference(ref);

                                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            //UserInfo userInfo = snapshot.getValue(UserInfo.class);
                                                            UserInfo userInfo = snapshot.getValue(UserInfo.class);
                                                            SharedPreferences settings = getApplicationContext().getSharedPreferences("UserData", 0);
                                                            SharedPreferences.Editor editor = settings.edit();
                                                            editor.putString("UNAME", userInfo.getUserName().toString());
                                                            editor.putString("UNum", userInfo.getUserNumber().toString());
                                                            editor.putString("email", userInfo.getUserEmail());
                                                            editor.apply();
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                        }
                                                    });

                                                    Intent intent
                                                            = new Intent(Login.this,
                                                            HomeActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                            else {
                                                load_ani.setVisibility(View.GONE);
                                                login_button.setVisibility(View.VISIBLE);
                                                Toast.makeText(getApplicationContext(),
                                                                "Invalid Password or mail !",
                                                                Toast.LENGTH_LONG)
                                                        .show();
                                                //progressbar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                }
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, SignupActivity.class);
                startActivity(i);
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, PasswordReset.class);
                startActivity(i);
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
