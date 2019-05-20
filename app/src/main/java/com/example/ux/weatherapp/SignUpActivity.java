package com.example.ux.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private Button btnSignUp, btnSignIn;
    private EditText name, email, password;
    private ProgressBar progressBar;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        btnSignUp = findViewById(R.id.btn_signUp);
        btnSignIn = findViewById(R.id.sign_in_button);
        name = findViewById(R.id.input_name);
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        progressBar = findViewById(R.id.progressBar);

        btnSignUp.setOnClickListener(v -> {
//                signUp();
            String email2 = email.getText().toString().trim();
            String name2 = name.getText().toString().trim();
            String password2 = password.getText().toString().trim();

            if (TextUtils.isEmpty(email2)) {
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(name2)) {
                Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password2)) {
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password too short, enter minimum of 6 characters!",
                        Toast.LENGTH_SHORT).show();
                return;
            }


            progressBar.setVisibility(View.VISIBLE);
            //create user
            auth.createUserWithEmailAndPassword(email2, password2)
                    .addOnCompleteListener(SignUpActivity.this, task -> {
                        Toast.makeText(SignUpActivity.this, "Account created successfully" + task.isSuccessful(),
                                Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                        }
                    });
        });

        btnSignIn.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));

    }
}
