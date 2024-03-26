package com.example.vayusamrakshak;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    EditText name, number, email, password;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Button signup_bt;
    UserInfo userInfo;
    LottieAnimationView load_ani;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name = findViewById(R.id.name_edit_text);
        number = findViewById(R.id.number_edit_txt);
        email = findViewById(R.id.email_edit_text2);
        password = findViewById(R.id.signup_password_edt);
        signup_bt = findViewById(R.id.signup_bt_1);
        load_ani = findViewById(R.id.loadani5);

        load_ani.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        signup_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load_ani.setVisibility(View.VISIBLE);
                signup_bt.setVisibility(View.GONE);

                String mail = email.getText().toString();
                String pass = password.getText().toString();
                String username = name.getText().toString();
                String usernumber = number.getText().toString();

                if (mail.equals("") || pass.equals("") || username.equals("") || usernumber.equals("")) {
                    load_ani.setVisibility(View.GONE);
                    signup_bt.setVisibility(View.VISIBLE);
                    Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!mail.matches(emailPattern)) {
                    load_ani.setVisibility(View.GONE);
                    signup_bt.setVisibility(View.VISIBLE);
                    Toast.makeText(SignupActivity.this, "Not a valid mail address", Toast.LENGTH_SHORT).show();
                } else if (pass.length() < 6) {
                    load_ani.setVisibility(View.GONE);
                    signup_bt.setVisibility(View.VISIBLE);
                    Toast.makeText(SignupActivity.this, "Password should be min 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(mail, pass)
                            .addOnCompleteListener(SignupActivity.this,new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).sendEmailVerification();
                                        userInfo = new UserInfo();
                                        String UID = mAuth.getUid();
                                        String ref = "userDetails/" + UID;

                                        databaseReference = firebaseDatabase.getReference(ref);
                                        addDatatoFirebase(username, usernumber, mail);

                                        Toast.makeText(SignupActivity.this, "Registration Completed", Toast.LENGTH_SHORT).show();
                                    }else {
                                        load_ani.setVisibility(View.GONE);
                                        signup_bt.setVisibility(View.VISIBLE);
                                        Toast.makeText(SignupActivity.this, "This email is already used !", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
    private void addDatatoFirebase(String name, String number, String email) {

        userInfo.setUserName(name);
        userInfo.setUserNumber(number);
        userInfo.setUserEmail(email);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    databaseReference.setValue(userInfo);
                    SharedPreferences settings = getApplicationContext().getSharedPreferences("UserData", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("UNAME" ,userInfo.getUserName().toString());
                    editor.putString("UNum" ,userInfo.getUserNumber().toString());
                    editor.apply();

                    //Toast.makeText(SignupActivity.this, "Registration Completed", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignupActivity.this, EmailVerficationActivity.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                    //Toast.makeText(SignupActivity.this, "This email is already used !", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignupActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}