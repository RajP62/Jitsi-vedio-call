package com.example.connectyou_indiasfreevedioconferencingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText emailBox, passwordBox, nameBox;
    Button createButton, btnGoToLogin;

    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        emailBox = findViewById(R.id.emailBox_signUp);
        nameBox = findViewById(R.id.Name_signUp);
        passwordBox = findViewById(R.id.passwordBox_signUp);

        btnGoToLogin = findViewById(R.id.btn_AlreadyHaveAccount_signUpActivity);
        createButton = findViewById(R.id.CreateAccount_SignUpActivity);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass, name;
                email = emailBox.getText().toString();
                pass = passwordBox.getText().toString();
                name = nameBox.getText().toString();

                if (!email.contains("@gmail.com")) {
                    emailBox.setError("Please enter the right credentials");
                } else if (name.length() < 4) {
                    nameBox.setError("Username should be of minimum 4 digits");
                } else if (pass.isEmpty()) {
                    passwordBox.setError("Password should be of minimum 6 digits");
                } else {
                    User user = new User();
                    user.setEmail(email);
                    user.setPass(pass);
                    user.setName(name);

                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                database.collection("Users")
                                        .document().set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignupActivity.this, "Account is successfully created", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                    }
                                });
                            } else {
                                Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });
    }
}