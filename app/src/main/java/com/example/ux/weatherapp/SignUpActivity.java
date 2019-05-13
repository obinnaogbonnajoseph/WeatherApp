package com.example.ux.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    private Button btnSignUp, btnSignIn;
    private EditText name, email, password;
    private ProgressBar progressBar;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        btnSignUp = (Button) findViewById(R.id.btn_signUp);
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        name = (EditText)findViewById(R.id.input_name);
        email = (EditText)findViewById(R.id.input_email);
        password = (EditText)findViewById(R.id.input_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
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
                            }
                        });
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

    }


    public void signUp(){
        if (!validated()){
            onSignupFailed();
            return;
        }

        btnSignUp.setEnabled(false);


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
        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
        return;
    }


        progressBar.setVisibility(View.VISIBLE);
    //create user
        auth.createUserWithEmailAndPassword(email2, password2)
            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);

            if (!task.isSuccessful()) {
                Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                        Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        }
    });
            onSignupSuccess();
}

    public void onSignupSuccess() {
        btnSignUp.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        btnSignUp.setEnabled(true);
    }


    public boolean validated(){
        boolean valid = true;

        String name1 = name.getText().toString().trim();
        String email1 = email.getText().toString();
        String password1 = password.getText().toString();

        if (name1.isEmpty() || name1.length() < 3){
            name.setError("Enter atleast 3 letters");
        }else {
            name.setError(null);
        }

        if (email1.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
            email.setError("Please, enter correct email address");
        }else {
            email.setError(null);
        }

        if (password1.isEmpty() || password1.length() < 6 || password1.length() > 12){
            password.setError("password must be between 6 and 12 characters");
            valid = false;
        }else {
            password.setError(null);
        }
        return valid;
    }
}
